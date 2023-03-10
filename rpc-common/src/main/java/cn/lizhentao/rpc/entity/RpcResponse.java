package cn.lizhentao.rpc.entity;

import cn.lizhentao.rpc.enumeration.ResponseCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.io.Serializable;

/**
 * @author lzt
 * @date 2023/3/8 16:53
 * @description: 用于返回请求结果
 */
@Data
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {
    /**
     * 响应对应的请求号
     */
    private String requestId;
    /**
     * 返回结果的状态码
     */
    private Integer statusCode;
    /**
     * 返回状态的附加信息
     */
    private String message;
    /**
     * 返回结果的数据体
     */
    private T data;

    /**
     * 用于成功情况下的返回结果封装
     * @param data
     * @return
     * @param <T>
     */
    public static <T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    /**
     * 用于失败情况下的返回结果封装
     * @param code
     * @return
     * @param <T>
     */
    public static <T> RpcResponse<T> fail(ResponseCode code, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}
