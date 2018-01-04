package org.wenzhe.cwm.console;

import org.wenzhe.cwm.akka.AkkaClusterService;
import org.wenzhe.cwm.domain.*;
import org.wenzhe.cwm.service.ClusterService;

import java.util.*;
import java.util.stream.Collectors;

public class ConsoleMain {

  public static void run(ClusterService clusterService) {
    boolean exit = false;
    while (!exit) {
      System.out.print("crms> ");
      Scanner in = new Scanner(System.in);
      String cmd = in.nextLine().trim();
      if (cmd.isEmpty()) {
        continue;
      }
      List<String> cmdParts = Arrays.stream(cmd.split(" "))
              .filter(cmdPart -> !cmdPart.isEmpty())
              .collect(Collectors.toList());
      Iterator<String> it = cmdParts.iterator();
      String cmdType = it.next();
      List<HostPort> hostPorts = new ArrayList<>(cmdParts.size());
      List<String> cmdArgs = new ArrayList<>(cmdParts.size());
      boolean applyToAllAvailableServers = false;
      while (it.hasNext()) {
        String cmdPart = it.next();
        if (cmdPart.contains(":")) {
          hostPorts.add(HostPort.from(cmdPart));
        } else {
          applyToAllAvailableServers = cmdPart.equals("all");
          cmdArgs.add(cmdPart);
          break;
        }
      }
      while (it.hasNext()) {
        cmdArgs.add(it.next());
      }

      switch (cmdType) {
        case "servers":
          clusterService.getServers().stream().map(Server::toString).forEach(System.out::println);
          break;
        case "run":
          if (cmdArgs.isEmpty()) {
            System.out.println("No command to run");
          } else {
            Job job = new CommandLineJob(cmdArgs);
            System.out.printf("A new job is created:\n%s %s\n", job.uuid.toString(), job.toString());
            clusterService.runJob(job, hostPorts);
          }
          break;
        case "workers":
          if (hostPorts.isEmpty()) {
            clusterService.getWorkers().stream()
                    .map(Worker::toString)
                    .forEach(System.out::println);
          } else {
            hostPorts.stream().forEach(hostPort -> {
              System.out.printf("Get workers from Server %s: \n", hostPort);
              clusterService.getWorkers(hostPort).stream()
                      .map(Worker::toString)
                      .forEach(System.out::println);
            });
          }
          break;
        case "jobs":
          if (hostPorts.isEmpty()) {
            clusterService.getJobs().stream()
                    .map(JobDetail::toString)
                    .forEach(System.out::println);
          } else {
            hostPorts.stream().forEach(hostPort -> {
              System.out.printf("Get jobs from Server %s: \n", hostPort);
              clusterService.getJobs(hostPort).stream()
                      .map(JobDetail::toString)
                      .forEach(System.out::println);
            });
          }
          break;
        case "quit": case "exit": case "bye": case "shutdown":
          clusterService.shutdown(hostPorts);
          if (hostPorts.isEmpty()) {
            System.exit(0);
          }
          break;
        default:
          System.out.printf("Unknown command: %s\n", cmd);
          break;
      }
    }
  }

  public static void main(String[] args) {
    int port = Integer.parseInt(args[0]);
    int workerCount = Integer.parseInt(args[1]);
    AkkaClusterService clusterService = new AkkaClusterService(port, workerCount);
    run(clusterService);
  }
}
