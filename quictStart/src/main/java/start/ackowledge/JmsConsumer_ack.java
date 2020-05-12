package start.ackowledge;/*
@author : Administrator
@create : 2020-05-2020/5/8-21:53
    三种队列消费情况： 1个生产者 2个消费者  生成6条消息
    1.先启动生成者，然后再启动消费者  第一个消费者消费完全6  另一个消费者  消费0
    2.先启动2个消费者，然后再启动生成者  2个消费者平均消费


*/

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer_ack {

    private final  static String ACTIVEMQ_URL="tcp://192.168.77.112:61616";
    private static  String quere_name="query_ack";


    public static void main(String[] args) throws JMSException, IOException {
        consumeListener();
    }


    public static void consume_normal() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue(quere_name);
        MessageConsumer consumer = session.createConsumer(queue);
        TextMessage message=null;
        while (true){
            message = (TextMessage) consumer.receive();  //consumer.receive(4000) 4秒之后 不等了
            if( null != message){
                System.out.println(message.getText());
            }else{
                break;
            }
        }
        consumer.close();
        session.close();
        connection.close();
    }

    //消费者监听模式  consumerListener
    public static void consumeListener() throws JMSException, IOException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue(quere_name);
        MessageConsumer consumer = session.createConsumer(queue);

        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                if(null != message && message  instanceof  TextMessage){
                    TextMessage textMessage= (TextMessage) message;
                    try {
                        textMessage.acknowledge();
                        System.out.println(textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        System.in.read();  //防止还没读取 就执行下面代码而关闭了

        consumer.close();
        session.close();
        connection.close();
    }



}
