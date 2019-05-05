package com.anla.zk.learn.use;

import com.anla.zk.learn.ZooKeeperLearnConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

/**
 * 分布式计数器
 * @author luoan
 * @version 1.0
 * @date 2019/5/5 10:51
 **/
public class RecipesDistAtomicInt {
    static String path = "/curator_recipes_distatomicint_path";
    static CuratorFramework client = CuratorFrameworkFactory
            .builder()
            .connectString(ZooKeeperLearnConstant.CONNECT_STRING)
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
    public static void main(String[] args) throws Exception {
        client.start();
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(client, path, new RetryNTimes(3, 1000));
        AtomicValue<Integer> atomicValue = atomicInteger.add(8);
        System.out.println("result : " + atomicValue.succeeded());
    }
}
