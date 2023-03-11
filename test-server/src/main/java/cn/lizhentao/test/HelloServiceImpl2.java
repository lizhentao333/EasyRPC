package cn.lizhentao.test;

import cn.lizhentao.rpc.api.HelloObject;
import cn.lizhentao.rpc.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lzt
 * @date 2023/3/8 17:37
 * @description:
 */
public class HelloServiceImpl2 implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl2.class);
    @Override
    public String hello(HelloObject object) {
        logger.info("接收到消息：{}", object.getMessage());
        return "本次处理来自Socket服务";
    }
}
