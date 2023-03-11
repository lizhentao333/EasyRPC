package cn.lizhentao.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @author lzt
 * @date 2023/3/11 15:27
 * @description:
 */
public interface ServiceDiscovery {
    /**
     * 根据指定服务名查找对应的提供商
     * @param serviceName
     * @return
     */
    InetSocketAddress lookupService(String serviceName);
}
