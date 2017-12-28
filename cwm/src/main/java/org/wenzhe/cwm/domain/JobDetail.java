package org.wenzhe.cwm.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JobDetail implements Serializable {
  public final Job job;
  public final JobStatus status;
  public final LocalDateTime startTime;
  public final LocalDateTime endTime;
  public final String errorMsg;

  public JobDetail(Job job, JobStatus status, LocalDateTime startTime, LocalDateTime endTime, String errorMsg) {
    this.job = job;
    this.status = status;
    this.startTime = startTime;
    this.endTime = endTime;
    this.errorMsg = errorMsg;
  }

  @Override
  public String toString() {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    StringBuilder sb = new StringBuilder(job.uuid.toString())
            .append(" ").append(status);
    if (!startTime.equals(LocalDateTime.MAX)) {
      sb.append(" ").append(startTime.format(fmt));
    }
    if (!endTime.equals(LocalDateTime.MAX)) {
      sb.append(" ").append(endTime.format(fmt));
    }
    if (!errorMsg.isEmpty()) {
      sb.append(" ").append(errorMsg);
    }
    sb.append(" ").append(job.toString());
    return sb.toString();
  }
}
