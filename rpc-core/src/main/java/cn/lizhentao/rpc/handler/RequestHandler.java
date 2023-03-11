package cn.lizhentao.rpc.handler;

import cn.lizhentao.rpc.entity.RpcRequest;
import cn.lizhentao.rpc.entity.RpcResponse;
import cn.lizhentao.rpc.enumeration.ResponseCode;
import cn.lizhentao.rpc.provider.ServiceProvider;
import cn.lizhentao.rpc.provider.ServiceProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author lzt
 * @date 2023/3/8 19:16
 * @description:
 */
public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final ServiceProvider serviceProvider;
    static {
        serviceProvider = new ServiceProviderImpl();
    }
    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest, service);
    }
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result = null;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            logger.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND, rpcRequest.getRequestId());
        }
        return result;
    }
}
