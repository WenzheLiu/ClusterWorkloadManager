# Run in command line
## locally across processes
```
java -jar cwm/target/cwm-1.0-SNAPSHOT-allinone.jar 2551 2  (as the first seed node)
java -jar cwm/target/cwm-1.0-SNAPSHOT-allinone.jar 2554 4
```
## clusters across servers
In server fnode404 (as the first seed node):
```
java -Dakka.remote.netty.tcp.hostname=fnode404 -Dakka.cluster.seed-nodes.0=akka.tcp://ClusterSystem@fnode404:2551  -jar ~/tools/cwm-1.0-SNAPSHOT-allinone.jar 2551 2
```
In server fnode408:
```
java -Dakka.remote.netty.tcp.hostname=fnode408 -Dakka.cluster.seed-nodes.0=akka.tcp://ClusterSystem@fnode404:2551  -jar ~/tools/cwm-1.0-SNAPSHOT-allinone.jar 2556 3
```
# Run in web
We can put the war in the tomcat, but we can also run directly without tomcat, because tomcat has provided by the war itself.
## run command locally across processes
```
java -jar cwm-server/target/cwm-server-0.0.1-SNAPSHOT.war --cwm.port=2551 --cwm.workerCount=3
```
Or using `-D`:
```
java -Dcwm.port=2551 -Dcwm.workerCount=5 -jar cwm-server/target/cwm-server-0.0.1-SNAPSHOT.war
```

## run command in clusters across servers
```
java -Dakka.remote.netty.tcp.hostname=fnode404 -Dakka.cluster.seed-nodes.0=akka.tcp://ClusterSystem@fnode404:2551 -jar ~/tools/crms-server.war --cwm.port=2551 --cwm.workerCount=4
```
Or using `-D`:
```
java -Dakka.remote.netty.tcp.hostname=fnode404 -Dakka.cluster.seed-nodes.0=akka.tcp://ClusterSystem@fnode404:2551 -Dcwm.port=2551 -Dcwm.workerCount=4 -jar ~/tools/cwm-server-0.0.1-SNAPSHOT.war
```
We can also use `-Dcwm.console` to run in console command line mode by using the war file:
```
java -Dcwm.console -Dakka.remote.netty.tcp.hostname=fnode408 -Dakka.cluster.seed-nodes.0=akka.tcp://ClusterSystem@fnode404:2551 -jar ~/tools/crms-server.war 2555 7
```

## Rest API
we can use PostMan to test, for example, run a job by post command:
```
{
  "job": ["python", "C:\\Users\\weliu\\code\\wenzhe\\ClusterWorkloadManager\\testdata\\longtime.py"],
  "hostPorts": ["127.0.0.1:2551"]
}
```


# Test jobs

$ cat testdata/test.py
import sys

sys.exit(1)


$ cat testdata/longtime.py
import time

time.sleep(30)


