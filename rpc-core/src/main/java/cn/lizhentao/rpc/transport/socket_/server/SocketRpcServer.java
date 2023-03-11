package cn.lizhentao.rpc.transport.socket_.server;

import cn.lizhentao.rpc.enumeration.RpcError;
import cn.lizhentao.rpc.exception.RpcException;
import cn.lizhentao.rpc.handler.RequestHandler;
import cn.lizhentao.rpc.provider.ServiceProvider;
import cn.lizhentao.rpc.provider.ServiceProviderImpl;
import cn.lizhentao.rpc.registry.NacosServiceRegistry;
import cn.lizhentao.rpc.serializer.CommonSerializer;
import cn.lizhentao.rpc.transport.RpcServer;
import cn.lizhentao.rpc.registry.ServiceRegistry;
import cn.lizhentao.rpc.factory.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author lzt
 * @date 2023/3/8 17:16
 * @description: 处理发送过来的rpc请求的服务端
 */
public class SocketRpcServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketRpcServer.class);
    private final ExecutorService threadPool;
    private final String host;
    private final int port;
    private CommonSerializer serializer;
    private final RequestHandler requestHandler = new RequestHandler();
    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    public SocketRpcServer(String host, int port) {
        this.host = host;
        this.port = port;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
    }

    @Override
    public <T> void publishService(T service, Class<T> serviceClass) {
        if(serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        // 像本地注册表中添加服务
        serviceProvider.addServiceProvider(service, serviceClass);
        // 向注册中心注册该服务
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        // 注册之后开始启动服务器
        start();
    }

    @Override
    public void start() {
        try(ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host, port));
            logger.info("服务器正在启动...");
            Socket socket;
            // 监听对应端口即可
            while ((socket = serverSocket.accept()) != null) {
                logger.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                // 每来到一个客户请求，就新开一个线程，扔到线程池中
                threadPool.execute(new SocketRequestHandlerThread(socket, requestHandler, serviceRegistry, serializer));
            }
            // 当服务端关闭后，线程池也要回收
            threadPool.shutdown();
        }catch (IOException e) {
            logger.error("服务器启动时有错误发生", e);
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
