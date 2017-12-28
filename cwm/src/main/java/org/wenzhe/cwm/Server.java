package org.wenzhe.cwm;

import akka.cluster.Member;
import akka.cluster.MemberStatus;

import java.io.Serializable;

public class Server implements Serializable {

  public final Member clusterMember;
  public final boolean isReachable;

  private Server(Member clusterMember, boolean isReachable) {
    this.clusterMember = clusterMember;
    this.isReachable = isReachable;
  }

  public Server(Member clusterMember) {
    this(clusterMember, isReachable(clusterMember));
  }

  private static boolean isReachable(Member clusterMember) {
    MemberStatus status = clusterMember.status();
    return status != MemberStatus.removed() && status != MemberStatus.down();
  }

  public Server toUnReachable() {
    return new Server(clusterMember, false);
  }
}
