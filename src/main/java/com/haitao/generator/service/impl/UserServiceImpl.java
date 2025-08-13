package com.haitao.generator.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.haitao.generator.enums.UserRoleEnum;
import com.haitao.generator.exception.BusinessException;
import com.haitao.generator.exception.ErrorCode;
import com.haitao.generator.model.request.UserLoginRequest;
import com.haitao.generator.model.request.UserQueryRequest;
import com.haitao.generator.model.request.UserRegisterRequest;
import com.haitao.generator.model.response.LoginUserVO;
import com.haitao.generator.model.response.UserVO;
import com.haitao.generator.utils.EncrptUtils;
import com.haitao.generator.utils.ThrowUtils;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.haitao.generator.model.entity.User;
import com.haitao.generator.mapper.UserMapper;
import com.haitao.generator.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表 服务层实现。
 *
 * @author haitao
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private static final String DEFAULT_USER_NAME = "无名";
    private static final String DEFAULT_USER_ROLE = UserRoleEnum.USER.getValue();

    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        ThrowUtils.throwIf(!password.equals(checkPassword), ErrorCode.PARAMS_ERROR, "密码与确认密码不一致");

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_account", userAccount);
        User existUser = this.mapper.selectOneByQuery(queryWrapper);
        ThrowUtils.throwIf(ObjUtil.isNotNull(existUser), ErrorCode.PARAMS_ERROR, "账号已存在");
        String encryptPassword = EncrptUtils.getEncryptPassword(password);
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName(DEFAULT_USER_NAME);
        user.setUserRole(DEFAULT_USER_ROLE);
        this.save(user);
        return user.getId();
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }

        LoginUserVO loginUserVO = BeanUtil.copyProperties(user, LoginUserVO.class);
        return loginUserVO;
    }

    @Override
    public LoginUserVO userLogin(UserLoginRequest userLoginRequest) {
        String userAccount = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getUserPassword();
        String encryptPassword = EncrptUtils.getEncryptPassword(password);

        QueryWrapper queryWrapper = QueryWrapper
                .create()
                .eq("user_account", userAccount)
                .eq("user_password", encryptPassword);
        User existUser = this.mapper.selectOneByQuery(queryWrapper);
        ThrowUtils.throwIf(ObjUtil.isEmpty(existUser), ErrorCode.SYSTEM_ERROR, "用户名或密码错误");

        StpUtil.login(existUser.getId());
        return getLoginUserVO(existUser);
    }

    @Override
    public boolean isLogin() {
        return StpUtil.isLogin();
    }

    @Override
    public boolean userLogout() {
        try {
            StpUtil.logout();
            return true;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户退出登录失败" + e.getMessage());
        }

    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }

        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return List.of();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    public QueryWrapper getPageQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        return QueryWrapper.create()
                .eq("id", id)
                .eq("userRole", userRole)
                .like("userAccount", userAccount)
                .like("userName", userName)
                .like("userProfile", userProfile)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }


}
