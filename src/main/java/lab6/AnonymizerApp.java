package lab6;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.Http;
import akka.stream.ActorMaterializer;
import org.apache.log4j.BasicConfigurator;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AnonymizerApp {
    private static final String SYSTEM_NAME = "anonymizer";
    private static final int TIMEOUT = 5000;



    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        if (args.length < 2) {
            System.err.println("Incorrect arguments amount");
            System.exit(-1);
        }

        BasicConfigurator.configure();
        System.out.println("Start with ports: " + Arrays.toString(args));
        ActorSystem system = ActorSystem.create(SYSTEM_NAME);
        ActorRef configurator = system.actorOf(Props.create(ConfiguratorActor.class));
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        ZooKeeper zooKeeper = null;

        zooKeeper = new ZooKeeper(args[0], TIMEOUT,null);
        new ZooKeeperWatcher(zooKeeper, configurator);

        ArrayList

    }
}
