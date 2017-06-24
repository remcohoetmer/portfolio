import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

public class ZooCreator {
    static Logger LOG = Logger.getLogger(ZooCreator.class.getName());
    static ZooKeeper zk = null;
    static String znode= "/rnode";

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        String response;
        zk = new ZooKeeper("localhost:2181", 2000, new RemcoWatcher());

        response= zk.create( znode ,"new rznode".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        LOG.info( "Response: " + response);
    }


    private static class RemcoWatcher implements Watcher {
        public void process(WatchedEvent event) {
            String path = event.getPath();
            LOG.info(String.format("Process event %s path %s", event, "path"));
            if (event.getType() == Event.EventType.None) {
                // We are are being told that the state of the
                // connection has changed
                switch (event.getState()) {
                    case SyncConnected:
                        // In this particular example we don't need to do anything
                        // here - watches are automatically re-registered with
                        // server and any watches triggered while the client was
                        // disconnected will be delivered (in order of course)
                        break;
                    case Expired:
                        // It's all over
                        break;
                }
            }
            else {
                if (path != null && path.equals(znode)) {
                    // Something has changed on the node, let's find out
                    zk.exists(znode, true, new RemcoStatCallback(), null);
                }
            }
        }
    }

    private static class RemcoStatCallback implements AsyncCallback.StatCallback {
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            boolean exists;
            switch (rc) {
                case KeeperException.Code.Ok:
                    exists = true;
                    break;
                case KeeperException.Code.NoNode:
                    exists = false;
                    break;
                case KeeperException.Code.SessionExpired:
                case KeeperException.Code.NoAuth:
 /*                   dead = true;
                    listener.closing(rc);
 */                   return;
                default:
                    // Retry errors
                    zk.exists(znode, true, this, null);
                    return;
            }

            byte b[] = null;
            if (exists) {
                try {
                    b = zk.getData(znode, false, null);
                } catch (KeeperException e) {
                    // We don't need to worry about recovering now. The watch
                    // callbacks will kick off any exception handling
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    return;
                }
            }
            /*
            if ((b == null && b != prevData)
                    || (b != null && !Arrays.equals(prevData, b))) {
                listener.exists(b);
                prevData = b;
            }
            */
        }
    }
}
