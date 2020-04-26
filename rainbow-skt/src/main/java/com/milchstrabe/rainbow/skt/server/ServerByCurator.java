package com.milchstrabe.rainbow.skt.server;

import com.milchstrabe.rainbow.skt.common.util.ByteUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

/**
 * @Author ch3ng
 * @Date 2020/4/26 17:32:21
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Component
public class ServerByCurator{

    private final static String ROOT_PATH = "/rainbow";

    @Autowired
    private CuratorFramework curatorFramework;

    @Value("${server.servlet.application-display-name}")
    private String displayName;

    /**
     * create node
     */
    public void createNode() throws Exception {
        String keyPath = ROOT_PATH + "/" + displayName;
            if(curatorFramework.checkExists().forPath(keyPath)!=null){
                throw new Exception("the znode exists at:" + keyPath);
            }
            curatorFramework
                    .create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(keyPath);
    }


    /**
     * 移除节点
     * @param path
     * @return
     * @throws Exception
     */
    public boolean removeNode(String path) throws Exception {
        String keyPath = ROOT_PATH + "/" + displayName;
        if (curatorFramework.checkExists().forPath(keyPath) != null) {
            curatorFramework.delete().forPath(keyPath);
            return true;
        }
        return false;
    }

    /**
     * set data for znode
     * @param cnt
     * @return
     * @throws Exception
     */
    public boolean setData2Node(long cnt) throws Exception {
        String keyPath = ROOT_PATH + "/" + displayName;
        Stat stat = curatorFramework.setData().forPath(keyPath, ByteUtil.longToBytes(cnt));
        if(stat != null){
            return true;
        }
        return false;
    }

    /**
     * get data from znode
     * @return
     * @throws Exception
     */
    public long getDataFromNode() throws Exception {
        String keyPath = ROOT_PATH + "/" + displayName;
        byte[] bytes = curatorFramework.getData().forPath(keyPath);
        if(bytes != null && bytes.length>0){
            long l = ByteUtil.bytesToLong(bytes);
            return l;
        }
        return 0L;
    }
}