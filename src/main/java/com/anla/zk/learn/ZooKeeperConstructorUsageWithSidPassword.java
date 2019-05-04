package com.anla.zk.learn;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * 使用sessionId和sessionPassword来恢复会话。
 * @author luoan
 * @version 1.0
 * @date 2019/4/28 17:54
 **/
public class ZooKeeperConstructorUsageWithSidPassword implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeperConstructorUsageWithSidPassword());
        connectedSemaphore.await();
        long sessionId = zooKeeper.getSessionId();
        byte[] password = zooKeeper.getSessionPasswd();

        // 使用错误串
        zooKeeper = new ZooKeeper(ZooKeeperLearnConstant.CONNECT_STRING, 5000, new ZooKeeperConstructorUsageWithSidPassword(), 11, "test".getBytes());

        // 使用正确串
        zooKeeper = new ZooKeeper(ZooKeeperLearnConstant.CONNECT_STRING, 5000, new ZooKeeperConstructorUsageWithSidPassword(), sessionId,password);

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event : " + watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
