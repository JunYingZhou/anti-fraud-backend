package team.weyoung.exception;

import team.weyoung.common.HttpCodeEnum;


public class ThrowUtils {

    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    public static void throwIf(boolean condition, HttpCodeEnum httpCodeEnum) {
        throwIf(condition, new BusinessException(httpCodeEnum));
    }

    public static void throwIf(boolean condition, HttpCodeEnum httpCodeEnum, String message) {
        throwIf(condition, new BusinessException(httpCodeEnum, message));
    }
}