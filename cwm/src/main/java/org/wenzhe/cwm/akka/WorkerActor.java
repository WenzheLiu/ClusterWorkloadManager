package org.wenzhe.cwm.akka;

import akka.actor.AbstractActor;
import org.wenzhe.cwm.domain.CommandLineJob;
import org.wenzhe.cwm.domain.Job;

import java.time.LocalDateTime;

public class WorkerActor extends AbstractActor {

  static class JobStarted {
    final LocalDateTime time = LocalDateTime.now();
    final Job job;

    public JobStarted(Job job) {
      this.job = job;
    }
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
            .match(CommandLineJob.class, job -> {
              ProcessBuilder processBuilder = new ProcessBuilder(job.getCommand());
              try {
                sender().tell(new JobStarted(job), self());
                Process process = processBuilder.start();
                int exitValue = process.waitFor();
                sender().tell(new CommandLineJob.Result(job, exitValue, null), self());
              } catch (Exception e) {
                sender().tell(new CommandLineJob.Result(job, -1, e), self());
              }
            })
            .build();
  }
}
