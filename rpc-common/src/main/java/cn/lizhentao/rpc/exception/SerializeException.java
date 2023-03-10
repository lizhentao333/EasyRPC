package cn.lizhentao.rpc.exception;

/**
 * @author lzt
 * @date 2023/3/9 21:40
 * @description: 序列化异常
 */
public class SerializeException extends RuntimeException{

    public SerializeException(String msg) {
        super(msg);
    }
}
