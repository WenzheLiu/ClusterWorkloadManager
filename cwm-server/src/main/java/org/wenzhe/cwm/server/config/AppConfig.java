package org.wenzhe.cwm.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wenzhe.cwm.akka.AkkaClusterService;
import org.wenzhe.cwm.service.ClusterService;

@Configuration
public class AppConfig {

  @Bean
  public ClusterService clusterService(@Value("${cwm.port}") int port, @Value("${cwm.workerCount}") int workerCount) {
    return new AkkaClusterService(port, workerCount);
  }
}
