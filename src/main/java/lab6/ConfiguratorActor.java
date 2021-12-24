package lab6;

import akka.actor.AbstractActor;

import java.util.ArrayList;
import java.util.List;

public class ConfiguratorActor extends AbstractActor {
    private List<String> servers = new ArrayList<>();
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match()
                .match()
                .build();
    }
}
