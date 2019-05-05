package com.anla.zk.learn.use;

import com.anla.zk.learn.ZooKeeperLearnConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * 分布式锁，即一时间，只能一个，所以不会冲突
 * @author luoan
 * @version 1.0
 * @date 2019/5/5 10:45
 **/
public class RecipesNoLock {
    static String path = "/curator_recipes_lock_path";
    static CuratorFramework client = CuratorFrameworkFactory
            .builder()
            .connectString(ZooKeeperLearnConstant.CONNECT_STRING)
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
    public static void main(String[] args) throws InterruptedException {
        client.start();
        final InterProcessMutex mutex = new InterProcessMutex(client, path);
        final CountDownLatch downLatch = new CountDownLatch(1);
        for (int i = 0; i  < 30; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        downLatch.await();
                        mutex.acquire();
                    }catch (Exception e) {}
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss | SSSS");
                    String orderNo = simpleDateFormat.format(new Date());
                    System.out.println("生成订单序列号是： " + orderNo);
                    try {
                        mutex.release();
                    }catch (Exception e) {}
                }
            }).start();
        }
        downLatch.countDown();
    }
}
