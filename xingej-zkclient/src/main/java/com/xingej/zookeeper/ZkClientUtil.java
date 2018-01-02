package com.xingej.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * 创建zk工具类
 * 
 * @author erjun
 * 2018年1月2日 上午9:20:11
 */
public class ZkClientUtil {
    
    //将构造方法，私有化，外界不允许创建对象
    private ZkClientUtil(){
        
    }
    
    //session超时时间
    private static final int SESSION_OUTTIME = 5000;//ms毫秒
    
    //阻塞程序执行，用于等待zookeeper连接成功，发送成功信号
    private static final CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static final CountDownLatch connectedSemaphore2 = new CountDownLatch(1);
    
    
    private static  ZooKeeper zk = null;
    
    
    static{
        //创建一个zk实例对象
        try {
            zk = new ZooKeeper(PropertiesUtil.getValue("zk_addr"), SESSION_OUTTIME, new Watcher() {
                
                @Override
                public void process(WatchedEvent event) {
                    //获取事件的状态
                    KeeperState keeperState = event.getState();
                    
                    //获取事件的类型
                    EventType eventType = event.getType();
                    
                    //开始判断事件的状态，以及事件的类型
                    if (keeperState.SyncConnected == keeperState) {
                       if (EventType.None == eventType) {
                           //如果建立链接成功，则发送信号量，让后续阻塞程序，可以往下执行
                           connectedSemaphore.countDown();
                           System.out.println("---->zk 建立链接-----");
                       }
                    }
                }
            });
            
            //进行阻塞
            connectedSemaphore.await();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
   
        
    }
    
    public static ZooKeeper getInstance(){
        
        return zk;
    }
    
    //创建节点
    public static void create(String path, byte[] data, CreateMode createMode){
        try {
            zk.create(path, data, Ids.OPEN_ACL_UNSAFE, createMode);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    //获取节点信息
    public static byte[] getNodeInfo(String path){
        byte[] bys = null;
        
        try {
            bys = zk.getData(path, false,null);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return bys;
    }
    
    
    
}
