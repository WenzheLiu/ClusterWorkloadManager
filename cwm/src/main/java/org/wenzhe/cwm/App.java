package org.wenzhe.cwm;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Hello world!
 *
 */
public class App {
  public static void main(String[] args) {
    //akka.Main.main(new String[] { Dispatcher.class.getName() });
    startup(args[0]);
  }

  public static void startup(String port) {
    // Override the configuration of the port
    Config config = ConfigFactory.parseString(
            "akka.remote.netty.tcp.port=" + port).withFallback(
            ConfigFactory.load());

    // Create an Akka system
    ActorSystem system = ActorSystem.create("ClusterSystem", config);

    // Create an actor that handles cluster domain events
    system.actorOf(Props.create(SimpleClusterListener.class), "clusterListener");
    system.actorOf(Props.create(Dispatcher.class), "dispatcher");
  }
}
