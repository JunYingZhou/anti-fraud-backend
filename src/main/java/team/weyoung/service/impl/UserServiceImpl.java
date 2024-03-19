package team.weyoung.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import team.weyoung.common.HttpCodeEnum;
import team.weyoung.exception.BusinessException;
import team.weyoung.mapper.UserMapper;
import team.weyoung.model.entity.User;
import team.weyoung.model.enums.UserRoleEnum;
import team.weyoung.model.vo.LoginUserVO;
import team.weyoung.model.vo.UserVO;
import team.weyoung.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static team.weyoung.model.entity.table.UserTableDef.USER;


/**
 * 用户服务实现
 *
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "Anti-Fraud";

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper query = QueryWrapper.create().from(USER).where(USER.USER_ACCOUNT.eq(userAccount));
            long count = this.count(query);
            if (count > 0) {
                throw new BusinessException(HttpCodeEnum.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 3. 插入数据
            User user = new User();
            user.setUserName(userAccount);
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(HttpCodeEnum.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper queryWrapper = QueryWrapper.create().from(USER).where(USER.USER_ACCOUNT.eq(userAccount)).where(USER.USER_PASSWORD.eq(encryptPassword));
        User user = userMapper.selectOneByQuery(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        StpUtil.login(user.getId());
        StpUtil.getSession().set("user", user);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        LoginUserVO loginUserVO = this.getLoginUserVO(user);
        loginUserVO.setToken(tokenInfo);
        return loginUserVO;
    }

    /**
     * 获取当前登录用户
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        long userId = StpUtil.getLoginIdAsLong();
        User currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(HttpCodeEnum.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        if (StpUtil.isLogin()) {
            long userId = StpUtil.getLoginIdAsLong();
            User currentUser = this.getById(userId);
            if (currentUser == null || currentUser.getId() == null) {
                return null;
            }
            return currentUser;
        }
        return null;
    }

    /**
     * 是否为管理员
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        User user = (User) StpUtil.getSession().get("user");
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 用户注销
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        StpUtil.logout();
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public boolean updatePassword(String oldPassword, String newPassword) {
        // 1. 校验
        if (newPassword.length() < 8) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR, "密码过短");
        }
        // 2. 加密
        String encryptOldPassword = DigestUtils.md5DigestAsHex((SALT + oldPassword).getBytes());
        String encryptNewPassword = DigestUtils.md5DigestAsHex((SALT + newPassword).getBytes());
        // 3. 查询用户
        long userId = StpUtil.getLoginIdAsLong();
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(HttpCodeEnum.NOT_LOGIN_ERROR);
        }
        // 4. 校验旧密码
        if (!encryptOldPassword.equals(user.getUserPassword())) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR, "旧密码错误");
        }
        // 5. 更新密码
        user.setUserPassword(encryptNewPassword);
        return this.updateById(user);
    }
}
