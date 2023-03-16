package cn.lizhentao.rpc.registry;

import cn.lizhentao.rpc.enumeration.RpcError;
import cn.lizhentao.rpc.exception.RpcException;
import cn.lizhentao.rpc.factory.SingletonFactory;
import cn.lizhentao.rpc.loadbalance.LoadBalance;
import cn.lizhentao.rpc.loadbalancer.ConsistentHashLoadBalance;
import cn.lizhentao.rpc.loadbalancer.RandomLoadBalance;
import cn.lizhentao.rpc.util.NacosUtil;
import cn.lizhentao.rpc.util.ZooKeeperUtil;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author lzt
 * @date 2023/3/11 15:27
 * @description:
 */
public class ZookeerperServiceDiscovery implements ServiceDiscovery{

    private static final Logger logger = LoggerFactory.getLogger(ZookeerperServiceDiscovery.class);
    private final LoadBalance loadBalance;

    public ZookeerperServiceDiscovery() {
        this.loadBalance = SingletonFactory.getInstance(RandomLoadBalance.class);
        System.out.println(loadBalance);
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        List<String> serviceUrlList = ZooKeeperUtil.getChildrenNodes(serviceName);
        if (serviceUrlList == null || serviceUrlList.size() == 0) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND, serviceName);
        }
        // load balancing
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, serviceName);
//        String targetServiceUrl = serviceUrlList.get(0);
        logger.info("Successfully found the service address:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }

}
