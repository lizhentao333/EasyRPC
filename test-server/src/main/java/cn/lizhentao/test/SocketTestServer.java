package cn.lizhentao.test;

import cn.lizhentao.rpc.api.HelloService;
import cn.lizhentao.rpc.serializer.HessianSerializer;
import cn.lizhentao.rpc.transport.socket_.server.SocketRpcServer;

/**
 * @author lzt
 * @date 2023/3/8 17:40
 * @description:
 */
public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        SocketRpcServer socketServer = new SocketRpcServer("127.0.0.1", 9998);
        socketServer.setSerializer(new HessianSerializer());
        socketServer.publishService(helloService, HelloService.class);
    }
}
