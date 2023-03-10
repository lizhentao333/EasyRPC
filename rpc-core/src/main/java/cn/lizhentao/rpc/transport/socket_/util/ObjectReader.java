package cn.lizhentao.rpc.transport.socket_.util;

import cn.lizhentao.rpc.constant.ProtocolConstant;
import cn.lizhentao.rpc.entity.RpcRequest;
import cn.lizhentao.rpc.entity.RpcResponse;
import cn.lizhentao.rpc.enumeration.PackageType;
import cn.lizhentao.rpc.enumeration.RpcError;
import cn.lizhentao.rpc.exception.RpcException;
import cn.lizhentao.rpc.serializer.CommonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lzt
 * @date 2023/3/10 17:25
 * @description:
 */
public class ObjectReader {

    private static final Logger logger = LoggerFactory.getLogger(ObjectReader.class);


    public static Object readObject(InputStream in) throws IOException {
        byte[] numberBytes = new byte[4];
        in.read(numberBytes);
        int magic = bytesToInt(numberBytes);
        if (magic != ProtocolConstant.MAGIC_NUMBER) {
            logger.error("不识别的协议包: {}", magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }

        in.read(numberBytes);
        int packageCode = bytesToInt(numberBytes);
        Class<?> packageClass;
        if (packageCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if (packageCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            logger.error("不识别的数据包: {}", packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
        in.read(numberBytes);
        int serializerCode = bytesToInt(numberBytes);
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if (serializer == null) {
            logger.error("不识别的反序列化器: {}", serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }

        in.read(numberBytes);
        int length = bytesToInt(numberBytes);
        byte[] bytes = new byte[length];
        in.read(bytes);
        return serializer.deserialize(bytes, packageClass);

    }

    private static int bytesToInt(byte[] src) {
        int value = 0;
        for(int i = 0; i < 4; i++) {
            value |= ((src[i] & 0xff) << (24 - i * 8));
        }
        return value;
    }
}
