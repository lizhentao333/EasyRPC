package cn.lizhentao.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @author lzt
 * @date 2023/3/8 18:55
 * @description: 服务注册中心通用接口
 */
public interface ServiceRegistry {
    /**
     * 将一个服务注册进服务中心
     * @param serviceName
     * @param inetSocketAddress
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

}
