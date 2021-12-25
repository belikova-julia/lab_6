package lab6;

import akka.actor.ActorSystem;
import org.apache.log4j.BasicConfigurator;

import java.util.Arrays;

public class AnonymizerApp {
    private static final String SYSTEM_NAME = "anonymizer"
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Incorrect arguments amount");
            System.exit(-1);
        }

        BasicConfigurator.configure();
        System.out.println("Start with ports: " + Arrays.toString(args));
        ActorSystem system = ActorSystem.create(SYSTEM_NAME);
        
    }
}
