package cn.lizhentao.rpc.exception;

import cn.lizhentao.rpc.enumeration.RpcError;

/**
 * @author lzt
 * @date 2023/3/8 18:45
 * @description: rpc调用异常
 */
public class RpcException extends RuntimeException {
    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + ": " + detail);
    }
    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
    public RpcException(RpcError error) {
        super(error.getMessage());
    }
}
