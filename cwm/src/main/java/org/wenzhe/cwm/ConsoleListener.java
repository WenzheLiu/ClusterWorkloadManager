package org.wenzhe.cwm;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import java.util.Scanner;

import static org.wenzhe.cwm.Command.Type.GET_STATUS;
import static org.wenzhe.cwm.Command.Type.*;
import static org.wenzhe.cwm.ConsoleAction.Type.LISTEN_TO_CONSOLE;


public class ConsoleListener extends AbstractActor {
  @Override
  public Receive createReceive() {
    return receiveBuilder()
            .matchEquals(LISTEN_TO_CONSOLE, evt -> {
              System.out.println();
              System.out.print("cwm > ");
              Scanner in = new Scanner(System.in);
              String cmd = in.nextLine().trim();
              if (cmd.isEmpty()) {
                return;
              }
              sender().tell(parse(cmd), getSelf());
            })
            .build();
  }

  private Command parse(String cmd) {
    switch (cmd) {
      case "status": return GET_STATUS;
      case "quit": case "exit": case "bye": return EXIT;
      default: return NONE;
    }
  }
}
