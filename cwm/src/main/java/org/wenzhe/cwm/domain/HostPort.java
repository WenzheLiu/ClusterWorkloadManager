package org.wenzhe.cwm.domain;

import java.io.Serializable;

public class HostPort implements Serializable {

  public final String host;
  public final int port;

  public HostPort(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public static HostPort from(String hostPort) {
    String[] hp = hostPort.split(":");
    if (hp.length != 2) {
      throw new IllegalArgumentException("Format should be host:port");
    }
    return new HostPort(hp[0], Integer.parseInt(hp[1]));
  }

  @Override
  public String toString() {
    return host + ":" + port;
  }
}
