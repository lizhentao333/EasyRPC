package cn.lizhentao.rpc.provider;

/**
 * @author lzt
 * @date 2023/3/10 15:36
 * @description:
 */
public interface ServiceProvider {
    <T> void addServiceProvider(T service, Class<T> serviceClass);

    Object getServiceProvider(String serviceName);
}
