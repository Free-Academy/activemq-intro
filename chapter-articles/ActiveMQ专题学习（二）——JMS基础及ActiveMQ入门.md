# ActiveMQ专题学习（二）——JMS基础及ActiveMQ入门

>本节内容主要包含
>
>- JMS 基础：
>    - 学习JMS概念，认识JMS本质到底是什么，理解JMS和ActiveMQ之间的关系；
>    - 掌握JMS的消息模型，及每种消息模型的特点；
>    - 理解JMS编程模型，熟悉JMS中的重要接口；
>    - 了解JMS相关的专业术语。
>- ActiveMQ 入门：
>    - 通过编程，熟练实现“P2P模式”入门案例；
>    - 通过编程，熟练实现“Pub/Sub模式”入门案例。



* [ActiveMQ专题学习（二）——JMS基础及ActiveMQ入门](#activemq专题学习二jms基础及activemq入门)
* [1.	JMS 基础](#1jms-基础)
     * [1.1.	JMS 概念](#11jms-概念)
     * [1.2.	JMS 消息模型](#12jms-消息模型)
     * [1.3.	JMS 编程模型](#13jms-编程模型)
   
* [2.	ActiveMQ 入门](#2activemq-入门)
     * [2.1.	“P2P模式”入门案例](#21p2p模式入门案例)
     * [2.2.	“Pub/Sub模式”入门案例](#22pubsub模式入门案例)


## 1.	JMS 基础

### 1.1.	JMS 概念

​		JMS全称是Java Message Service，即Java消息服务，是Java平台中关于消息中间件的API规范；通俗讲，JMS就是`javax.jms`包下面一批的编程接口，Java平台通过这些编程接口定义了JMS协议。

​		不同的厂商根据JMS协议实现自己的消息中间件，如ActiveMQ就是一个消息中间件，消息中间件主要通过发送和接收消息，实现线程或进程之间，或者分布式系统之间异步通信功能。

​		JMS是Java平台中定义的一批接口，ActiveMQ是基于JMS定义的接口实现的消息中间件，我们编程通过ActiveMQ实现异步通信功能。



### 1.2.	JMS 消息模型

​		JMS具体有两种通信模式：

- 点对点模式（Point-to-Point Messaging Domain，下文简称：P2P模式）

- 发布/订阅模式（Publish/Subscribe Messaging Domain，下文简称：Pub/Sub模式）

  

  （1）P2P模式

  <img src="https://raw.githubusercontent.com/Free-Academy/activemq-intro/master/chapter-articles/pic/2/p2p%E6%A8%A1%E5%BC%8F.png" alt="图1-2" style="zoom:40%;" />

  ​																						图1-1 p2p模式示意图

  在P2P模式中，应用程序由“生产者（Producer）”和“消费者（Consumer）”组成，Producer将消息发送到指定的消息队列（Queue），Consumer从对应的消息队列（Queue）消费消息，同时向Queue返回响应消息（Acknowledge Message）。

  P2P模式的特点：

  - 每个消息只有一个消费者；
  - 生产者和消费者之间没有依赖，生产者只管将消息发送到消息队列中，不关心消息的消费情况；
  - 消费者从队列消费消息，队列中的消息只有被消费或者超时，才会被销毁，消费者无需时时消费队列中的消息。

  

  （2）Pub/Sub模式

  <img src="https://raw.githubusercontent.com/Free-Academy/activemq-intro/master/chapter-articles/pic/2/pub%3Asub%E6%A8%A1%E5%BC%8F.png" alt="图1-2" style="zoom:40%;" />

  ​																						图1-2 pub/sub模式示意图

  在pub/sub模式中，生产者发布一个消息到指定的topic中，该消息将通过topic传递给所有订阅该topic的消费端；在该模式下生产者和消费者都是匿名的，即生产者和消费者都不知道对方是谁。

  Pub/Sub模式的特点：

  - 一个消息可以传递给多个订阅者，即一条消息可以被多次消费；
  - 默认情况下，当生产者发送消息时，消费者必须同时在线消费，即发布Topic时，订阅者必须在线监听；
  - 为了解除生产者和订阅者之间的时间耦合，JMS提供可持久化订阅，即针对某些特定的订阅者，topic会缓存消息直至其被订阅者消费或超时。



### 1.3.	JMS 编程模型







## 2.	ActiveMQ 入门

### 2.1.	“P2P模式”入门案例





### 2.2.	“Pub/Sub模式”入门案例

















