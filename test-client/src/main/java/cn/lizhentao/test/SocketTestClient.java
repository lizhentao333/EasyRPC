package cn.lizhentao.test;

import cn.lizhentao.rpc.api.HelloObject;
import cn.lizhentao.rpc.api.HelloService;
import cn.lizhentao.rpc.RpcClientProxy;
import cn.lizhentao.rpc.socket_.client.SocketRpcClient;

/**
 * @author lzt
 * @date 2023/3/8 17:34
 * @description:
 */
public class SocketTestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy(new SocketRpcClient("127.0.0.1", 9000));
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
