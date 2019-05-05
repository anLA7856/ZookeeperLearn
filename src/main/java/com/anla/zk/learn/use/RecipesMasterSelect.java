package com.anla.zk.learn.use;

import com.anla.zk.learn.ZooKeeperLearnConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Master选举示例
 * @author luoan
 * @version 1.0
 * @date 2019/5/5 10:31
 **/
public class RecipesMasterSelect {
    static String path = "/curator_recipes_master_path";
    static CuratorFramework client = CuratorFrameworkFactory
            .builder()
            .connectString(ZooKeeperLearnConstant.CONNECT_STRING)
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
    public static void main(String[] args) throws InterruptedException {
        client.start();
        LeaderSelector selector = new LeaderSelector(client, path, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println("成为Master角色");
                Thread.sleep(3000);
                System.out.println("完成Master操作，释放Master权利");
            }
        });
        selector.autoRequeue();
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
