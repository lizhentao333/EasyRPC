package cn.lizhentao.test;

import cn.lizhentao.rpc.RpcClient;
import cn.lizhentao.rpc.RpcClientProxy;
import cn.lizhentao.rpc.api.HelloObject;
import cn.lizhentao.rpc.api.HelloService;
import cn.lizhentao.rpc.nety.client.NettyClient;

/**
 * @author lzt
 * @date 2023/3/9 12:22
 * @description:
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
