package org.wenzhe.cwm;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.Scanner;

import org.wenzhe.cwm.ConsoleAction.ConsoleCommand;

public class Dispatcher extends AbstractActor {

  private ActorRef consoleListener = getContext().actorOf(Props.create(ConsoleListener.class), "consoleListener");

  @Override
  public void preStart() {
    listenToNextConsoleInput();
  }

  private void listenToNextConsoleInput() {
    consoleListener.tell(ConsoleAction.Type.LISTEN_TO_CONSOLE, getSelf());
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
            .matchEquals(Command.Type.EXIT, cmd -> getContext().getSystem().terminate())
            .matchEquals(Command.Type.GET_STATUS, cmd -> {
              System.out.println("good");
              listenToNextConsoleInput();
            })
            .match(Command.class, cmd -> {
              System.out.println(cmd);
              listenToNextConsoleInput();
            })
            .build();
  }


}
