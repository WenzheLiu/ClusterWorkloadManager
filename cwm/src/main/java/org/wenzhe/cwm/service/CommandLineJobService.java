package org.wenzhe.cwm.service;

import io.reactivex.Observable;
import org.wenzhe.cwm.domain.CommandLineJob;

import java.io.IOException;

public class CommandLineJobService {

  public Observable<Process> runJob(CommandLineJob job) {
    return Observable.just(new ProcessBuilder(job.getCommand()))
            .map(processBuilder -> {
              try {
                return processBuilder.start();
              } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
              }
            });
  }
}
