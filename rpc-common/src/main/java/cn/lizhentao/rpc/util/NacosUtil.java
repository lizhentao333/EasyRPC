package cn.lizhentao.rpc.util;

import cn.lizhentao.rpc.constant.ProtocolConstant;
import cn.lizhentao.rpc.enumeration.RpcError;
import cn.lizhentao.rpc.exception.RpcException;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author lzt
 * @date 2023/3/11 15:18
 * @description:
 */
public class NacosUtil {
    private static final Logger logger = LoggerFactory.getLogger(NacosUtil.class);

    /**
     * 获取注册服务
     * @return
     */
    public static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(ProtocolConstant.SERVER_ADDR);
        } catch (NacosException e) {
            logger.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    /**
     * 向nacos中注册服务
     * @param namingService
     * @param serviceName
     * @param address
     * @throws NacosException
     */
    public static void registerService(NamingService namingService, String serviceName, InetSocketAddress address) throws NacosException {
        namingService.registerInstance(serviceName, address.getHostName(), address.getPort());
    }

    /**
     * 从nacos获取指定服务的所有提供者
     * @param namingService
     * @param serviceName
     * @return
     * @throws NacosException
     */
    public static List<Instance> getAllInstance(NamingService namingService, String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }
}
