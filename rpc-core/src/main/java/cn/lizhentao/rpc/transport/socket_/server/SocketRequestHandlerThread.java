package cn.lizhentao.rpc.transport.socket_.server;

import cn.lizhentao.rpc.handler.RequestHandler;
import cn.lizhentao.rpc.entity.RpcRequest;
import cn.lizhentao.rpc.entity.RpcResponse;
import cn.lizhentao.rpc.registry.ServiceRegistry;
import cn.lizhentao.rpc.serializer.CommonSerializer;
import cn.lizhentao.rpc.transport.socket_.util.ObjectReader;
import cn.lizhentao.rpc.transport.socket_.util.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * @author lzt
 * @date 2023/3/8 19:09
 * @description:
 */
public class SocketRequestHandlerThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(SocketRequestHandlerThread.class);

    private final Socket socket;
    private final RequestHandler requestHandler;
    private final ServiceRegistry serviceRegistry;
    private CommonSerializer serializer;

    public SocketRequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry, CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        // 收到客户端的请求后
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            // 解析请求报文
            RpcRequest request = (RpcRequest) ObjectReader.readObject(inputStream);
            // 在本地查找对应的请求方法，然后根据请求中的参数调用该方法，最终得到请求结果
            Object result = requestHandler.handle(request);
            // 将结果返回给客户端
            RpcResponse<Object> response = RpcResponse.success(result, request.getRequestId());
            // 将报文编码，然后发送给客户端
            ObjectWriter.writeObject(outputStream, response, serializer);

        }catch (IOException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
    }
}
