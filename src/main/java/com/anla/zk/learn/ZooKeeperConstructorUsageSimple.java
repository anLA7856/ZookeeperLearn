package com.anla.zk.learn;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * 创建基本Zookeeper连接，并自己为Watcher，从而连接成功调用process通知自己
 * 阻塞式等待。
 *
 * @author luoan
 * @version 1.0
 * @date 2019/4/28 17:37
 **/
public class ZooKeeperConstructorUsageSimple implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeperConstructorUsageSimple());
        System.out.println(zooKeeper.getState());
        connectedSemaphore.await();
        System.out.println("Zookeeper session established");
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event : " + watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
