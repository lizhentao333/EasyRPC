package cn.lizhentao.rpc.transport;

import cn.lizhentao.rpc.serializer.CommonSerializer;

/**
 * @author lzt
 * @date 2023/3/8 21:18
 * @description:
 */
public interface RpcServer {

    void start();

    <T> void publishService(T service, Class<T> serviceClass);
}
