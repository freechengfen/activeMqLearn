package start.persistence;/*
@author : Administrator
@create : 2020-05-2020/5/8-21:53
        1.消费者要先于生成者启动，要不然不能消费生产的信息
        2.消费者完全的消费了生成者的消息

*/

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer_topic_persistence {

    private final  static String ACTIVEMQ_URL="tcp://192.168.77.112:61616";
    private static  String topic_name="topic_persistence1";


    public static void main(String[] args) throws JMSException, IOException {
        consumeListener();

    }



    //消费者监听模式  consumerListener
    public static void consumeListener() throws JMSException, IOException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.setClientID("z1");
        System.out.println("==============消费者2");
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(topic_name);
        TopicSubscriber subscriber = session.createDurableSubscriber(topic, "remark");
        connection.start();  //创建持久化订阅者再打开
        TextMessage message = (TextMessage) subscriber.receive();

        while(null != message){
            System.out.println(message);
            message= (TextMessage) subscriber.receive(1000);
        }

        session.close();
        connection.close();
    }



}
