# ActiveMQ专题学习（一）——ActiveMQ安装部署

> 本章主要讲解`ActiveMQ`的下载、安装；若读者已经能够独立安装`ActiveMQ`可以跳过本章内容，继续后面的学习。如果想要了解一下大概过程，亦可大致浏览本章内容。



## 1. 下载ActiveMQ

打开[ActiveMQ官网](http://activemq.apache.org/ "http://activemq.apache.org/")，可以看到ActiveMQ分为两个版本：

- ActiveMQ 5 "Classic"

- ActiveMQ Artemis

这里我们使用经典版（ActiveMQ 5 "Classic"），点击对应的`Download Latest`链接按钮（如图1-1），进入下载界面。

<img src="https://github.com/Free-Academy/activemq-intro/blob/master/chapter-articles/pic/activemq%E5%AE%98%E7%BD%91%E9%A6%96%E9%A1%B5.png" alt="图1-1" style="zoom:70%;" />

​																									图1-1

在下载界面，根据要安装机器的操作系统，选择下载版本，笔者是mac系统下载Unix版本（如图1-2）。

<img src="https://raw.githubusercontent.com/Free-Academy/activemq-intro/master/chapter-articles/pic/activemq%E4%B8%8B%E8%BD%BD%E5%88%97%E8%A1%A8.png" alt="图1-2" style="zoom:70%;" />



​																									图1-2



## 2. 安装ActiveMQ

下载完成后，将`apache-activemq-5.16.0-bin.tar.gz`文件移动要安装的目录下（笔者的安装目录为：`~/dev/`），双击解压，默认解压到`apache-activemq-5.16.0`文件夹，打开`apache-activemq-5.16.0`文件夹，可以看到如图2-1所示结构。

<img src="https://github.com/Free-Academy/activemq-intro/blob/master/chapter-articles/pic/activemq%E6%96%87%E4%BB%B6%E5%A4%B9%E7%BB%93%E6%9E%84.png" alt="图2-1" style="zoom:50%;" />

​																									图2-1

各个文件夹大概作用：

- `bin`：主要存放启动`ActiveMQ`服务的脚本文件，可以通过`activemq start`命令启动MQ服务，也可以通过`activemq stop`命令，关闭MQ服务；
- `conf`：主要存放配置文件，如：管理界面登陆用户的配置信息、日志相关配置等；
- `data`：主要用于存放数据文件，如：ActiveMQ运行日志，ActiveMQ启动后，运行日志可以通过`./data/activemq.log`查看；
- `docs`：官方指导文档存放在该文件夹下；
- `examples`：官方自带的测试代码；
- `lib`：运行ActiveMQ依赖的`jar`包；
- `webapps`：存放ActiveMQ管理界面项目；
- `webapps-demo`：存放web测试项目。



## 3. 运行ActiveMQ

打开终端，`cd`到ActiveMQ的安装目录，运行命令：

```shell
./bin/activemq start
```

启动ActiveMQ。

ActiveMQ启动后，在浏览器中输入：[http:localhost:8161]("http:localhost:8161")，访问ActiveMQ管理界面，如果提示需要输入密码，用户名和密码都输入：admin，即可看到`ActiveMQ`管理界面（如图3-1），至此`ActiveMQ`已安装完成。

<img src="https://github.com/Free-Academy/activemq-intro/blob/master/chapter-articles/pic/activemq%E7%AE%A1%E7%90%86%E7%95%8C%E9%9D%A2.png" alt="图3-1" style="zoom:80%;" />



