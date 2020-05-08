package start;/*
@author : Administrator
@create : 2020-05-2020/5/8-21:21

*/

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduce {

    private final  static  String ACTIVEMQ_URL="tcp://192.168.77.112:61616";
    private static String quere_name="query1";

    public static void main(String[] args) throws JMSException {

        //创建connectionfactory
        Connection connection=null;
        Session session =null;
        MessageProducer producer=null;
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
            connection = activeMQConnectionFactory.createConnection();
            connection.start();
            //第一个参数 事务，第二个参数 签收
             session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            
            Destination destination = session.createQueue(quere_name);
             producer = session.createProducer(destination);

            for (int i = 1; i <=3 ; i++) {
                TextMessage textMessage = session.createTextMessage("队列\t" + i);
                producer.send(textMessage);
            }
            System.out.println("**********发送成功");
            
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            producer.close();
            session.close();
            connection.close();
        }


    }

}
