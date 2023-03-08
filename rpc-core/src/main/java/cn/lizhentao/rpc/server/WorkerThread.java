package cn.lizhentao.rpc.server;

import cn.lizhentao.rpc.entity.RpcRequest;
import cn.lizhentao.rpc.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author lzt
 * @date 2023/3/8 17:25
 * @description: 处理客户端请求的实际线程
 */
public class WorkerThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(WorkerThread.class);

    private Socket socket;
    private Object service;

    public WorkerThread(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run() {
        try(ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            // 反序列化
            RpcRequest request = (RpcRequest)inputStream.readObject();
            // 获取反射需要用的方法名以及参数类型
            Method method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
            // 反射执行
            Object result = method.invoke(service, request.getParameters());
            // 将结果返回给客户端
            outputStream.writeObject(RpcResponse.success(result));
            outputStream.flush();


        }catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InvocationTargetException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
    }
}
