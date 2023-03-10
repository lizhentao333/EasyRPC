package cn.lizhentao.rpc.codec;

import cn.lizhentao.rpc.entity.RpcRequest;
import cn.lizhentao.rpc.enumeration.PackageType;
import cn.lizhentao.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author lzt
 * @date 2023/3/8 22:08
 * @description: 用于编码
 */
public class CommonEncoder extends MessageToByteEncoder {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        // 写入魔数
        byteBuf.writeInt(MAGIC_NUMBER);
        // 写数据包类型
        byteBuf.writeInt(o instanceof RpcRequest ? PackageType.REQUEST_PACK.getCode() : PackageType.RESPONSE_PACK.getCode());

        // 写序列化类型
        byteBuf.writeInt(serializer.getCode());
        byte[] bytes = serializer.serialize(o);
        // 写数据长度
        byteBuf.writeInt(bytes.length);
        // 写数据
        byteBuf.writeBytes(bytes);

    }
}
