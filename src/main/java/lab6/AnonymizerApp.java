package lab6;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.apache.log4j.BasicConfigurator;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;

public class AnonymizerApp {
    private static final String SYSTEM_NAME = "anonymizer";
    private static final int TIMEOUT = 5000;
    private static final int ARGS_AMOUNT = 2;

    private static final String HOST = "localhost";
    private static final String SERVERS_PATH = "http://localhost:";

    private static final String INCORRECT_ARGUMENTS_AMOUNT = "Incorrect arguments amount\n";
    private static final String SERVERS_URL_INFO = "Server URLS:\n";
    private static final String NO_SERVERS = "No servers are running\n";

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        if (args.length < ARGS_AMOUNT) {
            System.err.print(INCORRECT_ARGUMENTS_AMOUNT);
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
        StringBuilder serversLocationInfo = new StringBuilder(SERVERS_URL_INFO);

        for (int i = 1; i < args.length; i++) {
            try {
                HttpServer server = new HttpServer(http, configurator, zooKeeper, args[i]);
                final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = server.createRoute().flow(system, materializer);
                bindings.add(http.bindAndHandle(routeFlow,
                        ConnectHttp.toHost(HOST, Integer.parseInt(args[i])),
                        materializer));
                serversLocationInfo.append(SERVERS_PATH).append(args[i]).append("\n");
            } catch (InterruptedException | KeeperException e) {
                e.printStackTrace();
            }
        }

        if (bindings.size() == 0) {
            System.err.println(NO_SERVERS);
        }

        System.out.println(serversLocationInfo);
        System.in.read();

        for (CompletionStage<ServerBinding> binding : bindings) {
            binding.thenCompose(ServerBinding::unbind)
                    .thenAccept(unbound -> system.terminate());
        }
    }
}
