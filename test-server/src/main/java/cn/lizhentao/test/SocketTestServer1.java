package cn.lizhentao.test;

import cn.lizhentao.rpc.api.HelloService;
import cn.lizhentao.rpc.constant.ProtocolConstant;
import cn.lizhentao.rpc.transport.socket_.server.SocketRpcServer;

import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lzt
 * @date 2023/3/8 17:40
 * @description:
 */
public class SocketTestServer1 {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        SocketRpcServer socketServer = new SocketRpcServer("127.0.0.1", 9990, ProtocolConstant.KRYO_SERIALIZER);
        socketServer.publishService(helloService, HelloService.class);

    }
}
