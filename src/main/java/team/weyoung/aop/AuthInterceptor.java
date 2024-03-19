package team.weyoung.aop;

import team.weyoung.common.HttpCodeEnum;
import team.weyoung.exception.BusinessException;
import team.weyoung.model.entity.User;
import team.weyoung.annotation.AuthCheck;
import team.weyoung.model.enums.UserRoleEnum;
import team.weyoung.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * AuthInterceptor is an Aspect Oriented Programming (AOP) component that intercepts method calls
 * annotated with the AuthCheck annotation to perform authorization checks.
 * It ensures that the user making the request has the necessary role to access the method.
 * If the user does not have the necessary role, a BusinessException is thrown.
 *
 * @author <a href="https://github.com/Tunan81">图南</a>
 * @since 2023-12-18
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * This method intercepts method calls annotated with the AuthCheck annotation.
     * It retrieves the role required to access the method and the role of the user making the request.
     * If the user does not have the necessary role, a BusinessException is thrown.
     * If the user has the necessary role, the method call is allowed to proceed.
     *
     * @param joinPoint The ProceedingJoinPoint that represents the method call being intercepted.
     * @param authCheck The AuthCheck annotation applied to the method being intercepted.
     * @return The result of the method call if the user has the necessary role.
     * @throws Throwable If an error occurs during the method call or if the user does not have the necessary role.
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // Current logged in user
        User loginUser = userService.getLoginUser(request);
        // The user must have this role to pass
        if (StringUtils.isNotBlank(mustRole)) {
            UserRoleEnum mustUserRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
            if (mustUserRoleEnum == null) {
                throw new BusinessException(HttpCodeEnum.NO_AUTH_ERROR);
            }
            String userRole = loginUser.getUserRole();
            // If the user is banned, reject immediately
            if (UserRoleEnum.BAN.equals(mustUserRoleEnum)) {
                throw new BusinessException(HttpCodeEnum.NO_AUTH_ERROR);
            }
            // The user must have admin role
            if (UserRoleEnum.ADMIN.equals(mustUserRoleEnum)) {
                if (!mustRole.equals(userRole)) {
                    throw new BusinessException(HttpCodeEnum.NO_AUTH_ERROR);
                }
            }
        }
        // The user passed the role check, allow the method call to proceed
        return joinPoint.proceed();
    }
}