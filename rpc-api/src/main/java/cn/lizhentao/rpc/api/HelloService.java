package cn.lizhentao.rpc.api;

/**
 * @author lzt
 * @date 2023/3/8 16:48
 * @description: rpc通信调用的接口
 */
public interface HelloService {
    String hello(HelloObject object);
}
