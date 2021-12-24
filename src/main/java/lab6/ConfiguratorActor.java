package lab6;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConfiguratorActor extends AbstractActor {
    private List<String> servers = new ArrayList<>();
    private final Random random = new Random();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(/*msgGetRandomServer.class*/, msg -> sender().tell(getRandomServer(), ActorRef.noSender()))
                .match(/*msg*/)
                .build();
    }

    private String getRandomServer() {
        return servers.get(random.nextInt(servers.size()));
    }
}
