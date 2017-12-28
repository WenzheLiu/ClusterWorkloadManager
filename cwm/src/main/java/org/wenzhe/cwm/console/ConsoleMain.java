package org.wenzhe.cwm.console;

import org.wenzhe.cwm.akka.AkkaClusterService;
import org.wenzhe.cwm.domain.CommandLineJob;
import org.wenzhe.cwm.domain.Job;
import org.wenzhe.cwm.domain.JobDetail;
import org.wenzhe.cwm.domain.Worker;

import java.util.*;
import java.util.stream.Collectors;

public class ConsoleMain {

  public static void main(String[] args) {
    String port = args[0];
    int workerCount = Integer.parseInt(args[1]);
    AkkaClusterService clusterService = new AkkaClusterService(port, workerCount);
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
      List<String> hostPorts = new ArrayList<>(cmdParts.size());
      List<String> cmdArgs = new ArrayList<>(cmdParts.size());
      while (it.hasNext()) {
        String cmdPart = it.next();
        if (cmdPart.contains(":")) {
          hostPorts.add(cmdPart);
        } else {
          cmdArgs.add(cmdPart);
          break;
        }
      }
      while (it.hasNext()) {
        cmdArgs.add(it.next());
      }

      switch (cmdType) {
        case "status":
          System.out.println(clusterService.getServersStatus());
          break;
        case "servers":
          System.out.println(clusterService.getServers());
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
          clusterService.getWorkers().stream()
          .map(Worker::toString)
          .forEach(System.out::println);
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
        case "quit": case "exit": case "bye":
          clusterService.shutdown();
          exit = true;
          break;
        default:
          System.out.printf("Unknown command: %s\n", cmd);
          break;
      }
    }
  }
}
