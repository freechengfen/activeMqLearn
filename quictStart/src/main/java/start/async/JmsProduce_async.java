package start.async;/*
@author : Administrator
@create : 2020-05-2020/5/8-21:21

  1.activeMQConnectionFactory.setSendAcksAsync(true);  //设置同步发送
  2.使用ActiveMQMessageProducer
        ActiveMQMessageProducer activeMQMessageProducer =
        (ActiveMQMessageProducer) session.createProducer(destination);
  3.使用回调方法
   activeMQMessageProducer.send(textMessage, new AsyncCallback() {
                            public void onSuccess() {
                                System.out.println(jmsMessageID+":发送成功");
                            }
                            public void onException(JMSException e) {
                                System.out.println(jmsMessageID+":发送失败");
                            }
                        }
                );

*/

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.AsyncCallback;

import javax.jms.*;
import java.util.UUID;

public class JmsProduce_async {

    private final  static  String ACTIVEMQ_URL="tcp://192.168.77.112:61616";
    private static String quere_name="query1";

    public static void main(String[] args) throws JMSException {

        //创建connectionfactory
        Connection connection=null;
        Session session =null;
        MessageProducer producer=null;
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
            activeMQConnectionFactory.setSendAcksAsync(true);  //设置同步发送
            connection = activeMQConnectionFactory.createConnection();
            connection.start();
             session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            
            Destination destination = session.createQueue(quere_name);
             ActiveMQMessageProducer activeMQMessageProducer = (ActiveMQMessageProducer) session.createProducer(destination);

            for (int i = 1; i <=3 ; i++) {
                TextMessage textMessage = session.createTextMessage("队列\t" + i);  //有多种形式的message 常用的是 TextMessage\mapMessage
                textMessage.setJMSMessageID(UUID.randomUUID().toString()+"async");
                final String jmsMessageID = textMessage.getJMSMessageID();
                activeMQMessageProducer.send(textMessage, new AsyncCallback() {
                            public void onSuccess() {
                                System.out.println(jmsMessageID+":发送成功");
                            }
                            public void onException(JMSException e) {
                                System.out.println(jmsMessageID+":发送失败");
                            }
                        }
                );
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            producer.close();
            session.close();
            connection.close();
        }


    }

}
