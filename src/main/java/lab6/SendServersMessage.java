package lab6;

import java.util.List;

public class SendServersMessage {
    private final List<String> servers;

    public SendServersMessage(List<String> servers) {
        this.servers = servers;
    }

    public List<String> getServers() {
        return servers;
    }
}
