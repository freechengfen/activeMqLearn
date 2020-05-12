package activemq.springdemo.quickstart;/*
@author : Administrator
@create : 2020-05-2020/5/11-21:57

*/

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Component
public class JmsConsumer {

    @JmsListener(destination = "${myDestination}")
    public void   receive(TextMessage textMessage){
        try {
            System.out.println("receive:"+textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


}
