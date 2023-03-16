package cn.lizhentao.rpc.loadbalance;

import java.util.List;

/**
 * @author lzt
 * @date 2023/3/14 14:55
 * @description:
 */
public abstract class AbstractLoadBalance implements LoadBalance{
    @Override
    public String selectServiceAddress(List<String> serviceAddresses, String serviceName) {
        if (serviceAddresses == null || serviceAddresses.isEmpty()) {

            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return doSelect(serviceAddresses, serviceName);
    }
    protected abstract String doSelect(List<String> serviceAddresses, String serviceName);

}
