package com.anla.zk.learn;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * 用异步接口创建节点
 * @author luoan
 * @version 1.0
 * @date 2019/4/29 12:54
 **/
public class ZooKeeperCreateApiAsync implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{

        ZooKeeper zookeeper = new ZooKeeper(ZooKeeperLearnConstant.CONNECT_STRING,
                5000,
                new ZooKeeperCreateApiAsync());
        connectedSemaphore.await();

        zookeeper.create("/zk-test-ephemeral-", "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
                new IStringCallback(), "I am context.");

        zookeeper.create("/zk-test-ephemeral-", "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
                new IStringCallback(), "I am context.");

        zookeeper.create("/zk-test-ephemeral-", "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,
                new IStringCallback(), "I am context.");
        Thread.sleep( Integer.MAX_VALUE );
    }

    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            connectedSemaphore.countDown();
        }
    }


}
class IStringCallback implements AsyncCallback.StringCallback{

    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("Create path result : [" + rc + ", " + path + ", " + ctx + ", real path name: " + name);
    }
}
