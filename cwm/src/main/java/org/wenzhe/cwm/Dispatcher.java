package org.wenzhe.cwm;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.Props;
import akka.util.Timeout;
import scala.concurrent.duration.Duration;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;

public class Dispatcher extends AbstractActor {

  private ActorRef consoleListener = getContext().actorOf(Props.create(ConsoleListener.class), "consoleListener");
  private ActorRef clusterListener = getContext().actorOf(Props.create(SimpleClusterListener.class), "clusterListener");

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
              Timeout t = new Timeout(Duration.create(5, TimeUnit.SECONDS));
              ask(clusterListener, Command.Type.GET_SERVERS, t).thenAccept(evt -> {
                Map<Address, Server> servers = (Map<Address, Server> ) evt;

              });
              CompletableFuture<Object> future = ask(clusterListener, cmd, t).toCompletableFuture();
              clusterListener.tell(Command.Type.GET_STATUS, self());
              listenToNextConsoleInput();
            })
            .build();
  }


}
