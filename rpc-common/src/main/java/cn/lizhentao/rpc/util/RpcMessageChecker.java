package cn.lizhentao.rpc.util;

import cn.lizhentao.rpc.entity.RpcRequest;
import cn.lizhentao.rpc.entity.RpcResponse;
import cn.lizhentao.rpc.enumeration.ResponseCode;
import cn.lizhentao.rpc.enumeration.RpcError;
import cn.lizhentao.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lzt
 * @date 2023/3/10 17:39
 * @description:
 */
public class RpcMessageChecker {
    public static final String INTERFACE_NAME = "interfaceName";
    private static final Logger logger = LoggerFactory.getLogger(RpcMessageChecker.class);


    public static void check(RpcRequest rpcRequest, RpcResponse rpcResponse) {
        if (rpcResponse == null) {
            logger.error("调用服务失败,serviceName:{}", rpcRequest.getInterfaceName());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcError.RESPONSE_NOT_MATCH, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (rpcResponse.getStatusCode() == null || !rpcResponse.getStatusCode().equals(ResponseCode.SUCCESS.getCode())) {
            logger.error("调用服务失败,serviceName:{},RpcResponse:{}", rpcRequest.getInterfaceName(), rpcResponse);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
