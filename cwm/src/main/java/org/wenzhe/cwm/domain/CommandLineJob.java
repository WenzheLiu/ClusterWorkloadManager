package org.wenzhe.cwm.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class CommandLineJob extends Job {

  public static class Result {
    public final int exitValue;
    public final Exception exception;
    public final LocalDateTime time = LocalDateTime.now();
    public final Job job;

    public Result(Job job, int exitValue, Exception exception) {
      this.job = job;
      this.exitValue = exitValue;
      this.exception = exception;
    }

    @Override
    public String toString() {
      return Integer.toString(exitValue);
    }
  }

  private final List<String> command;

  public CommandLineJob(List<String> command) {
    this.command = Collections.unmodifiableList(command);
  }

  public List<String> getCommand() {
    return command;
  }

  @Override
  public String toString() {
    return String.join(" ", command);
  }
}
