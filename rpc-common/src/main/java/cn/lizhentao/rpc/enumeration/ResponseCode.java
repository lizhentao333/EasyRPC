package cn.lizhentao.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lzt
 * @date 2023/3/8 16:59
 * @description: 请求结果返回码
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {
    SUCCESS(200, "调用方法成功"),
    FAIL(500, "调用方法失败"),
    METHOD_NOT_FOUND(501, "未找到指定方法"),
    CLASS_NOT_FOUND(502, "未找到指定类");

    private final int code;
    private final String message;
}
