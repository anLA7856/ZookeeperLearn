package com.anla.zk.learn.use;

import com.anla.zk.learn.ZooKeeperLearnConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author luoan
 * @version 1.0
 * @date 2019/5/4 18:38
 **/
public class PathChildrenCacheSample {
    static String path = "/zk-book";
    static CuratorFramework client = CuratorFrameworkFactory
            .builder()
            .connectString(ZooKeeperLearnConstant.CONNECT_STRING)
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args){

    }
}
