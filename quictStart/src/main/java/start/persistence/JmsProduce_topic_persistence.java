package start.persistence;/*
@author : Administrator
@create : 2020-05-2020/5/8-21:21
 持久化subscriber，只要这个subscriber 注册过了并且是持化的，当此subscriber 离线时，再登录后，仍然可以收到
                   其离线时 发送的topic
*/

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduce_topic_persistence {

    private final  static  String ACTIVEMQ_URL="tcp://192.168.77.112:61616";
    private static String topic_name="topic_persistence1";

    public static void main(String[] args) throws JMSException {

        //创建connectionfactory
        Connection connection=null;
        Session session =null;
        MessageProducer producer=null;
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
            connection = activeMQConnectionFactory.createConnection();
            //第一个参数 事务，第二个参数 签收
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topic_name);
            producer = session.createProducer(topic);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            connection.start();  //创建持久化后  再打开

            for (int i = 1; i <=3 ; i++) {
                TextMessage textMessage = session.createTextMessage("主题\t" + i);
                producer.send(textMessage);
            }
            System.out.println(topic+"**********发送成功");
            
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            producer.close();
            session.close();
            connection.close();
        }


    }

}
