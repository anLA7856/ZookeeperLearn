package com.anla.zk.learn.use;

import com.anla.zk.learn.ZooKeeperLearnConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 分布式Barrier
 * @author luoan
 * @version 1.0
 * @date 2019/5/5 11:10
 **/
public class RecipesBarrier {
    static String barrierPath = "/curator_recipes_barrier_path";
    static DistributedBarrier barrier;

    public static void main(String[] args) throws Exception {
        for(int i = 0; i < 5; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CuratorFramework client = CuratorFrameworkFactory
                                .builder()
                                .connectString(ZooKeeperLearnConstant.CONNECT_STRING)
                                .sessionTimeoutMs(5000)
                                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                                .build();
                        client.start();
                        barrier = new DistributedBarrier(client, barrierPath);
                        System.out.println(Thread.currentThread().getName() + "号barrier 设置");
                        barrier.setBarrier();
                        barrier.waitOnBarrier();
                        System.err.println("启动");
                    }catch (Exception e){}
                }
            }).start();
        }
        Thread.sleep(2000);
        barrier.removeBarrier();
    }
}
