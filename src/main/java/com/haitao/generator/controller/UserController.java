package com.haitao.generator.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.bean.BeanUtil;
import com.haitao.generator.exception.BusinessException;
import com.haitao.generator.exception.ErrorCode;
import com.haitao.generator.model.ApiResponse;
import com.haitao.generator.model.request.*;
import com.haitao.generator.model.response.LoginUserVO;
import com.haitao.generator.model.response.UserVO;
import com.haitao.generator.utils.EncrptUtils;
import com.haitao.generator.utils.ThrowUtils;
import com.mybatisflex.core.paginate.Page;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.haitao.generator.model.entity.User;
import com.haitao.generator.service.UserService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户表 控制层。
 *
 * @author haitao
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册
     *
     * @param userRegisterRequest 用户注册请求参数
     * @return 用户注册id
     */
    @PostMapping("/save")
    public ApiResponse<Long> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        return ApiResponse.success(userService.userRegister(userRegisterRequest));
    }


    /**
     * 登录
     *
     * @param userLoginRequest 登录参数
     * @return 用户注册id
     */
    @PostMapping("/login")
    public ApiResponse<LoginUserVO> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        return ApiResponse.success(userService.userLogin(userLoginRequest));
    }


    /**
     * 获取当前登录用户
     * @return
     */
    @PostMapping("/login/user")
    public ApiResponse<LoginUserVO> getLoginUser() {
        return ApiResponse.success(userService.getLoginUser());
    }


    /**
     * 判断用户是否登录
     *
     */
    @PostMapping("/isLogin")
    public ApiResponse<Boolean> isLogin() {
        return ApiResponse.success(userService.isLogin());
    }


    /**
     * 退出登录
     *
     * @return 用户注册id
     */
    @PostMapping("/logout")
    public ApiResponse<Boolean> logout() {
        return ApiResponse.success(userService.userLogout());
    }


    /**
     * 创建用户
     */
    @PostMapping("/add")
    @SaCheckRole("admin")
    public ApiResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        final String DEFAULT_PASSWORD = "12345678";
        String encryptPassword = EncrptUtils.getEncryptPassword(DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(user.getId());
    }

    /**
     * 根据 id 获取用户（仅管理员）
     */
    @GetMapping("/get")
    @SaCheckRole("admin")
    public ApiResponse<User> getUserById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(user);
    }

    /**
     * 根据 id 获取包装类
     */
    @GetMapping("/get/vo")
    public ApiResponse<UserVO> getUserVOById(long id) {
        ApiResponse<User> response = getUserById(id);
        User user = response.getData();
        return ApiResponse.success(userService.getUserVO(user));
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    @SaCheckRole("admin")
    public ApiResponse<Boolean> deleteUser(@RequestBody @Valid UserDeleteRequest userDeleteRequest) {

        boolean b = userService.removeById(userDeleteRequest.getId());
        return ApiResponse.success(b);
    }

    /**
     * 更新用户
     */
    @PostMapping("/update")
    @SaCheckRole("admin")
    public ApiResponse<Boolean> updateUser(@RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 分页获取用户封装列表（仅管理员）
     *
     * @param userQueryRequest 查询请求参数
     */
    @PostMapping("/list/page/vo")
    public ApiResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long pageNum = userQueryRequest.getPageNum();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(Page.of(pageNum, pageSize),
                userService.getPageQueryWrapper(userQueryRequest));
        // 数据脱敏
        Page<UserVO> userVOPage = new Page<>(pageNum, pageSize, userPage.getTotalRow());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ApiResponse.success(userVOPage);
    }

}
