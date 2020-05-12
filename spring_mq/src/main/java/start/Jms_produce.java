package start;/*
@author : Administrator
@create : 2020-05-2020/5/9-22:13

*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

@Service
public class Jms_produce {
    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Jms_produce jms_produce = (Jms_produce) context.getBean("jms_produce");
        jms_produce.jmsTemplate.send((session)->{
            TextMessage textMessage = session.createTextMessage("spring-queue");
            return textMessage;
        });
        System.out.println("task send over");
    }

}
