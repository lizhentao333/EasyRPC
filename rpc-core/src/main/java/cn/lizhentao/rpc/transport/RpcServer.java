package cn.lizhentao.rpc.transport;

/**
 * @author lzt
 * @date 2023/3/8 21:18
 * @description:
 */
public interface RpcServer {

    void start();

    <T> void publishService(T service, Class<T> serviceClass);
}
