package com.foo.study.activemq.pubsub;

import com.foo.study.activemq.constant.Broker;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * topic，消息订阅者B
 */
public class TopicConsumerB {

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
                            System.out.println("TopicConsumerB 接收到消息：" + text);
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
