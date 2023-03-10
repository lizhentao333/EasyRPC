package cn.lizhentao.rpc.transport.socket_.util;

import cn.lizhentao.rpc.entity.RpcRequest;
import cn.lizhentao.rpc.entity.RpcResponse;
import cn.lizhentao.rpc.enumeration.PackageType;
import cn.lizhentao.rpc.enumeration.RpcError;
import cn.lizhentao.rpc.exception.RpcException;
import cn.lizhentao.rpc.serializer.CommonSerializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author lzt
 * @date 2023/3/10 17:09
 * @description:
 */
public class ObjectWriter {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;
    public static void writeObject(OutputStream outputStream, Object obj, CommonSerializer serializer) throws IOException {
        // 写入魔数
        outputStream.write(intToBytes(MAGIC_NUMBER));
        // 写入数据包类型
        if(obj instanceof RpcRequest) {
            outputStream.write(intToBytes(PackageType.REQUEST_PACK.getCode()));
        }else if(obj instanceof RpcResponse) {
            outputStream.write(intToBytes(PackageType.RESPONSE_PACK.getCode()));
        }else {
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
        // 写入序列化类型
        outputStream.write(intToBytes(serializer.getCode()));
        // 将真正的数据内容写入
        byte[] bytes = serializer.serialize(obj);
        outputStream.write(intToBytes(bytes.length));
        outputStream.write(bytes);
        outputStream.flush();
    }

    // 大端写入
    private static byte[] intToBytes(int value) {
        byte[] desc = new byte[4];
        for (int i = 0; i < 4; i++) {
            desc[i] = (byte) ((value >>> (24 - i * 8)) & 0xff);
        }
        return desc;
    }
}
