package org.wenzhe.cwm.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wenzhe.cwm.akka.AkkaClusterService;
import org.wenzhe.cwm.console.ConsoleMain;
import org.wenzhe.cwm.service.ClusterService;

@Configuration
public class AppConfig {

  @Bean
  public ClusterService clusterService(@Value("${cwm.port}") int port, @Value("${cwm.workerCount}") int workerCount) {
    ClusterService clusterService = new AkkaClusterService(port, workerCount);
    // run command line in another thread
    Thread t = new Thread(() -> ConsoleMain.run(clusterService));
    t.setDaemon(true);
    t.start();
    return clusterService;
  }
}
