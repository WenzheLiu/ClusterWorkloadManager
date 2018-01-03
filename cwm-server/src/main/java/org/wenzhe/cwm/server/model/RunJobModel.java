package org.wenzhe.cwm.server.model;

import java.util.List;

public class RunJobModel {

  private List<String> job;
  private List<String> hostPorts;

  public List<String> getJob() {
    return job;
  }

  public void setJob(List<String> job) {
    this.job = job;
  }

  public List<String> getHostPorts() {
    return hostPorts;
  }

  public void setHostPorts(List<String> hostPorts) {
    this.hostPorts = hostPorts;
  }
}
