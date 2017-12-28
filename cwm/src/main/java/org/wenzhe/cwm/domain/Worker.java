package org.wenzhe.cwm.domain;

import java.io.Serializable;

public class Worker implements Serializable {
  public enum Status {
    IDLE, WORKING
  }

  private final String name;
  private final Status status;

  public Worker(String name, Status status) {
    this.name = name;
    this.status = status;
  }

  public Status getStatus() {
    return status;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name + " " + status;
  }
}
