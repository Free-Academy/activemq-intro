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
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Broker.TEST_BROKER_URL);
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