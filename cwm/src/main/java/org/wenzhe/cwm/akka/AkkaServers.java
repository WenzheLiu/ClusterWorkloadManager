package org.wenzhe.cwm.akka;

import java.util.Collection;
import java.util.Collections;

public class AkkaServers {

  private final Collection<AkkaServer> servers;

  public AkkaServers(Collection<AkkaServer> servers) {
    this.servers = Collections.unmodifiableCollection(servers);
  }

  @Override
  public String toString() {
    return String.join(System.lineSeparator(),
            servers.stream().map(server -> server.toString()).toArray(String[]::new));
  }
}
