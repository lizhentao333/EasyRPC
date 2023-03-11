package cn.lizhentao.test;

import cn.lizhentao.rpc.api.HelloService;
import cn.lizhentao.rpc.constant.ProtocolConstant;
import cn.lizhentao.rpc.serializer.ProtobufSerializer;
import cn.lizhentao.rpc.transport.netty.server.NettyServer;

/**
 * @author lzt
 * @date 2023/3/9 12:27
 * @description:
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer server = new NettyServer("127.0.0.1", 9999, ProtocolConstant.PROTOBUF_SERIALIZER);
        server.publishService(helloService, HelloService.class);
    }
}
