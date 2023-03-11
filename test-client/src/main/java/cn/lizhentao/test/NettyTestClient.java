package cn.lizhentao.test;

import cn.lizhentao.rpc.constant.ProtocolConstant;
import cn.lizhentao.rpc.serializer.ProtobufSerializer;
import cn.lizhentao.rpc.transport.RpcClient;
import cn.lizhentao.rpc.transport.RpcClientProxy;
import cn.lizhentao.rpc.api.HelloObject;
import cn.lizhentao.rpc.api.HelloService;
import cn.lizhentao.rpc.transport.netty.client.NettyClient;

/**
 * @author lzt
 * @date 2023/3/9 12:22
 * @description:
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient(ProtocolConstant.PROTOBUF_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
