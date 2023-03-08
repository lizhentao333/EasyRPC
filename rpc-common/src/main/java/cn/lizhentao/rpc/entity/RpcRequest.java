package cn.lizhentao.rpc.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lzt
 * @date 2023/3/8 16:50
 * @description: 用于发送rpc请求
 */
@Data
@Builder
public class RpcRequest implements Serializable {
    /**
     * 目标接口名
     */
    private String interfaceName;
    /**
     * 目标方法名
     */
    private String methodName;
    /**
     * 目标方法中的参数列表
     */
    private Object[] parameters;
    /**
     * 参数类型列表
     */
    private Class<?>[] paramTypes;
}
