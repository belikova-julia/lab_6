package lab6;

import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class HttpServer implements Watcher {
    private final Http http;
    private final ActorRef configurator;
    private final ZooKeeper zoo;


    public HttpServer(Http http, ActorRef configurator, ZooKeeper zoo, String port) {
        this.http = http;
        this.configurator = configurator;
        this.zoo = zoo;
    }
    @Override
    public void process(WatchedEvent watchedEvent) {

    }
}
