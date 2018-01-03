package org.wenzhe.cwm.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.wenzhe.cwm.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ManagerActor extends AbstractActor {

  private static class WorkerWrapper {
    private final ActorRef workerActor;
    private Worker.Status status = Worker.Status.IDLE;

    public WorkerWrapper(ActorRef workerActor) {
      this.workerActor = workerActor;
    }

    public ActorRef getWorkerActor() {
      return workerActor;
    }

    public Worker.Status status() {
      return status;
    }

    public void setStatus(Worker.Status status) {
      this.status = status;
    }

    private String getName() {
      return workerActor.path().name();
    }

    public Worker toWorker() {
      return new Worker(getName(), status);
    }
  }

  private static class AkkaJobData {
    private JobStatus jobStatus = JobStatus.SUBMITTING;
    private LocalDateTime startTime = LocalDateTime.MAX;
    private LocalDateTime endTime = LocalDateTime.MAX;
    private Exception exception = null;

    public void start(LocalDateTime time) {
      jobStatus = JobStatus.RUNNING;
      startTime = time;
      exception = null;
    }

    public void end(int exitValue, Exception e, LocalDateTime time) {
      jobStatus = exitValue == 0 ? JobStatus.DONE : JobStatus.FAIL;
      endTime = time;
      exception = e;
    }

    public String getErrorMessage() {
      return exception == null ? "" : exception.getMessage();
    }
  }

  private final List<WorkerWrapper> workers;
  private final Map<Job, AkkaJobData> jobs = new HashMap<>();
  private final Map<Address, AkkaServer> servers = new HashMap<>();
  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  Cluster cluster = Cluster.get(getContext().system());

  public ManagerActor(int workerCount) {
    workers = new ArrayList<>(workerCount);
    for (int i = 0; i < workerCount; ++i) {
      workers.add(new WorkerWrapper(getContext().actorOf(Props.create(WorkerActor.class), "worker" + (i + 1))));
    }
  }

  public static Props props(int workerCount) {
    return Props.create(ManagerActor.class, () -> new ManagerActor(workerCount));
  }

  @Override
  public void preStart() {
    cluster.subscribe(self(), ClusterEvent.initialStateAsEvents(),
            ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class, ClusterEvent.ReachableMember.class);
  }

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
              List<Server> svs = servers.values().stream().map(AkkaServer::toServer).collect(Collectors.toList());
              sender().tell(svs, self());
            })
            .match(Job.class, job -> {
              jobs.put(job, new AkkaJobData());
              workers.stream().filter(worker -> worker.status() == Worker.Status.IDLE)
              .findAny().ifPresent(worker -> {
                worker.setStatus(Worker.Status.WORKING);
                //worker.setRequestActor(sender());
                log.info("run {}", job);
                worker.getWorkerActor().tell(job, self());
              });
            })
            .match(WorkerActor.JobStarted.class, jobStarted -> {
              jobs.get(jobStarted.job).start(jobStarted.time);
            })
            .match(CommandLineJob.Result.class, result -> {
              jobs.get(result.job).end(result.exitValue, result.exception, result.time);
              workers.stream().filter(worker -> worker.getWorkerActor() == sender())
              .findAny().ifPresent(worker -> {
                if (result.exception != null) {
                  log.info(result.exception.getMessage());
                } else {
                  log.info("exit value: {}", result.exitValue);
                }
                worker.setStatus(Worker.Status.IDLE);
              });
            })
            .matchEquals(Command.GET_WORKERS, cmd -> {
              sender().tell(workers.stream().map(it -> it.toWorker()).collect(Collectors.toList()), self());
            })
            .matchEquals(Command.GET_JOBS, cmd -> {
              sender().tell(getJobDetails(), self());
            })
            .build();
  }

  private List<JobDetail> getJobDetails() {
    return jobs.entrySet().stream().map(jobData -> new JobDetail(
            jobData.getKey(),
            jobData.getValue().jobStatus,
            jobData.getValue().startTime,
            jobData.getValue().endTime,
            jobData.getValue().getErrorMessage()))
    .collect(Collectors.toList());
  }
}
