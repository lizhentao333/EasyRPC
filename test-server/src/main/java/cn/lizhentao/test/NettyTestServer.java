package cn.lizhentao.test;

import cn.lizhentao.rpc.api.HelloService;
import cn.lizhentao.rpc.nety.server.NettyServer;
import cn.lizhentao.rpc.registry.DefaultServiceRegistry;
import cn.lizhentao.rpc.registry.ServiceRegistry;

/**
 * @author lzt
 * @date 2023/3/9 12:27
 * @description:
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9999);
    }
}
