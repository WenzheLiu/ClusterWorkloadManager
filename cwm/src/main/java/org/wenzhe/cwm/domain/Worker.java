package org.wenzhe.cwm.domain;

import java.io.Serializable;

public class Worker implements Serializable {
  public enum Status {
    IDLE, WORKING
  }

  public final String name;
  public final Status status;

  public Worker(String name, Status status) {
    this.name = name;
    this.status = status;
  }

  @Override
  public String toString() {
    return name + " " + status;
  }
}
