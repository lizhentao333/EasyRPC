package cn.lizhentao.rpc.socket_.client;

import cn.lizhentao.rpc.RpcClient;
import cn.lizhentao.rpc.entity.RpcRequest;
import cn.lizhentao.rpc.entity.RpcResponse;
import cn.lizhentao.rpc.enumeration.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author lzt
 * @date 2023/3/8 17:05
 * @description: 发送rpc请求的客户端
 */
public class SocketRpcClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketRpcClient.class);
    private final String host;
    private final int port;

    public SocketRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        try(Socket socket = new Socket(host, port)) {

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            // 这里注意处理类型转换，需要对返回结果进行处理
            RpcResponse resp = (RpcResponse)objectInputStream.readObject();
            if (resp.getStatusCode() == ResponseCode.SUCCESS.getCode()) {
                return resp.getData();
            }
            return resp.getMessage();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("sendRequest:调用时有错误发生：", e);
            return null;
        }
    }

}
