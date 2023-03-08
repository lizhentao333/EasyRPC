package cn.lizhentao.test;

import cn.lizhentao.rpc.api.HelloService;
import cn.lizhentao.rpc.registry.DefaultServiceRegistry;
import cn.lizhentao.rpc.registry.ServiceRegistry;
import cn.lizhentao.rpc.server.RpcServer;

/**
 * @author lzt
 * @date 2023/3/8 17:40
 * @description:
 */
public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9000);
    }
}
