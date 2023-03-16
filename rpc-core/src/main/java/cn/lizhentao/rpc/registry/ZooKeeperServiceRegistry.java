package cn.lizhentao.rpc.registry;

import cn.lizhentao.rpc.util.ZooKeeperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author lzt
 * @date 2023/3/13 20:15
 * @description:
 */
public class ZooKeeperServiceRegistry implements ServiceRegistry{
    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperServiceRegistry.class);
    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        //        ZooKeeperUtil.registerService(serviceName, inetSocketAddress);
        String path = ZooKeeperUtil.changeToPath(serviceName, inetSocketAddress);
        ZooKeeperUtil.createPersistentNode(path);
    }
}
