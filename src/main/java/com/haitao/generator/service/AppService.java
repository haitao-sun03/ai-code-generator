package com.haitao.generator.service;

import com.haitao.generator.model.entity.App;
import com.haitao.generator.model.entity.User;
import com.haitao.generator.model.request.app.AppAddRequest;
import com.haitao.generator.model.request.app.AppQueryRequest;
import com.haitao.generator.model.response.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用表 服务层。
 *
 * @author haitao
 */
public interface AppService extends IService<App> {

    /**
     * 生成应用代码并保存指定目录
     * @param appId
     * @param userMessage
     * @return
     */
    Flux<String> chatToGenerate(Long appId, String userMessage);

    /**
     * 部署应用到指定目录
     * @param appId
     * @return
     */
    String deployApp(Long appId);



    /**
     * 创建应用
     * @param app 应用信息
     * @param userId 用户id
     * @return 应用id
     */
    long createApp(App app, Long userId, AppAddRequest appAddRequest);

    /**
     * 用户更新应用
     * @param app 应用信息
     * @param userId 用户id
     * @return 是否成功
     */
    boolean updateUserApp(App app, Long userId);

    /**
     * 管理员更新应用
     * @param app 应用信息
     * @return 是否成功
     */
    boolean updateAdminApp(App app);

    /**
     * 用户删除应用
     * @param id 应用id
     * @param userId 用户id
     * @return 是否成功
     */
    boolean deleteUserApp(Long id, Long userId);

    /**
     * 管理员删除应用
     * @param id 应用id
     * @return 是否成功
     */
    boolean deleteAdminApp(Long id);

    /**
     * 获取应用详情
     * @param id 应用id
     * @return 应用详情
     */
    App getAppById(Long id);

    /**
     * 应用信息脱敏
     * @param app 应用信息
     * @return 脱敏后的应用信息
     */
    AppVO getAppVO(App app);

    /**
     * 应用列表信息脱敏
     * @param appList 应用列表
     * @return 脱敏后的应用列表
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 分页查询条件构造
     * @param appQueryRequest 查询条件
     * @return 查询条件包装器
     */
    QueryWrapper getPageQueryWrapper(AppQueryRequest appQueryRequest);

}