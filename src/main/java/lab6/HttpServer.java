package lab6;

import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import org.apache.zookeeper.*;

import java.nio.charset.StandardCharsets;

public class HttpServer implements Watcher {
    private final Http http;
    private final ActorRef configurator;
    private final ZooKeeper zoo;
    private final String serverPath;

    private static String SERVERS_PATH = "localhost:";


    public HttpServer(Http http, ActorRef configurator, ZooKeeper zoo, String port) throws InterruptedException, KeeperException {
        this.http = http;
        this.configurator = configurator;
        this.zoo = zoo;
        this.serverPath = SERVERS_PATH + port;
        zoo.create("/servers/" + serverPath,
                serverPath.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            zoo.getData(serverPath, this, null);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
