package org.wenzhe.cwm;

import akka.actor.AbstractActor;

import java.util.Scanner;

import static org.wenzhe.cwm.Command.Type.*;
import static org.wenzhe.cwm.ConsoleAction.Type.LISTEN_TO_CONSOLE;


public class ConsoleListener extends AbstractActor {
  @Override
  public Receive createReceive() {
    return receiveBuilder()
            .matchEquals(LISTEN_TO_CONSOLE, evt -> {
              Command cmd = null;
              while (cmd == null || cmd instanceof UnknownCommand) {
                System.out.println(cmd == null ? "" : cmd);
                System.out.print("cwm > ");
                Scanner in = new Scanner(System.in);
                cmd = parse(in.nextLine().trim());
              }
              sender().tell(cmd, getSelf());
            })
            .matchEquals(String.class, System.out::println)
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
