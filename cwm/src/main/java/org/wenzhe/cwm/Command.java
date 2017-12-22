package org.wenzhe.cwm;

import java.io.Serializable;

public interface Command extends Serializable {

  enum Type implements Command {
    NONE, EXIT,
    GET_STATUS
  }
}
