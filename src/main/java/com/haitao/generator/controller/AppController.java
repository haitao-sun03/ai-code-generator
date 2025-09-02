package com.haitao.generator.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.haitao.generator.annotations.HotKeyInvalidCache;
import com.haitao.generator.constant.AppConstant;
import com.haitao.generator.exception.BusinessException;
import com.haitao.generator.exception.ErrorCode;
import com.haitao.generator.model.ApiResponse;
import com.haitao.generator.model.entity.App;
import com.haitao.generator.model.request.app.*;
import com.haitao.generator.model.response.AppVO;
import com.haitao.generator.model.response.LoginUserVO;
import com.haitao.generator.service.AppService;
import com.haitao.generator.service.ProjectDownloadService;
import com.haitao.generator.service.UserService;
import com.haitao.generator.utils.ThrowUtils;
import com.mybatisflex.core.paginate.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * 应用 Controller
 *
 * @author haitao
 */
@RestController
@RequestMapping("/app")
@Slf4j
public class AppController {

    @Autowired
    private AppService appService;

    @Autowired
    private UserService userService;

    @Resource
    private ProjectDownloadService projectDownloadService;

    @GetMapping(value = "/chatToGenerate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @HotKeyInvalidCache(prefix = "app_history_",key = "#appId")
    public Flux<ServerSentEvent<String>> chatToGenerate(@RequestParam(value = "appId", required = true) Long appId,
                                                        @RequestParam(value = "userMessage", required = true) String userMessage) {
        Flux<String> contentFlux = appService.chatToGenerate(appId, userMessage);
        return contentFlux.map(chunk -> {
            Map<String, String> wrapper = Map.of("d", chunk);
            String jsonStr = JSONUtil.toJsonStr(wrapper);
            return ServerSentEvent
                    .<String>builder()
                    .data(jsonStr)
                    .build();
        }).concatWith(Mono.just(
                ServerSentEvent
                        .<String>builder()
                        .event("done")
                        .data("")
                        .build()
        ));
    }

    /**
     * 部署应用
     *
     * @param appId
     * @return 返回部署url
     */
    @GetMapping(value = "/deployApp")
    public ApiResponse<String> deployApp(@RequestParam(value = "appId", required = true) Long appId) {
        return ApiResponse.success(appService.deployApp(appId));

    }

    /**
     * 下载应用代码
     *
     * @param appId    应用ID
     * @param response 响应
     */
    @GetMapping("/download/{appId}")
    public void downloadAppCode(@PathVariable Long appId,
                                HttpServletResponse response) {
        // 1. 基础校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用ID无效");
        // 2. 查询应用信息
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        // 3. 权限校验：只有应用创建者可以下载代码
        LoginUserVO loginUser = userService.getLoginUser();
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限下载该应用代码");
        }
        // 4. 构建应用代码目录路径（生成目录，非部署目录）
        String codeGenType = app.getCodeGenType();
        String sourceDirName = codeGenType + "_" + appId;
        String sourceDirPath = AppConstant.CODE_OUTPUT_ROOT_DIR + File.separator + sourceDirName;
        // 5. 检查代码目录是否存在
        File sourceDir = new File(sourceDirPath);
        ThrowUtils.throwIf(!sourceDir.exists() || !sourceDir.isDirectory(),
                ErrorCode.NOT_FOUND_ERROR, "应用代码不存在，请先生成代码");
        // 6. 生成下载文件名（不建议添加中文内容）
        String downloadFileName = String.valueOf(appId);
        // 7. 调用通用下载服务
        projectDownloadService.downloadProject(sourceDirPath, downloadFileName, response);
    }

    /**
     * 创建应用
     *
     * @param appAddRequest 创建应用请求
     * @return 应用id
     */
    @PostMapping("/add")
    public ApiResponse<Long> addApp(@RequestBody @Valid AppAddRequest appAddRequest) {
        App app = new App();
        BeanUtil.copyProperties(appAddRequest, app);

        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        return ApiResponse.success(appService.createApp(app, userId, appAddRequest));
    }

    /**
     * 用户更新应用
     *
     * @param appUserUpdateRequest 用户更新应用请求
     * @return 是否成功
     */
    @PostMapping("/update")
    public ApiResponse<Boolean> updateApp(@RequestBody @Valid AppUserUpdateRequest appUserUpdateRequest) {
        App app = new App();
        BeanUtil.copyProperties(appUserUpdateRequest, app);
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        return ApiResponse.success(appService.updateUserApp(app, userId));
    }

    /**
     * 用户删除应用
     *
     * @param appDeleteRequest 删除应用请求
     * @return 是否成功
     */
    @PostMapping("/delete")
    public ApiResponse<Boolean> deleteApp(@RequestBody @Valid AppDeleteRequest appDeleteRequest) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        return ApiResponse.success(appService.deleteUserApp(appDeleteRequest.getId(), userId));
    }

    /**
     * 管理员更新应用
     *
     * @param appUpdateRequest 管理员更新应用请求
     * @return 是否成功
     */
    @PostMapping("/admin/update")
    @SaCheckRole("admin")
    public ApiResponse<Boolean> adminUpdateApp(@RequestBody @Valid AppUpdateRequest appUpdateRequest) {
        App app = new App();
        BeanUtil.copyProperties(appUpdateRequest, app);
        return ApiResponse.success(appService.updateAdminApp(app));
    }


    /**
     * 管理员删除应用
     *
     * @param appDeleteRequest 删除应用请求
     * @return 是否成功
     */
    @PostMapping("/admin/delete")
    @SaCheckRole("admin")
    public ApiResponse<Boolean> adminDeleteApp(@RequestBody @Valid AppDeleteRequest appDeleteRequest) {
        return ApiResponse.success(appService.deleteAdminApp(appDeleteRequest.getId()));
    }

    /**
     * 根据id获取应用详情
     *
     * @param id 应用id
     * @return 应用详情
     */
    @GetMapping("/get")
    public ApiResponse<AppVO> getAppById(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        App app = appService.getAppById(id);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(appService.getAppVO(app));
    }

    /**
     * 管理员根据id获取应用详情
     *
     * @param id 应用id
     * @return 应用详情
     */
    @GetMapping("/admin/get")
    @SaCheckRole("admin")
    public ApiResponse<AppVO> adminGetAppById(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        App app = appService.getAppById(id);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(appService.getAppVO(app));
    }

    /**
     * 分页获取当前用户的应用列表
     *
     * @param appQueryRequest 查询请求
     * @return 应用列表
     */
    @PostMapping("/list/page/my")
    public ApiResponse<Page<AppVO>> listMyAppByPage(@RequestBody AppQueryRequest appQueryRequest) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        appQueryRequest.setUserId(userId);
        long pageNum = appQueryRequest.getPageNum();
        long pageSize = appQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR, "数量不能超过20条");
        Page<App> appPage = appService.page(Page.of(pageNum, pageSize),
                appService.getPageQueryWrapper(appQueryRequest));
        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        List<AppVO> appVOList = appService.getAppVOList(appPage.getRecords());
        appVOPage.setRecords(appVOList);
        return ApiResponse.success(appVOPage);
    }

    /**
     * 分页获取精选应用列表
     *
     * @param appQueryRequest 查询请求
     * @return 应用列表
     */
    @PostMapping("/list/page")
    @Cached(name = "featured_app_page:", key = "T(com.haitao.generator.utils.CacheKeyUtils).generateKey(#appQueryRequest)", expire = 3600, cacheNullValue = true, cacheType = CacheType.REMOTE)
    public ApiResponse<Page<AppVO>> listFeaturedAppByPage(@RequestBody AppQueryRequest appQueryRequest) {
        long pageNum = appQueryRequest.getPageNum();
        long pageSize = appQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);
        Page<App> appPage = appService.page(Page.of(pageNum, pageSize),
                appService.getPageQueryWrapper(appQueryRequest));
        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        List<AppVO> appVOList = appService.getAppVOList(appPage.getRecords());
        appVOPage.setRecords(appVOList);
        return ApiResponse.success(appVOPage);
    }

    /**
     * 管理员分页获取应用列表
     *
     * @param appQueryRequest 查询请求
     * @return 应用列表
     */
    @PostMapping("/admin/list/page")
    @SaCheckRole("admin")
    public ApiResponse<Page<AppVO>> adminListAppByPage(@RequestBody AppQueryRequest appQueryRequest) {
        long pageNum = appQueryRequest.getPageNum();
        long pageSize = appQueryRequest.getPageSize();
        Page<App> appPage = appService.page(Page.of(pageNum, pageSize),
                appService.getPageQueryWrapper(appQueryRequest));
        List<AppVO> appVOList = appService.getAppVOList(appPage.getRecords());

        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        appVOPage.setRecords(appVOList);
        return ApiResponse.success(appVOPage);
    }
}