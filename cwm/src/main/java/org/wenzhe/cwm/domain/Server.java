package org.wenzhe.cwm.domain;

import java.io.Serializable;

public class Server implements Serializable {

  public enum Status {
    JOINING, WEAKLY_UP, UP, LEAVING, EXITING, DOWN, REMOVED, UNKNOWN
  }

  public final String host;
  public final int port;
  public final Status status;
  public final boolean isReachable;

  public Server(String host, int port, Status status, boolean isReachable) {
    this.host = host;
    this.port = port;
    this.status = status;
    this.isReachable = isReachable;
  }

  @Override
  public String toString() {
    return String.format("%s:%d %s %s", host, port, status,
            isReachable ? "reachable" : "unreachable");
  }
}
