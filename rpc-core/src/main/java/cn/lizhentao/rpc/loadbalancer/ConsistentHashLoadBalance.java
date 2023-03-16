package cn.lizhentao.rpc.loadbalancer;

import cn.lizhentao.rpc.loadbalance.AbstractLoadBalance;
import cn.lizhentao.rpc.provider.ServiceProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author lzt
 * @date 2023/3/14 15:09
 * @description:
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance {
//    private HashStrategy hashStrategy = new JdkHashCodeStrategy();
    private static final Logger logger = LoggerFactory.getLogger(ConsistentHashLoadBalance.class);

    private final static int VIRTUAL_NODE_SIZE = 10;
    private final static String VIRTUAL_NODE_SUFFIX = "&&";
    @Override
    protected String doSelect(List<String> serviceAddresses, String serviceName) {
        try {
            ConsistentHashSelector consistentHashSelector = new ConsistentHashSelector(serviceAddresses, 160);
            return consistentHashSelector.select(serviceName);
        } catch (NoSuchAlgorithmException e) {
            logger.error("An encryption algorithm that does not exist is used: ", e);
            e.printStackTrace();
        }

        return null;
    }

    static class ConsistentHashSelector {
        private final TreeMap<Long, String> virtualInvokers;

        ConsistentHashSelector(List<String> invokers, int replicaNumber) throws NoSuchAlgorithmException {
            this.virtualInvokers = new TreeMap<>();

            for (String invoker : invokers) {
                for (int i = 0; i < replicaNumber / 4; i++) {
                    byte[] digest = md5(invoker + i);
                    for (int h = 0; h < 4; h++) {
                        long m = hash(digest, h);
                        virtualInvokers.put(m, invoker);
                    }
                }
            }
        }
        static byte[] md5(String key) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
            md.update(bytes);
            return md.digest();
        }
        static long hash(byte[] digest, int idx) {
            return ((long) (digest[3 + idx * 4] & 255) << 24 | (long) (digest[2 + idx * 4] & 255) << 16 | (long) (digest[1 + idx * 4] & 255) << 8 | (long) (digest[idx * 4] & 255)) & 4294967295L;
        }
        public String select(String rpcServiceName) throws NoSuchAlgorithmException {
            byte[] digest = md5(rpcServiceName);
            return selectForKey(hash(digest, 0));
        }

        public String selectForKey(long hashCode) {
            Map.Entry<Long, String> entry = virtualInvokers.tailMap(hashCode, true).firstEntry();

            if (entry == null) {
                entry = virtualInvokers.firstEntry();
            }
            return entry.getValue();
        }
    }
}
