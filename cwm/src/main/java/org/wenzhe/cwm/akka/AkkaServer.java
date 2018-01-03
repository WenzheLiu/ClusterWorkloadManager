package org.wenzhe.cwm.akka;

import akka.actor.Address;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import org.wenzhe.cwm.domain.Server;

public class AkkaServer {

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

  private Server.Status toStatus() {
    MemberStatus status = clusterMember.status();
    if (status == MemberStatus.joining()) {
      return Server.Status.JOINING;
    } else if (status == MemberStatus.down()) {
      return Server.Status.DOWN;
    } else if (status == MemberStatus.removed()) {
      return Server.Status.REMOVED;
    } else if (status == MemberStatus.up()) {
      return Server.Status.UP;
    } else if (status == MemberStatus.exiting()) {
      return Server.Status.EXITING;
    } else if (status == MemberStatus.leaving()) {
      return Server.Status.LEAVING;
    } else if (status == MemberStatus.weaklyUp()) {
      return Server.Status.WEAKLY_UP;
    } else {
      return Server.Status.UNKNOWN;
    }
  }

  public Server toServer() {
    Address address = clusterMember.address();
    String host = address.host().isDefined() ? address.host().get() : "Unknown";
    int port = address.port().isDefined() ? (Integer) address.port().get() : -1;
    return new Server(host, port, toStatus(), isReachable);
  }
}
