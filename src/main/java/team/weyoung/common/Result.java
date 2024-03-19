package team.weyoung.common;


import lombok.Data;

/**
 * @author Tunan
 * @since 2023/6/20 14:55
 * @version 1.0
 * 统一返回结果集
 */

@Data
public class Result<T> {

    /**
     * 请求状态
     */
    private Boolean success;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态信息
     */
    private String message;

    /**
     * 数据信息
     */
    private T data;

    /**
     * 私有构造
     *
     * @param success 请求状态
     * @param code    状态码
     * @param message 状态信息
     * @param data    数据
     * @param <T>     泛型数据
     * @return 结果集处理器
     */
    private static <T> Result<T> response(Boolean success, Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.setSuccess(success);
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 请求成功返回（一）
     *
     * @param code    状态码
     * @param message 状态信息
     * @param data    数据
     * @param <T>     泛型数据
     * @return 结果集处理器
     */
    public static <T> Result<T> success(Integer code, String message, T data) {
        return response(true, code, message, data);
    }

    /**
     * 请求成功返回（二）
     *
     * @param message 状态信息
     * @param data    数据
     * @param <T>     泛型数据
     * @return 结果集处理器
     */
    public static <T> Result<T> success(String message, T data) {
        return response(true, HttpCodeEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 请求成功返回（三）
     *
     * @param message 状态信息
     * @param <T>     泛型数据
     * @return 结果集处理器
     */
    public static <T> Result<T> success(String message) {
        return response(true, HttpCodeEnum.SUCCESS.getCode(), message, null);
    }

    /**
     * 请求成功返回（四）
     *
     * @param httpCodeEnum 状态枚举
     * @param <T>          泛型数据
     * @return 结果集处理器
     */
    public static <T> Result<T> success(HttpCodeEnum httpCodeEnum) {
        return response(true, httpCodeEnum.getCode(), httpCodeEnum.getMessage(), null);
    }

    /**
     * 请求成功返回（五）
     *
     * @param httpCodeEnum 状态枚举
     * @param data         数据
     * @param <T>          泛型数据
     * @return 结果集处理器
     */
    public static <T> Result<T> success(HttpCodeEnum httpCodeEnum, T data) {
        return response(true, httpCodeEnum.getCode(), httpCodeEnum.getMessage(), data);
    }

    /**
     * 请求成功返回（六）
     *
     * @param data 数据
     * @param <T>  泛型数据
     * @return 结果集处理器
     */
    public static <T> Result<T> success(T data) {
        return response(true, HttpCodeEnum.SUCCESS.getCode(), HttpCodeEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 请求失败返回（一）
     *
     * @param code    状态码
     * @param message 状态信息
     * @param data    数据
     * @param <T>     泛型数据
     * @return 结果集处理器
     */
    public static <T> Result<T> fail(Integer code, String message, T data) {
        return response(false, code, message, data);
    }

    /**
     * 请求失败返回（二）
     *
     * @param message 状态信息
     * @param data    数据
     * @param <T>     泛型数据
     * @return 结果集处理器
     */
    public static <T> Result<T> fail(String message, T data) {
        return response(false, HttpCodeEnum.OPERATION_ERROR.getCode(), message, data);
    }

    /**
     * 请求失败返回（三）
     *
     * @param message 状态信息
     * @param <T>     泛型数据
     * @return 结果集处理器
     */
    public static <T> Result<T> fail(String message) {
        return response(false, HttpCodeEnum.OPERATION_ERROR.getCode(), message, null);
    }

    /**
     * 请求失败返回（四）
     *
     * @param httpCodeEnum 状态枚举
     * @param <T>          泛型数据
     * @return 结果集处理器
     */
    public static <T> Result<T> fail(HttpCodeEnum httpCodeEnum) {
        return response(false, httpCodeEnum.getCode(), httpCodeEnum.getMessage(), null);
    }

    /**
     * 请求失败返回（五）
     *
     * @param code    状态码
     * @param message 状态信息
     * @param <T>     泛型数据
     * @return 结果集处理器
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return response(false, code, message, null);
    }

}
