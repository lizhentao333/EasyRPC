package cn.lizhentao.rpc.socket_.server;

import cn.lizhentao.rpc.RequestHandler;
import cn.lizhentao.rpc.RpcServer;
import cn.lizhentao.rpc.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private final ExecutorService threadPool;

    private final RequestHandler requestHandler = new RequestHandler();
    private final ServiceRegistry serviceRegistry;

    public SocketRpcServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    @Override
    public void start(int port) {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("服务器正在启动...");
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                logger.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serviceRegistry));
            }
            threadPool.shutdown();
        }catch (IOException e) {
            logger.error("服务器启动时有错误发生", e);
        }
    }
}
