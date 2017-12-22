package org.wenzhe.cwm;

import java.io.Serializable;

public interface Command extends Serializable {

  enum Type implements Command {
    EXIT,
    GET_STATUS
  }

  class UnknownCommand implements Command {
    public final String cmd;

    public UnknownCommand(String cmd) {
      this.cmd = cmd;
    }

    @Override
    public String toString() {
      return "Unknown Command: " + cmd;
    }
  }
}
