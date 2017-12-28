package org.wenzhe.cwm;

import akka.actor.Address;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public class ServerStatus implements Serializable {

  public final Map<Address, Server> servers;

  public ServerStatus(Map<Address, Server> servers) {
    this.servers = Collections.unmodifiableMap(servers);
  }
}
