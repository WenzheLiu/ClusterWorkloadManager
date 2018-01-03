package org.wenzhe.cwm.service;

import org.wenzhe.cwm.domain.*;

import java.util.List;

public interface ClusterService {
  void shutdown(List<HostPort> hostPorts);

  List<Server> getServers();

  void runJob(Job job, List<HostPort> hostPorts);

  List<Worker> getWorkers();

  List<Worker> getWorkers(HostPort hostPort);

  List<JobDetail> getJobs();

  List<JobDetail> getJobs(HostPort hostPort);
}
