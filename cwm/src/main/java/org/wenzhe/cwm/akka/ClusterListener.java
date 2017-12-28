package org.wenzhe.cwm.akka;

import akka.actor.AbstractActor;
import akka.actor.Address;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.HashMap;
import java.util.Map;

public class ClusterListener extends AbstractActor {
  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  Cluster cluster = Cluster.get(getContext().system());

  private final Map<Address, AkkaServer> servers = new HashMap<>();

  //subscribe to cluster changes
  @Override
  public void preStart() {
    cluster.subscribe(self(), ClusterEvent.initialStateAsEvents(),
            ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class, ClusterEvent.ReachableMember.class);
  }

  //re-subscribe when restart
  @Override
  public void postStop() {
    cluster.unsubscribe(self());
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
            .match(ClusterEvent.UnreachableMember.class, mUnreachable -> {
              Member m = mUnreachable.member();
              servers.put(m.address(), new AkkaServer(m).toUnReachable());
              log.debug("Member detected as unreachable: {}", m);

            })
            .match(ClusterEvent.ReachableMember.class, reachable -> {
              Member m = reachable.member();
              servers.put(m.address(), new AkkaServer(m));
              log.debug("Member detected as reachable: {}", m);
            })
            .match(ClusterEvent.MemberEvent.class, message -> {
              Member m = message.member();
              servers.put(m.address(), new AkkaServer(m));
              log.debug("Member event {}: {}", message.getClass().getSimpleName(), m);
            })
            .matchEquals(Command.GET_SERVERS, evt -> {
              sender().tell(servers.values(), self());
            })
            .build();
  }
}
