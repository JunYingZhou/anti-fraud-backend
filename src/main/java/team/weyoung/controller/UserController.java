package team.weyoung.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.google.gson.Gson;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import team.weyoung.annotation.AuthCheck;
import team.weyoung.common.*;
import team.weyoung.constant.UserConstant;
import team.weyoung.exception.BusinessException;
import team.weyoung.exception.ThrowUtils;
import team.weyoung.model.dto.user.*;
import team.weyoung.model.entity.User;
import team.weyoung.model.vo.LoginUserVO;
import team.weyoung.model.vo.UserVO;
import team.weyoung.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 *
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "TuNan";

    private final static Gson GSON = new Gson();

    @Resource
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return Result.success(result);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return Result.success(loginUserVO);
    }

    /**
     * 用户注销
     */
    @PostMapping("/logout")
    public Result<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return Result.success(result);
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/get/login")
    public Result<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return Result.success(userService.getLoginUserVO(user));
    }


    /**
     * 管理员创建用户
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        if (userAddRequest == null) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + 123456).getBytes());
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, HttpCodeEnum.OPERATION_ERROR);
        return Result.success(user.getId());
    }

    /**
     * 管理员删除用户
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return Result.success(b);
    }

    /**
     * 管理员更新用户
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, HttpCodeEnum.OPERATION_ERROR);
        return Result.success(true);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<User> getUserById(long id) {
        if (id <= 0) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, HttpCodeEnum.NOT_FOUND_ERROR);
        return Result.success(user);
    }

    /**
     * 根据 id 获取包装类
     */
    @GetMapping("/get/vo")
    public Result<UserVO> getUserVOById(long id) {
        Result<User> response = getUserById(id);
        User user = response.getData();
        return Result.success(userService.getUserVO(user));
    }

    /**
     * 分页获取用户列表（仅管理员）
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        long pageNumber = userQueryRequest.getPageNumber();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(pageNumber, pageSize));
        return Result.success(userPage);
    }

    /**
     * 分页获取用户封装列表
     */
    @PostMapping("/list/page/vo")
    public Result<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR);
        }
        long pageNumber = userQueryRequest.getPageNumber();
        long pageSize = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, HttpCodeEnum.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(pageNumber, pageSize), new QueryWrapper());
        Page<UserVO> userVOPage = new Page<>(pageNumber, pageSize, userPage.getTotalRow());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return Result.success(userVOPage);
    }

    /**
     * 更新个人信息
     */
    @PostMapping("/update/my")
    public Result<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
                                        HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        List<String> tags = userUpdateMyRequest.getTags();
        if (tags != null) {
            user.setTags(GSON.toJson(tags));
        }
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        System.out.println(user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, HttpCodeEnum.OPERATION_ERROR);
        return Result.success(true);
    }

    /**
     * 更改密码
     */
    @PostMapping("/update/password")
    public Result<Boolean> updatePassword(@RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest) {
        if (userUpdatePasswordRequest == null) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR);
        }
        String oldPassword = userUpdatePasswordRequest.getOldPassword();
        String newPassword = userUpdatePasswordRequest.getNewPassword();
        if (StringUtils.isAnyBlank(oldPassword, newPassword)) {
            throw new BusinessException(HttpCodeEnum.PARAMS_ERROR);
        }
        boolean result = userService.updatePassword(oldPassword, newPassword);
        return Result.success(result);
    }

    /**
     * 获取个人信息
     */
    @GetMapping("/get/my")
    public Result<UserVO> getMyUser() {
        Long userId = StpUtil.getLoginIdAsLong();
        User loginUser = userService.getById(userId);
        return Result.success(userService.getUserVO(loginUser));
    }
}
