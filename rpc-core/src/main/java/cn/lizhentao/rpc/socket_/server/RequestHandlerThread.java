package cn.lizhentao.rpc.socket_.server;

import cn.lizhentao.rpc.RequestHandler;
import cn.lizhentao.rpc.entity.RpcRequest;
import cn.lizhentao.rpc.entity.RpcResponse;
import cn.lizhentao.rpc.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author lzt
 * @date 2023/3/8 19:09
 * @description:
 */
public class RequestHandlerThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);

    private final Socket socket;
    private final RequestHandler requestHandler;
    private final ServiceRegistry serviceRegistry;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try(ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            // 反序列化
            RpcRequest request = (RpcRequest)inputStream.readObject();
            // 此时由requestHandler负责找执行对应方法
            String interfaceName = request.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = requestHandler.handle(request, service);

            // 将结果返回给客户端
            outputStream.writeObject(RpcResponse.success(result));
            outputStream.flush();


        }catch (IOException | ClassNotFoundException  e) {
            logger.error("调用或发送时有错误发生：", e);
        }
    }
}
