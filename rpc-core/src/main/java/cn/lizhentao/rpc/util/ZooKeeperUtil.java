package cn.lizhentao.rpc.util;

import cn.lizhentao.rpc.constant.ProtocolConstant;
import cn.lizhentao.rpc.serializer.CommonSerializer;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lzt
 * @date 2023/3/13 20:31
 * @description:
 */
public class ZooKeeperUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperUtil.class);
    private static final  CuratorFramework client;
    private static final CommonSerializer serializer;

    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();

    private static InetSocketAddress address;

    static {
        client = getCuratorClient();
        client.start();
        serializer = CommonSerializer.getByCode(ProtocolConstant.JSON_SERIALIZER);
    }

    /**
     * 用于创建一个client对象
     * @return
     */
    private static CuratorFramework getCuratorClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(ProtocolConstant.ZOOKEEPER_SLEEP_TIME,
                ProtocolConstant.ZOOKEEPER_RETRY_TIMES);

        return CuratorFrameworkFactory.builder()
                .connectString(ProtocolConstant.ZOOKEEPER_SERVER_ADDR)
                .retryPolicy(retryPolicy)
                .build();
    }

    /**
     * 向zk中存入一个持久性节点
     * @param path
     */
    public static void createPersistentNode(String path) {
        try {
            if (REGISTERED_PATH_SET.contains(path) || client.checkExists().forPath(path) != null) {
                logger.info("The node already exists. The node is:[{}]", path);
            } else {
                //eg: /easy-rpc/org.lizhentao.HelloService/127.0.0.1:9999
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                logger.info("The node was created successfully. The node is:[{}]", path);
            }
            REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            logger.error("create persistent node for path [{}] fail", path);
        }
    }

    /**
     * 获取指定服务下的所有提供商
     * @param serviceName
     * @return
     */
    public static List<String> getChildrenNodes(String serviceName) {
        if (SERVICE_ADDRESS_MAP.containsKey(serviceName)) {
            return SERVICE_ADDRESS_MAP.get(serviceName);
        }
        List<String> result = null;
        String servicePath = ProtocolConstant.ZOOKEEPER_NAMESPACE + "/" + serviceName;
        try {
            result = client.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(serviceName, result);
            registerWatcher(serviceName);
        } catch (Exception e) {
            logger.error("get children nodes for path [{}] fail", servicePath);
        }
        return result;
    }

    public static void clearRegistry() {
        REGISTERED_PATH_SET.stream().parallel().forEach(p -> {
            try {
                client.delete().forPath(p);
            } catch (Exception e) {
                logger.error("clear registry for path [{}] fail", p);
            }
        });
        logger.info("All registered services on the server are cleared:[{}]", REGISTERED_PATH_SET.toString());
    }

    private static void registerWatcher(String serviceName) throws Exception {
        String servicePath = ProtocolConstant.ZOOKEEPER_NAMESPACE + "/" + serviceName;

        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            List<String> serviceAddresses = curatorFramework.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(serviceName, serviceAddresses);

//            System.out.println(SERVICE_ADDRESS_MAP);
//            System.out.println(pathChildrenCacheEvent.toString());
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
        System.out.println("start listen....");
    }

    public static void deleteNode(String serviceName, InetSocketAddress address) {
        String path = changeToPath(serviceName, address);
        try {
            client.delete().forPath(path);
//            registerWatcher(serviceName);
        } catch (Exception e) {
            logger.error("delete node for path [{}] fail", path);
        }
    }

    public static String changeToPath(String serviceName, InetSocketAddress address) {
        return ProtocolConstant.ZOOKEEPER_NAMESPACE + "/" + serviceName + address.toString();
    }

}
