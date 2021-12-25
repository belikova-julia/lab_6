package lab6;

import akka.actor.ActorRef;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;

public class ZooKeeperWatcher implements Watcher {
    private final ZooKeeper zooKeeper;
    private final ActorRef configurator;

    private static final String SERVERS_ROOT = "/servers";

    public ZooKeeperWatcher(ZooKeeper zooKeeper, ActorRef configurator)
            throws InterruptedException, KeeperException {
        this.configurator = configurator;
        this.zooKeeper = zooKeeper;

        String serversData = new String(zooKeeper.getData(SERVERS_ROOT, true, null));
        System.out.println("Servers data: " + serversData);
    }

    public void sendServers() throws InterruptedException, KeeperException {
        ArrayList<String> servers = new ArrayList<>();
        for (String server : zooKeeper.getChildren(SERVERS_ROOT, this)) {
            servers.add(new String(zooKeeper.getData(SERVERS_ROOT + server, false, null)));
        }
        configurator.tell(new SendServersMessage(servers), ActorRef.noSender());
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            zooKeeper.getChildren(SERVERS_ROOT, this);
            sendServers();
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
