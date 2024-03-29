package cn.lizhentao.test;

import cn.lizhentao.rpc.api.HelloService;
import cn.lizhentao.rpc.constant.ProtocolConstant;
import cn.lizhentao.rpc.transport.socket_.server.SocketRpcServer;

/**
 * @author lzt
 * @date 2023/3/8 17:40
 * @description:
 */
public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl2();
        SocketRpcServer socketServer = new SocketRpcServer("127.0.0.1", 9998, ProtocolConstant.KRYO_SERIALIZER);
        socketServer.publishService(helloService, HelloService.class);
    }
}
