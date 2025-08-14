package com.haitao.generator.service;

import com.haitao.generator.model.request.UserLoginRequest;
import com.haitao.generator.model.request.UserQueryRequest;
import com.haitao.generator.model.request.UserRegisterRequest;
import com.haitao.generator.model.response.LoginUserVO;
import com.haitao.generator.model.response.UserVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.haitao.generator.model.entity.User;

import java.util.List;

/**
 * 用户表 服务层。
 *
 * @author haitao
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    long userRegister(UserRegisterRequest userRegisterRequest);


    /**
     * 用户信息脱敏
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);


    /**
     * 用户登录
     *
     * @param userLoginRequest  用户登录信息
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(UserLoginRequest userLoginRequest);

    /**
     * 是否登录
     * @return
     */
    boolean isLogin();

    /**
     * 获取登录用户
     * @return
     */
    LoginUserVO getLoginUser();

    /**
     * 用户注销
     *
     * @return
     */
    boolean userLogout();

//    --------------------------

    /**
     * 脱敏user
     */
    UserVO getUserVO(User user);

    /**
     * 脱敏userList
     * @param userList
     * @return
     */
    List<UserVO> getUserVOList(List<User> userList);


    /**
     * 分页查询条件构造
     * @param userQueryRequest
     * @return
     */
    QueryWrapper getPageQueryWrapper(UserQueryRequest userQueryRequest);



}
