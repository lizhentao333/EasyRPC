package cn.lizhentao.rpc.loadbalance;

import java.util.List;

/**
 * @author lzt
 * @date 2023/3/14 14:49
 * @description:
 */
public interface LoadBalance {
    /**
     * 这种负载均衡都是针对客户端实现的，即在每个客户端自身实现，并不是针对全局，
     * 但是如果是随机负载均衡，那么每个客户端内部随机，我们可以近似认为是全局随机
     * @param serviceAddresses
     * @param serviceName
     * @return
     */
    String selectServiceAddress(List<String> serviceAddresses, String serviceName);
}
