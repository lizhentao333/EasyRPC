package cn.lizhentao.rpc.transport;

import cn.lizhentao.rpc.entity.RpcRequest;

/**
 * @author lzt
 * @date 2023/3/8 21:13
 * @description: 客户端通用接口
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);

}
