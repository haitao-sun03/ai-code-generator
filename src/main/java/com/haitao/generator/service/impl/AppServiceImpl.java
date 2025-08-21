package com.haitao.generator.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.haitao.generator.ai.AiCodeGenTypeRouteService;
import com.haitao.generator.core.AiCodeGeneratorFacade;
import com.haitao.generator.core.builder.VueProjectBuilder;
import com.haitao.generator.core.stream.handler.StreamHandler;
import com.haitao.generator.enums.ChatMessageTypeEnum;
import com.haitao.generator.enums.CodeGenTypeEnum;
import com.haitao.generator.exception.BusinessException;
import com.haitao.generator.exception.ErrorCode;
import com.haitao.generator.mapper.AppMapper;
import com.haitao.generator.model.entity.App;
import com.haitao.generator.model.entity.User;
import com.haitao.generator.model.request.app.AppAddRequest;
import com.haitao.generator.model.request.app.AppQueryRequest;
import com.haitao.generator.model.response.AppVO;
import com.haitao.generator.model.response.UserVO;
import com.haitao.generator.service.AppService;
import com.haitao.generator.service.ChatHistoryService;
import com.haitao.generator.service.ScreenShotService;
import com.haitao.generator.service.UserService;
import com.haitao.generator.utils.ThrowUtils;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import opennlp.tools.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.haitao.generator.constant.AppConstant.*;

/**
 * 应用 服务实现类
 *
 * @author haitao
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Autowired
    private UserService userService;

    @Autowired
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Autowired
    @Lazy
    private ChatHistoryService chatHistoryService;

    @Autowired
    private List<StreamHandler> streamHandlers;

    @Resource
    private VueProjectBuilder vueProjectBuilder;

    @Resource
    private ScreenShotService screenShotService;

    @Resource
    private AiCodeGenTypeRouteService aiCodeGenTypeRouteService;

    @Override
    public Flux<String> chatToGenerate(Long appId, String userMessage) {
//        校验参数
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用Id非法");
        ThrowUtils.throwIf(StrUtil.isBlank(userMessage), ErrorCode.PARAMS_ERROR, "用户提示词不能为空");

        App app = this.getAppById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在：" + appId);
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        ThrowUtils.throwIf(!userId.equals(app.getUserId()), ErrorCode.NO_AUTH_ERROR, "登录用户不是当前应用拥有者");
        String codeGenType = app.getCodeGenType();
        CodeGenTypeEnum codeGenTypeEnum = CodeGenTypeEnum.getEnumByValue(codeGenType);
//保存用户消息
        chatHistoryService.addChatMessage(appId, userMessage, ChatMessageTypeEnum.USER.getValue(), userId);

//        生成内容
        Flux<String> aiResponseFlux = aiCodeGeneratorFacade.generateAndSaveCodeStream(userMessage, codeGenTypeEnum, appId);
        for (StreamHandler streamHandler : streamHandlers) {
            if (streamHandler.support(codeGenTypeEnum)) {
                return streamHandler.handle(aiResponseFlux, chatHistoryService, appId, userId);
            }
        }

        return aiResponseFlux;
    }

    @Override
    public String deployApp(Long appId) {
//        参数校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用Id非法");
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在：" + appId);
//        校验当前登录用户
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        ThrowUtils.throwIf(!userId.equals(app.getUserId()), ErrorCode.NO_AUTH_ERROR, "登录用户不是当前应用拥有者");
//        查询是否部署过
        String deployKey = app.getDeployKey();
        if (StrUtil.isBlank(deployKey)) {
            deployKey = RandomUtil.randomString(8);
        }
        String codeGenType = app.getCodeGenType();
        String sourceDir = CODE_OUTPUT_ROOT_DIR + File.separator + codeGenType + "_" + appId;
        File sourceDirFile = new File(sourceDir);
        if (!sourceDirFile.exists() || !sourceDirFile.isDirectory()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "请先生成应用");
        }

//        如果是VUE项目，需要移动的是项目的打包目录dist，特殊处理
        if (codeGenType.equals(CodeGenTypeEnum.VUE_PROJECT.getValue())) {
            boolean isBuildSuccess = vueProjectBuilder.buildProject(sourceDir);
            ThrowUtils.throwIf(!isBuildSuccess, ErrorCode.SYSTEM_ERROR, "部署VUE项目失败，请重试");
            File distDir = new File(sourceDir, "dist");
            ThrowUtils.throwIf(!distDir.exists(), ErrorCode.SYSTEM_ERROR, "部署VUE项目成功，但dist目录未生成，请重试");
            sourceDirFile = distDir;
        }
        //        移动文件到部署目录
        String destDir = CODE_DEPLOY_ROOT_DIR + File.separator + deployKey;
        File destDirFile = new File(destDir);
        try {
            FileUtil.copyContent(sourceDirFile, destDirFile, true);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "部署应用失败：" + e.getMessage());
        }

//        更新数据库
        App updateApp = new App();
        updateApp.setId(appId);
        updateApp.setDeployKey(deployKey);
        updateApp.setDeployedTime(LocalDateTime.now());

        boolean updateResult = this.updateById(updateApp);
        ThrowUtils.throwIf(!updateResult, ErrorCode.OPERATION_ERROR, "部署应用后更新数据库失败");
        String deployUrl = StrUtil.format("{}/{}", CODE_DEPLOY_HOST, deployKey);
//        异步进行截图，压缩，上传COS，清理，更新app cover字段
        screenShotAndUploadCOSAsync(deployUrl, appId);
        return deployUrl;
    }

    private void screenShotAndUploadCOSAsync(String webUrl, long appId) {
        Thread.startVirtualThread(() -> {
            String cosUrl = screenShotService.screenShotAndUpload(webUrl);
            App app = new App();
            app.setId(appId);
            app.setCover(cosUrl);
            boolean updateRes = this.updateById(app);
            ThrowUtils.throwIf(!updateRes, ErrorCode.OPERATION_ERROR, "更新app cover失败：" + appId);
        });
    }

    @Override
    public long createApp(App app, Long userId, AppAddRequest appAddRequest) {
        ThrowUtils.throwIf(app == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不合法");
        ThrowUtils.throwIf(app.getInitPrompt() == null || app.getInitPrompt().isEmpty(), ErrorCode.PARAMS_ERROR, "初始提示词不能为空");

        String appName = appAddRequest.getInitPrompt().length() > 12 ? appAddRequest.getInitPrompt().substring(0, 12) : appAddRequest.getInitPrompt();
        app.setAppName(appName);
        app.setUserId(userId);
        //使用智能路由，由AI选择合适的项目生成类型
        CodeGenTypeEnum decidedCodeGenTypeEnum = aiCodeGenTypeRouteService.routeToCodeGenType(appAddRequest.getInitPrompt());
        app.setCodeGenType(decidedCodeGenTypeEnum.getValue());
        // 默认优先级为0
        app.setPriority(0);

        boolean result = this.save(app);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return app.getId();
    }

    @Override
    public boolean updateUserApp(App app, Long userId) {
        ThrowUtils.throwIf(app == null || app.getId() == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不合法");

        // 查询原应用信息，判断是否存在且属于当前用户
        App oldApp = this.getById(app.getId());
        ThrowUtils.throwIf(oldApp == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(!oldApp.getUserId().equals(userId), ErrorCode.NO_AUTH_ERROR, "无权限修改他人应用");

        // 用户只能修改应用名称
        App updateApp = new App();
        updateApp.setId(app.getId());
        updateApp.setAppName(app.getAppName());

        return this.updateById(updateApp);
    }

    @Override
    public boolean updateAdminApp(App app) {
        ThrowUtils.throwIf(app == null || app.getId() == null, ErrorCode.PARAMS_ERROR);

        // 查询原应用信息，判断是否存在
        App oldApp = this.getById(app.getId());
        ThrowUtils.throwIf(oldApp == null, ErrorCode.NOT_FOUND_ERROR);

        // 管理员可以修改应用名称、应用封面、优先级
        App updateApp = new App();
        updateApp.setId(app.getId());
        if (app.getAppName() != null) {
            updateApp.setAppName(app.getAppName());
        }
        if (app.getCover() != null) {
            updateApp.setCover(app.getCover());
        }
        if (app.getPriority() != null) {
            updateApp.setPriority(app.getPriority());
        }

        return this.updateById(updateApp);
    }

    @Override
    public boolean deleteUserApp(Long id, Long userId) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不合法");

        // 查询原应用信息，判断是否存在且属于当前用户
        App app = this.getById(id);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(!app.getUserId().equals(userId), ErrorCode.NO_AUTH_ERROR, "无权限删除他人应用");

        return this.removeById(id);
    }

    @Override
    public boolean deleteAdminApp(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);

        // 查询原应用信息，判断是否存在
        App app = this.getById(id);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);

        return this.removeById(id);
    }

    @Override
    public App getAppById(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        return this.getById(id);
    }

    @Override
    public AppVO getAppVO(App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = BeanUtil.copyProperties(app, AppVO.class);
        User user = userService.getById(appVO.getUserId());
        UserVO userVO = userService.getUserVO(user);
        appVO.setUserVO(userVO);
        return appVO;
    }

    @Override
    public List<AppVO> getAppVOList(List<App> appList) {
        if (CollUtil.isEmpty(appList)) {
            return List.of();
        }
        Set<Long> userIds = appList.stream().map(App::getUserId).collect(Collectors.toSet());
        Map<Long, UserVO> userVOMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(User::getId, userService::getUserVO));
        return appList.stream().map(app -> {
            AppVO appVO = this.getAppVO(app);
            UserVO userVO = userVOMap.get(app.getUserId());
            appVO.setUserVO(userVO);
            return appVO;
        }).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper getPageQueryWrapper(AppQueryRequest appQueryRequest) {
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        Long id = appQueryRequest.getId();
        Long userId = appQueryRequest.getUserId();
        String appName = appQueryRequest.getAppName();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();


        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq("id", id)
                .eq("user_id", userId)
                .like("app_name", appName);

        // 默认按照优先级和创建时间排序
        if (sortField == null) {
            queryWrapper.orderBy("priority", "descend".equals(sortOrder))
                    .orderBy("create_time", "descend".equals(sortOrder));
        } else {
            queryWrapper.orderBy(sortField, "ascend".equals(sortOrder));
        }

        return queryWrapper;
    }

    /**
     * 重写Mybatis-flex父类方法，其中补充删除对话历史记录的逻辑
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR, "待删除的appId不能为空");
        chatHistoryService.deleteChatMessageByAppId((Long) id);
        return super.removeById(id);

    }
}