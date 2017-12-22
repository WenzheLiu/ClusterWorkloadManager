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
              String cmd;
              do {
                System.out.println();
                System.out.print("cwm > ");
                Scanner in = new Scanner(System.in);
                cmd = in.nextLine().trim();
              } while (cmd.isEmpty());
              sender().tell(parse(cmd), getSelf());
            })
            .build();
  }

  private Command parse(String cmd) {
    switch (cmd) {
      case "status": return GET_STATUS;
      case "quit": case "exit": case "bye": return EXIT;
      default: return new UnknownCommand(cmd);
    }
  }
}
