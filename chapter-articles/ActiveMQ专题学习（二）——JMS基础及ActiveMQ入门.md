# ActiveMQ专题学习（二）——JMS基础及ActiveMQ入门

>本节内容主要包含
>
>- JMS 基础：
>   - 学习JMS概念，认识JMS本质到底是什么，理解JMS和ActiveMQ之间的关系；
>   - 理解P2P消息模式；
>   - 理解Pub/Sub消息模式。
>- ActiveMQ 入门：
>   - 通过编程，熟练实现“P2P模式”入门案例；
>   - 通过编程，熟练实现“Pub/Sub模式”入门案例；
>   - 梳理编程模型，掌握“P2P模式”和“Pub/Sub模式”编程思想。



* [ActiveMQ专题学习（二）——JMS基础及ActiveMQ入门](#activemq专题学习二jms基础及activemq入门)
* [1.	JMS 基础](#1jms-基础)
     * [1.1.	JMS 概念](#11jms-概念)
     * [1.2.	JMS 消息模型](#12jms-消息模型)
     
* [2.	ActiveMQ 入门](#2activemq-入门)
     * [2.1.	“P2P模式”入门案例](#21p2p模式入门案例)
     * [2.2.	“Pub/Sub模式”入门案例](#22pubsub模式入门案例)
     * [2.3.	编程模型](#23编程模型)


## 1.	JMS 基础

### 1.1.	JMS 概念

​		JMS全称是Java Message Service，即Java消息服务，是Java平台中关于消息中间件的API规范；通俗讲，JMS就是`javax.jms`包下面一批的编程接口，Java平台通过这些编程接口定义了JMS协议。

​		不同的厂商根据JMS协议实现自己的消息中间件，如ActiveMQ就是一个消息中间件，消息中间件主要通过发送和接收消息，实现线程或进程之间，或者分布式系统之间异步通信功能。

​		JMS是Java平台中定义的一批接口，ActiveMQ是基于JMS定义的接口实现的消息中间件，我们编程通过ActiveMQ实现异步通信功能。



### 1.2.	JMS 消息模型

​		JMS具体有两种通信模式：

- 点对点模式（Point-to-Point Messaging Domain，下文简称：P2P模式）

- 发布/订阅模式（Publish/Subscribe Messaging Domain，下文简称：Pub/Sub模式）

  

  **（1）P2P模式**

  <img src="https://raw.githubusercontent.com/Free-Academy/activemq-intro/master/chapter-articles/pic/2/p2p%E6%A8%A1%E5%BC%8F.png" alt="图1-2" style="zoom:40%;" />

  ​																						图1-1 p2p模式示意图

  在P2P模式中，应用程序由“生产者（Producer）”和“消费者（Consumer）”组成，Producer将消息发送到指定的消息队列（Queue），Consumer从对应的消息队列（Queue）消费消息，同时向Queue返回响应消息（Acknowledge Message）。

  P2P模式的特点：

  - 每个消息只有一个消费者；
  - 生产者和消费者之间没有依赖，生产者只管将消息发送到消息队列中，不关心消息的消费情况；
  - 消费者从队列消费消息，队列中的消息只有被消费或者超时，才会被销毁，消费者无需时时消费队列中的消息。

  

  **（2）Pub/Sub模式**

  <img src="https://raw.githubusercontent.com/Free-Academy/activemq-intro/master/chapter-articles/pic/2/pub%3Asub%E6%A8%A1%E5%BC%8F.png" alt="图1-2" style="zoom:40%;" />

  ​																						图1-2 pub/sub模式示意图

  在pub/sub模式中，生产者发布一个消息到指定的topic中，该消息将通过topic传递给所有订阅该topic的消费端；在该模式下生产者和消费者都是匿名的，即生产者和消费者都不知道对方是谁。

  Pub/Sub模式的特点：

  - 一个消息可以传递给多个订阅者，即一条消息可以被多次消费；
  - 默认情况下，当生产者发送消息时，消费者必须同时在线消费，即发布Topic时，订阅者必须在线监听；
  - 为了解除生产者和订阅者之间的时间耦合，JMS提供可持久化订阅，即针对某些特定的订阅者，topic会缓存消息直至其被订阅者消费或超时。



## 2.	ActiveMQ 入门

​		打开`IDE`新建`maven`项目，项目名`activemq-1`，在`pom.xml`中添加依赖`jar`包。

​		`pom.xml`文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.foo.study</groupId>
    <artifactId>activemq-1</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-client</artifactId>
            <version>5.16.0</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-nop</artifactId>
            <version>1.7.30</version>
        </dependency>
    </dependencies>

</project>
```

​		项目结构如图2-1:

<img src="https://raw.githubusercontent.com/Free-Academy/activemq-intro/master/chapter-articles/pic/2/activemq-01%E9%A1%B9%E7%9B%AE%E7%BB%93%E6%9E%84.png" style="zoom:50%;" />

​																									图2-1 项目结构图

### 2.1.	“P2P模式”入门案例

​		`QueueProducer`代码：

```java
package com.foo.study.activemq.p2p;

import com.foo.study.activemq.constant.Broker;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

/**
 * queue，消息生产者
 */
public class QueueProducer {

    public static void main(String[] args) {
        try {
            //1. 创建一个链接工厂，实现类为ActiveMQConnectionFactory，通过构造器传入ActiveMQ服务器的地址
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Broker.TEST_QUEUE_01_URL);
            //2. 使用ConnectionFactory创建Connection对象
            Connection connection = connectionFactory.createConnection();
            //3. 打开链接
            connection.start();
            //4. 使用Connection，创建会话对象Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //5. 使用Session，创建Destination对象，这里使用的"P2P消息模式"，所以创建的是Queue
            Destination queue = session.createQueue(Broker.TEST_QUEUE_01_NAME);
            //6. 使用Session，创建MessageProducer对象
            MessageProducer producer = session.createProducer(queue);
            //7. 创建一个TextMessage对象
            TextMessage textMessage = new ActiveMQTextMessage();
            textMessage.setText("Hello ActiveMQ");
            //8. 发送消息
            producer.send(textMessage);
            //9. 关闭资源
            producer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
```

​		`QueueConsumer`代码：

```java
package com.foo.study.activemq.p2p;

import com.foo.study.activemq.constant.Broker;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * queue，消息消费者
 */
public class QueueConsumer {

    public static void main(String[] args) {
        try {
            //1. 创建一个链接工厂，实现类为ActiveMQConnectionFactory，通过构造器传入ActiveMQ服务器的地址
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Broker.TEST_QUEUE_01_URL);
            //2. 使用ConnectionFactory创建Connection对象
            Connection connection = connectionFactory.createConnection();
            //3. 打开链接
            connection.start();
            //4. 使用Connection，创建会话对象Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //5. 使用Session，创建Destination对象，这里使用的"P2P消息模式"，所以创建的是Queue
            Destination queue = session.createQueue(Broker.TEST_QUEUE_01_NAME);
            //6. 使用Session，创建Consumer对象
            MessageConsumer consumer = session.createConsumer(queue);
            //7. 为consumer设置消息监听器，用来接收消息
            consumer.setMessageListener(new MyMessageListener());
            //8. 关闭资源
            consumer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}

/**
 * 自定义监听器，接收消息
 */
class MyMessageListener implements MessageListener {

    /**
     * 实现方法，处理从queue中接收的消息
     * @param message
     */
    public void onMessage(Message message) {
        try {
            //这里只处理TextMessage
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println("Consumer接收到的信息：" + text);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
```

​		`Broker`常量类，用于存放`ActiveMQ`的配置常量：

```java
package com.foo.study.activemq.constant;

/**
 * 常量，Broker的配置信息
 */
public class Broker {
    /**
     * ActiveMQ 服务器地址
     */
    public static final String TEST_QUEUE_01_URL = "tcp://localhost:61616";
    /**
     * ActiveMQ 配置的Queue的名称
     */
    public static final String TEST_QUEUE_01_NAME = "TEST_QUEUE_01";

}
```

​		**（1）运行`QueueProducer`**

​		运行`QueueProducer`，打开`http://localhost:8161/admin/queues.jsp`查看`ActiveMQ`管理界面效果，如图2-2：

- `Number Of Pending Messages`：等待消费的消息数为：1，即我们运行`QueueProducer`刚才发送的一条消息，消息还没有被消费；
- `Messages Enqueued`：已经排队的消息数为：1，即队列接收到的消息数量，每次`TESR_QUEUE_01`接收到一条消息，这里就会对应+1；
- `TEST_QUEUE_01`：这里如果没有手动通过管理界面创建`TEST_QUEUE_01`，当运行`Producer`时，会自动创建。

<img src="https://raw.githubusercontent.com/Free-Academy/activemq-intro/master/chapter-articles/pic/2/%E8%BF%90%E8%A1%8CQueueProducer%E6%95%88%E6%9E%9C.png" style="zoom:80%;" />

​																								图 2-2

​		**（2）运行`QueueConsumer`**

​		运行`QueueConsumer`，通过`ActiveMQ`管理界面查看效果，如图 2-3:

- `Messages Dequeued `：出列消息数量为：1，即有一条消息被消费了，每次又一条消息被消费，这里就会对应+1；

<img src="https://raw.githubusercontent.com/Free-Academy/activemq-intro/master/chapter-articles/pic/2/%E8%BF%90%E8%A1%8CQueueConsumer%E6%95%88%E6%9E%9C.png" style="zoom:80%;" />

​																								图 2-3

​		查看控制台输出：`Consumer接收到的信息：Hello ActiveMQ`，说明消息已经被消费端成功消费，如图2-4所示。

<img src="https://raw.githubusercontent.com/Free-Academy/activemq-intro/master/chapter-articles/pic/2/%E8%BF%90%E8%A1%8CQueueConsumer%E6%8E%A7%E5%88%B6%E5%8F%B0%E8%BE%93%E5%87%BA.png" style="zoom:50%;" />

​																								图 2-4

### 2.2.	“Pub/Sub模式”入门案例

​		`TopicProducer`代码：

```java
package com.foo.study.activemq.pubsub;

import com.foo.study.activemq.constant.Broker;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

/**
 * topic，消息发布者
 */
public class TopicProducer {

    public static void main(String[] args) {
        try {
            //1. 创建一个链接工厂，实现类为ActiveMQConnectionFactory，通过构造器传入ActiveMQ服务器的地址
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Broker.TEST_BROKER_URL);
            //2. 使用ConnectionFactory创建Connection对象
            Connection connection = connectionFactory.createConnection();
            //3. 打开链接
            connection.start();
            //4. 使用Connection，创建会话对象Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //5. 使用Session，创建Destination对象，这里使用的"Pub/Sub消息模式"，所以创建的是Topic
            Destination topic = session.createTopic(Broker.TEST_TOPIC_01_NAME);
            //6. 使用Session，创建MessageProducer对象
            MessageProducer producer = session.createProducer(topic);
            //7. 创建一个TextMessage对象
            TextMessage textMessage = new ActiveMQTextMessage();
            textMessage.setText("Hello ActiveMQ, Topic");
            //8. 发送消息
            producer.send(textMessage);
            System.out.println("TopicProducer 已发布消息：Hello ActiveMQ, Topic");
            //9. 关闭资源
            producer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
```

​		`TopicConsumerA`代码：

```java
package com.foo.study.activemq.pubsub;

import com.foo.study.activemq.constant.Broker;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * topic，消息订阅者A
 */
public class TopicConsumerA {

    public static void main(String[] args) {
        try {
            //1. 创建一个链接工厂，实现类为ActiveMQConnectionFactory，通过构造器传入ActiveMQ服务器的地址
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Broker.TEST_BROKER_URL);
            //2. 使用ConnectionFactory创建Connection对象
            Connection connection = connectionFactory.createConnection();
            //3. 打开链接
            connection.start();
            //4. 使用Connection，创建会话对象Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //5. 使用Session，创建Destination对象，这里使用的"Pub/Sub消息模式"，所以创建的是Topic
            Destination topic = session.createTopic(Broker.TEST_TOPIC_01_NAME);
            //6. 使用Session，创建MessageConsumer对象
            MessageConsumer consumer = session.createConsumer(topic);
            //7. 为consumer设置消息监听器，用来接收消息
            MessageListener listener = new MessageListener() {
                public void onMessage(Message message) {
                    try {
                        if (message instanceof TextMessage) {
                            TextMessage textMessage = (TextMessage) message;
                            String text = textMessage.getText();
                            System.out.println("TopicConsumerA 接收到消息：" + text);
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            };
            //8. 为Consumer对象设置监听器
            consumer.setMessageListener(listener);
            //等待键盘输入，再继续执行
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //9. 关闭资源
            consumer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
```

​		`TopicConsumerB`代码和`TopicConsumerA`代码基本一样，只是`MessageListener`中的打印语句有所不同：

```java
System.out.println("TopicConsumerB 接收到消息：" + text);
```

​		**（1）运行`TopicConsumerA`和`TopicConsumerB`**

​		运行`TopicConsumerA`和`TopicConsumerB`，打开`ActiveMQ`管理界面，如图 2-5:

- `Number Of Consumers`：订阅者数量，因为我们打开两个消费者（`TopicConsumerA`和`TopicConsumerB`），所以这里显示有2个两个消费者已链接。

<img src="/Users/bethanwang/myspace/Free-Academy/activemq-intro/chapter-articles/pic/2/运行TopicConsumer效果.png" style="zoom:80%;" />

​																								图 2-5

​		**（2）运行`TopicProducer`**

​		运行`TopicProducer`，打开`ActiveMQ`管理界面，如图 2-6:

- `Messages Enqueued `：入列消息数量为：1，因为发布者只发布了一条消息；
- `Messages Dequeued `：出列消息数量为：2，因为有2个订阅者，消息被消费了两次。

<img src="/Users/bethanwang/myspace/Free-Academy/activemq-intro/chapter-articles/pic/2/运行TopicProducer效果.png" style="zoom:80%;" />

​																								图 2-6

​		**（3）控制台打印信息**

​		`TopicProducer`控制台打印信息：`TopicProducer 已发布消息：Hello ActiveMQ, Topic`，说明生产者已发送报文内容为`Hello ActiveMQ, Topic`的消息，如图 2-7。

<img src="/Users/bethanwang/myspace/Free-Academy/activemq-intro/chapter-articles/pic/2/运行TopicProducer控制台输出.png" style="zoom:50%;" />

​																								图 2-7

​		`TopicConsumerA`和`TopicConsumerB`，控制台分别打印：

`TopicConsumerA 接收到消息：Hello ActiveMQ, Topic`、`TopicConsumerB 接收到消息：Hello ActiveMQ, Topic`，说明`TopicConsumerA`和`TopicConsumerB`都从订阅的`TEST_TOPIC_01`中消费了一条消息，接收到的消息内容都是：`Hello ActiveMQ, Topic`，其实就是上文中`TopicProducer`发布的消息内容，如图 2-8和图 2-9。

<img src="/Users/bethanwang/myspace/Free-Academy/activemq-intro/chapter-articles/pic/2/运行TopicConsumerA控制台输出.png" style="zoom:50%;" />

​																								图 2-8

<img src="/Users/bethanwang/myspace/Free-Academy/activemq-intro/chapter-articles/pic/2/运行TopicConsumerB控制台输出.png" style="zoom:50%;" />

​																								图 2-9

### 2.3.	编程模型



















