VisualVM Remote Edition
=======================

VisualVM Sampler的内存分析部分之前是不支持远程调用的，在可以预见的短时间内也不会增加此功能；本工具基于VisualVM 1.3.2修改，添加了Sampler远程内存分析功能。
在APP Server端添加一个Agent(jmx-agent.jar)，并在客户端修改了接入方式，使其可以进行CPU和内存的远程Sampler。
注：这一版不支持本地CPU和内存Sampler，即本地和远程操作是二选一的。

使用方法：

1.上传jmx-agent.jar到服务器

2.在应用JVM进程启动参数中添加如下配置(javaagent要指向jar包的绝对路径)，然后重启应用
`-Dcom.sun.management.jmxremote.port=1090 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.rmi.server.hostname=本机IP`
`-javaagent:/home/admin/jmx-agent.jar`


3.在本地启动VisualVM Remote Edition，用服务器IP和JMX端口添加远程主机即可

