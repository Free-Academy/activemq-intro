# ActiveMQ专题学习（二）——JMS基础及ActiveMQ入门

>本节内容主要包含
>
>- JMS 基础：
>  - 学习JMS概念，认识JMS本质到底是什么，理解JMS和ActiveMQ之间的关系；
>  - 掌握JMS的消息模型，及每种消息模型的特点；
>  - 理解JMS编程模型，熟悉JMS中的重要接口；
>  - 了解JMS相关的专业术语。
>- ActiveMQ 入门：
>  - 通过编程，熟练实现“P2P模式”入门案例；
>  - 通过编程，熟练实现“Pub/Sub模式”入门案例。



* [ActiveMQ专题学习（二）——JMS基础及ActiveMQ入门](#activemq专题学习二jms基础及activemq入门)

   * [1.	JMS 基础](#1jms-基础)
      * [1.1.	JMS 概念](#11jms-概念)

​     - [1.2.	JMS 消息模型](#12jms-消息模型)

​     - [1.3.	JMS 编程模型](#13jms-编程模型)

   * [2.	ActiveMQ 入门](#2activemq-入门)

​     - [2.1.	“P2P模式”入门案例](#21p2p模式入门案例)

​     - [2.2.	“Pub/Sub模式”入门案例](#22pubsub模式入门案例)



## 1.	JMS 基础

### 1.1.	JMS 概念

​		JMS全称是Java Message Service，即Java消息服务，是Java平台中关于消息中间件（JMS Provider）的API；通俗讲，JMS就是`javax.jms`包下面一批的编程接口，Java平台通过这些编程接口定义了JMS协议。

​		不同的厂商根据JMS协议实现自己的消息中间件（JMS Provider），如ActiveMQ就是一个消息中间件，消息中间件主要通过发送和接收消息，实现线程或进程之间，或者分布式系统之间异步通信功能。

​		JMS是Java平台中定义的一批接口，ActiveMQ是基于JMS定义的接口实现的消息中间件，我们编程通过ActiveMQ实现异步通信功能。



### 1.2.	JMS 消息模型





### 1.3.	JMS 编程模型







## 2.	ActiveMQ 入门

### 2.1.	“P2P模式”入门案例





### 2.2.	“Pub/Sub模式”入门案例

















