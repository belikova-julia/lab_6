package lab6;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
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
    private static final int ARGS_AMOUNT = 2;



    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        if (args.length < ARGS_AMOUNT) {
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

        ArrayList<CompletionStage<ServerBinding>> bindings = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            HttpServer server = new HttpServer(http, configurator, zooKeeper, args[i]);
            Flow<HttpRequest, HttpResponse, NotUsed> routFlow = server.createRoute().flow(system)
        }

    }
}
