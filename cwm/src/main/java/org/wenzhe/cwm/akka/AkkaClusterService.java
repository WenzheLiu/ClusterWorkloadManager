package org.wenzhe.cwm.akka;

import akka.actor.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.wenzhe.cwm.domain.Job;
import org.wenzhe.cwm.domain.JobDetail;
import org.wenzhe.cwm.domain.ServersStatus;
import org.wenzhe.cwm.domain.Worker;
import scala.concurrent.duration.Duration;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AkkaClusterService {

  private final ActorSystem system;
  private final ActorRef clusterListener;
  private final ActorRef manager;

  public AkkaClusterService(String port, int workerCount) {
    // Override the configuration of the port
    Config config = ConfigFactory.parseString(
            "akka.remote.netty.tcp.port=" + port).withFallback(
            ConfigFactory.load());

    // Create an Akka system
    system = ActorSystem.create("ClusterSystem", config);

    // Create an actor that handles cluster domain events
    clusterListener = system.actorOf(Props.create(ClusterListener.class), "ClusterListener");

    manager = system.actorOf(ManagerActor.props(workerCount), "manager");
  }

  public void shutdown() {
    system.terminate();
  }

  public ServersStatus getServersStatus() {
    return new ServersStatus();
  }

  public AkkaServers getServers() {
    return new AkkaServers((Collection<AkkaServer>) ask(clusterListener, Command.GET_SERVERS));
  }

  public void runJob(Job job, String... hostPorts) {
    if (hostPorts == null || hostPorts.length == 0) {
      tell(manager, job);
    } else {
      Arrays.stream(hostPorts).map(this::toAkkaAddress)
              .map(system::actorSelection)
              .forEach(remoteManager -> tell(remoteManager, job));
    }
  }

  private String toAkkaAddress(String hostPort) {
    return "akka.tcp://ClusterSystem@" + hostPort + "/user/manager";
  }

  public void runJob(Job job, List<String> hostPorts) {
    runJob(job, hostPorts.toArray(new String[hostPorts.size()]));
  }

  public List<Worker> getWorkers() {
    return (List<Worker>) ask(manager, Command.GET_WORKERS);
  }

  private Inbox tell(ActorRef target, Object message) {
    Inbox inbox = Inbox.create(system);
    inbox.send(target, message);
    return inbox;
  }

  private Inbox tell(ActorSelection target, Object message) {
    Inbox inbox = Inbox.create(system);
    target.tell(message, inbox.getRef());
    return inbox;
  }

  private Object ask(ActorRef target, Object message) {
    Inbox inbox = tell(target, message);
    try {
      return inbox.receive(Duration.create(5, TimeUnit.SECONDS));
    } catch (TimeoutException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private Object ask(ActorSelection target, Object message) {
    Inbox inbox = tell(target, message);
    try {
      return inbox.receive(Duration.create(15, TimeUnit.SECONDS));
    } catch (TimeoutException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public List<JobDetail> getJobs() {
    return (List<JobDetail>) ask(manager, Command.GET_JOBS);
  }

  public List<JobDetail> getJobs(String hostPort) {
    ActorSelection remoteManager = system.actorSelection(toAkkaAddress(hostPort));
    return (List<JobDetail>) ask(remoteManager, Command.GET_JOBS);
  }
}
