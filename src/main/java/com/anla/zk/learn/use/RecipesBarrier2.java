package com.anla.zk.learn.use;

import com.anla.zk.learn.ZooKeeperLearnConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 自发触发barrier触发
 * @author luoan
 * @version 1.0
 * @date 2019/5/5 11:50
 **/
public class RecipesBarrier2 {
    static String barrierPath = "/curator_recipes_barrier2_path";
    public static void main(String[] args){
        for (int i = 0;i < 5; i++){
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
                        DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, barrierPath, 5);
                        Thread.sleep(Math.round(Math.random() * 3000));
                        System.out.println(Thread.currentThread().getName() + "号进入barrier");
                        barrier.enter();
                        System.out.println("启动");
                        Thread.sleep(Math.round(Math.random() * 3000));
                        barrier.leave();
                        System.out.println("退出...");

                    }catch (Exception e) {

                    }
                }
            }).start();
        }
    }

}
