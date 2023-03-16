package zookeeper;

import cn.lizhentao.rpc.util.ZooKeeperUtil;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author lzt
 * @date 2023/3/13 21:48
 * @description:
 */
public class ZooKeeperTest {
    @Test
    public void testRegister() {
//        try {
//            ZooKeeperUtil.registerWatcher("org.lizhentao.test");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        InetSocketAddress address = new InetSocketAddress("192.168.1.1", 1215);
        ZooKeeperUtil.createPersistentNode(ZooKeeperUtil.changeToPath("org.lizhentao.test", address));
        while (true);
        //        ZooKeeperUtil.registerService("org.lizhentao.test", address);
    }
    @Test
    public void testGET() throws InterruptedException {

        ZooKeeperUtil.getChildrenNodes("org.lizhentao.test").forEach(System.out::println);
        Thread.sleep(5000);
        InetSocketAddress address = new InetSocketAddress("192.168.1.1", 1215);
        ZooKeeperUtil.deleteNode("org.lizhentao.test", address);
        while (true);
    }

    @Test
    public void testDel(){
        InetSocketAddress address = new InetSocketAddress("192.168.1.1", 1216);
        ZooKeeperUtil.deleteNode("org.lizhentao.test", address);
    }
}
