package com.anla.zk.learn;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * @author luoan
 * @version 1.0
 * @date 2019/4/29 13:24
 **/
public class ZooKeeperCreateApiSync implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        ZooKeeper zookeeper = new ZooKeeper(ZooKeeperLearnConstant.CONNECT_STRING,
                5000,
                new ZooKeeperCreateApiSync());
        connectedSemaphore.await();
        String path1 = zookeeper.create("/zk-test-sync-ephemeral-",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        System.out.println("Success create znode: " + path1);

        String path2 = zookeeper.create("/zk-test-sync-ephemeral-",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("Success create znode: " + path2);
    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
