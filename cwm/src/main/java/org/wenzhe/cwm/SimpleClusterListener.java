package org.wenzhe.cwm;

import akka.actor.AbstractActor;
import akka.actor.Address;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.ReachableMember;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.cluster.Member;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.HashMap;
import java.util.Map;

public class SimpleClusterListener extends AbstractActor {
  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  Cluster cluster = Cluster.get(getContext().system());

  private final Map<Address, Server> servers = new HashMap<>();

  //subscribe to cluster changes
  @Override
  public void preStart() {
    cluster.subscribe(self(), ClusterEvent.initialStateAsEvents(),
            MemberEvent.class, UnreachableMember.class, ReachableMember.class);
  }

  //re-subscribe when restart
  @Override
  public void postStop() {
    cluster.unsubscribe(self());
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
            .match(UnreachableMember.class, mUnreachable -> {
              Member m = mUnreachable.member();
              servers.put(m.address(), new Server(m).toUnReachable());
              log.info("Member detected as unreachable: {}", m);

            })
            .match(ReachableMember.class, reachable -> {
              Member m = reachable.member();
              servers.put(m.address(), new Server(m));
              log.info("Member detected as reachable: {}", m);
            })
            .match(MemberEvent.class, message -> {
              Member m = message.member();
              servers.put(m.address(), new Server(m));
              log.info("Member event {}: {}", message.getClass().getSimpleName(), m);
            })
            .matchEquals(Command.Type.GET_SERVERS, evt -> {
              sender().tell(servers, self());
            })
            .build();
  }
}
