package cn.lizhentao.rpc.hook;

import cn.lizhentao.rpc.factory.ThreadPoolFactory;
import cn.lizhentao.rpc.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * @author lzt
 * @date 2023/3/11 16:01
 * @description:
 */
public class ShutdownHook {
    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    private final ExecutorService threadPool = ThreadPoolFactory.createDefaultThreadPool("shutdown-hook");

    private volatile static ShutdownHook shutdownHook = null;

    private ShutdownHook() {}

    public static ShutdownHook getShutdownHook() {
        if (shutdownHook == null) {
            synchronized (ShutdownHook.class) {
                if (shutdownHook == null) {
                    shutdownHook = new ShutdownHook();
                }
            }
        }
        return shutdownHook;
    }
    public void addClearAllHook() {
        logger.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            NacosUtil.clearRegistry();
            threadPool.shutdown();
        }));
    }

}
