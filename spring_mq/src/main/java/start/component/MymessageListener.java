package start.component;/*
@author : Administrator
@create : 2020-05-2020/5/9-23:49

*/

import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
public class MymessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
       TextMessage textMessage= (TextMessage) message;
        try {
            System.out.println("message:"+textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
