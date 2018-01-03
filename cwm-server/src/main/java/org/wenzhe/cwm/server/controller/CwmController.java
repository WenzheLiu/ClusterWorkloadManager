package org.wenzhe.cwm.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wenzhe.cwm.akka.AkkaClusterService;
import org.wenzhe.cwm.domain.*;
import org.wenzhe.cwm.server.model.RunJobModel;
import org.wenzhe.cwm.service.ClusterService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CwmController {

  private static Logger LOGGER = LoggerFactory.getLogger(CwmController.class);

  @Autowired
  private ClusterService clusterService;

  @GetMapping("/servers")
  public List<Server> servers() {
    return clusterService.getServers();
  }

  @GetMapping("/workers")
  public List<Worker> workers(@RequestParam(value="host", defaultValue="") String host,
                              @RequestParam(value="port", defaultValue="-1") int port) {
    if (host.isEmpty() || port == -1) {
      return clusterService.getWorkers();
    } else {
      return clusterService.getWorkers(new HostPort(host, port));
    }
  }

  @GetMapping("/jobs")
  public List<JobDetail> jobs(@RequestParam(value="host", defaultValue="") String host,
                              @RequestParam(value="port", defaultValue="-1") int port) {
    if (host.isEmpty() || port == -1) {
      return clusterService.getJobs();
    } else {
      return clusterService.getJobs(new HostPort(host, port));
    }
  }

  @PostMapping("/run")
  public void run(@RequestBody RunJobModel model) {
    LOGGER.debug("Run job: {} in host:port {}", model.getJob(), model.getHostPorts());
    clusterService.runJob(new CommandLineJob(model.getJob()), toHostPorts(model.getHostPorts()));
  }

  @PostMapping("/shutdown")
  public void shutdown(@RequestBody List<String> hostPorts) {
    LOGGER.debug("Shutdown server {}", hostPorts);
    clusterService.shutdown(toHostPorts(hostPorts));

    // if shutdown itself, exit the program after exiting the cluster
    if (hostPorts.isEmpty()) {
      new Thread(() -> {
        try {
          Thread.sleep(3000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.exit(0);
      }).start();
    }
  }

  private List<HostPort> toHostPorts(@RequestBody List<String> hostPorts) {
    return hostPorts.stream().map(HostPort::from).collect(Collectors.toList());
  }
}
