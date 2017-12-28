package org.wenzhe.cwm.akka;

import akka.cluster.Member;
import akka.cluster.MemberStatus;

import java.io.Serializable;
import java.text.MessageFormat;

public class AkkaServer implements Serializable {

  private final Member clusterMember;
  private final boolean isReachable;

  private AkkaServer(Member clusterMember, boolean isReachable) {
    this.clusterMember = clusterMember;
    this.isReachable = isReachable;
  }

  public AkkaServer(Member clusterMember) {
    this(clusterMember, isReachable(clusterMember));
  }

  private static boolean isReachable(Member clusterMember) {
    MemberStatus status = clusterMember.status();
    return status != MemberStatus.removed() && status != MemberStatus.down();
  }

  public AkkaServer toUnReachable() {
    return new AkkaServer(clusterMember, false);
  }

  @Override
  public String toString() {
    return MessageFormat.format("{0} {1} {2}",
            clusterMember.address().toString(),
            clusterMember.status().toString(),
            isReachable ? "reachable" : "unreachable"
            );
  }
}
