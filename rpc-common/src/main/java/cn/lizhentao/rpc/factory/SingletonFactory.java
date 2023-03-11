package cn.lizhentao.rpc.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lzt
 * @date 2023/3/11 15:43
 * @description:
 */
public class SingletonFactory {
    private static Map<Class, Object> objectMap = new HashMap<>();

    public static <T> T getInstance(Class<T> clazz) {
        Object instance = objectMap.get(clazz);
        if (instance == null) {
            synchronized (clazz) {
                if (instance == null) {
                    try {
                        instance = clazz.newInstance();
                        objectMap.put(clazz, instance);
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return clazz.cast(instance);
    }
}
