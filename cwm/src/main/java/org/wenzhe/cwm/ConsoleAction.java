package org.wenzhe.cwm;

import java.io.Serializable;

public interface ConsoleAction extends Serializable {

  enum Type implements ConsoleAction {
    LISTEN_TO_CONSOLE
  }

  class ConsoleCommand implements ConsoleAction {
    public final String cmd;

    public ConsoleCommand(String cmd) {
      this.cmd = cmd;
    }
  }
}
