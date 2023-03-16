package cn.lizhentao.rpc.constant;

/**
 * @author lzt
 * @date 2023/3/10 20:42
 * @description:
 */
public class ProtocolConstant {
    /**
     * 协议头：魔数
     */
    public static final int MAGIC_NUMBER = 0xCAFEBABE;
    /**
     * 服务注册中心地址
     */
    public static final String NACOS_SERVER_ADDR = "101.43.133.157:8848";
    public static final String ZOOKEEPER_SERVER_ADDR = "101.43.133.157:2181";
    public static final Integer ZOOKEEPER_SLEEP_TIME = 3000;
    public static final Integer ZOOKEEPER_RETRY_TIMES = 10;
    public static final String ZOOKEEPER_NAMESPACE = "/easy-rpc";
    /**
     * kryo序列化方法
     */
    public static final Integer KRYO_SERIALIZER = 0;
    /**
     * JSON序列化方法
     */
    public static final Integer JSON_SERIALIZER = 1;
    /**
     * hessian序列化方法
     */
    public static final Integer HESSIAN_SERIALIZER = 2;
    /**
     * protobuf序列化方法
     */
    public static final Integer PROTOBUF_SERIALIZER = 3;
    /**
     * 默认序列化方法
     */
    public static final int DEFAULT_SERIALIZER = KRYO_SERIALIZER;
}
