package cn.lizhentao.rpc.transport.socket_.client;

import cn.lizhentao.rpc.constant.ProtocolConstant;
import cn.lizhentao.rpc.enumeration.RpcError;
import cn.lizhentao.rpc.exception.RpcException;
import cn.lizhentao.rpc.registry.NacosServiceDiscovery;
import cn.lizhentao.rpc.registry.NacosServiceRegistry;
import cn.lizhentao.rpc.registry.ServiceDiscovery;
import cn.lizhentao.rpc.registry.ServiceRegistry;
import cn.lizhentao.rpc.serializer.CommonSerializer;
import cn.lizhentao.rpc.transport.socket_.util.ObjectReader;
import cn.lizhentao.rpc.transport.socket_.util.ObjectWriter;
import cn.lizhentao.rpc.transport.RpcClient;
import cn.lizhentao.rpc.entity.RpcRequest;
import cn.lizhentao.rpc.entity.RpcResponse;
import cn.lizhentao.rpc.util.RpcMessageChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author lzt
 * @date 2023/3/8 17:05
 * @description: 发送rpc请求的客户端
 */
public class SocketRpcClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketRpcClient.class);
    private final CommonSerializer serializer;
    private final ServiceDiscovery serviceDiscovery;

    public SocketRpcClient() {
        this(ProtocolConstant.DEFAULT_SERIALIZER);
    }

    public SocketRpcClient(Integer serializerCode) {
        this.serviceDiscovery = new NacosServiceDiscovery();
        this.serializer = CommonSerializer.getByCode(serializerCode);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        // 向注册中心查找对应的服务所在的ip和port
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        try (Socket socket = new Socket()) {
            // 对应服务提供商进行通信
            socket.connect(inetSocketAddress);

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            // 将请求进行编码，然后发送给服务端，
            ObjectWriter.writeObject(outputStream, rpcRequest, serializer);

            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
            // 检查
            RpcMessageChecker.check(rpcRequest, rpcResponse);

            return rpcResponse.getData();
        } catch (IOException e) {
            logger.error("sendRequest:调用时有错误发生：", e);
            return null;
        }
    }

}
