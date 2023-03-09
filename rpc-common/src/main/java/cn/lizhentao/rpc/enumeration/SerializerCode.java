package cn.lizhentao.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lzt
 * @date 2023/3/8 21:07
 * @description:
 */
@Getter
@AllArgsConstructor
public enum SerializerCode {
    KRYO(0),
    JSON(1);
    private final int code;
}
