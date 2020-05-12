package activemq.springdemo.quickstart;/*
@author : Administrator
@create : 2020-05-2020/5/11-15:05

*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Queue;
import java.util.UUID;

@Component
public class JmsProduce {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Queue queue;

    public void sendTest() throws JMSException {
        jmsMessagingTemplate.convertAndSend(queue, UUID.randomUUID().toString().substring(0,6));
    }

    @Scheduled(fixedDelay = 3000)
    public void sendSchedule() throws JMSException {
        jmsMessagingTemplate.convertAndSend(queue, UUID.randomUUID().toString().substring(0,6));
    }

}
