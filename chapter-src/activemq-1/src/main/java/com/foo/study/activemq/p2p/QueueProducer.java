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
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Broker.TEST_BROKER_URL);
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