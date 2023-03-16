package cn.lizhentao.test;

import cn.lizhentao.rpc.api.HelloObject;
import cn.lizhentao.rpc.api.HelloService;
import cn.lizhentao.rpc.constant.ProtocolConstant;
import cn.lizhentao.rpc.transport.RpcClientProxy;
import cn.lizhentao.rpc.transport.socket_.client.SocketRpcClient;

/**
 * @author lzt
 * @date 2023/3/8 17:34
 * @description:
 */
public class SocketTestClient {
    public static void main(String[] args) throws InterruptedException {
        SocketRpcClient client = new SocketRpcClient(ProtocolConstant.KRYO_SERIALIZER);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        for (int i = 0; i < 100; i++) {
            String res = helloService.hello(object);
            System.out.println(res);
        }
        Thread.sleep(100000);
    }
}
