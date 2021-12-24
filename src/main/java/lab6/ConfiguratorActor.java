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
                .match(GetRandomServerMessage.class, msg -> sender().tell(getRandomServer(), ActorRef.noSender()))
                .match(/*msgSendServers.class*/, msg -> servers = msg.getServers())
                .build();
    }

    private String getRandomServer() {
        return servers.get(random.nextInt(servers.size()));
    }
}
