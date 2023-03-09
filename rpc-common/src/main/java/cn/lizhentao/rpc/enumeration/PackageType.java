package cn.lizhentao.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lzt
 * @date 2023/3/8 21:05
 * @description:
 */
@Getter
@AllArgsConstructor
public enum PackageType {
    REQUEST_PACK(0),
    RESPONSE_PACK(1);
    private final int code;
}
