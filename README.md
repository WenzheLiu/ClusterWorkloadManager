# Run in command line
## locally across processes
```
java ~/tools/cwm-1.0-SNAPSHOT-allinone.jar 2551 2  (as the first seed node)
java ~/tools/cwm-1.0-SNAPSHOT-allinone.jar 2554 4
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

# Test jobs

$ cat testdata/test.py
import sys

sys.exit(1)


$ cat testdata/longtime.py
import time

time.sleep(30)


