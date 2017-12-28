package org.wenzhe.cwm.domain;

public class ProcessJob {
  private final CommandLineJob commandLineJob;
  private final Process process;

  public ProcessJob(CommandLineJob commandLineJob, Process process) {
    this.commandLineJob = commandLineJob;
    this.process = process;
  }
}
