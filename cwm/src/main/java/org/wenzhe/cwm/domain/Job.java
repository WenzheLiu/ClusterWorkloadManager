package org.wenzhe.cwm.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class Job implements Serializable {

  public final UUID uuid = UUID.randomUUID();

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Job)) return false;
    Job job = (Job) o;
    return Objects.equals(uuid, job.uuid);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(uuid);
  }

  @Override
  public String toString() {
    return uuid.toString();
  }
}
