package com.anla.zk.learn.use;

import com.anla.zk.learn.ZooKeeperLearnConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * 使用PathChildrenCache监听子节点变化
 * @author luoan
 * @version 1.0
 * @date 2019/5/4 18:38
 **/
public class PathChildrenCacheSample {
    static String path = "/zk-book1";
    static CuratorFramework client = CuratorFrameworkFactory
            .builder()
            .connectString(ZooKeeperLearnConstant.CONNECT_STRING)
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args) throws Exception {
        client.start();
        PathChildrenCache childrenCache = new PathChildrenCache(client, path, true);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                switch (pathChildrenCacheEvent.getType()) {
                    case CHILD_ADDED:
                        System.out.println("Child add, " + pathChildrenCacheEvent.getData());
                        break;
                    case CHILD_UPDATED:
                        System.out.println("Child update, " + pathChildrenCacheEvent.getData());
                        break;
                    case CHILD_REMOVED:
                        System.out.println("Child remove, " + pathChildrenCacheEvent.getData());
                        break;
                    default:
                        break;
                }
            }
        });
        client.create().withMode(CreateMode.PERSISTENT).forPath(path);
        Thread.sleep(1000);
        client.create().withMode(CreateMode.PERSISTENT).forPath(path+"/c1");
        Thread.sleep(1000);
        client.delete().forPath(path+"/c1");
        Thread.sleep(1000);
        client.delete().forPath(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
