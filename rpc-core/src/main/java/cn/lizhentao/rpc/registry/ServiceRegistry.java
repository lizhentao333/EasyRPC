package cn.lizhentao.rpc.registry;

/**
 * @author lzt
 * @date 2023/3/8 18:55
 * @description: 服务注册表通用接口
 */
public interface ServiceRegistry {
    /**
     * 将一个服务注册进服务表
     * @param service
     * @param <T>
     */
    <T> void register(T service);

    /**
     * 根据服务名称获取服务实体
     * @param serviceName
     * @return 服务实体
     */
    Object getService(String serviceName);
}
