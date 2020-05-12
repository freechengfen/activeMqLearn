package start;/*
@author : Administrator
@create : 2020-05-2020/5/9-22:13
     类上@service 之后  ac.getbean("类名(第一个字母小写)")
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

@Service
public class Jms_consumer {
    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Jms_consumer consumer = (Jms_consumer) context.getBean("jms_consumer");
        String message = (String)consumer.jmsTemplate.receiveAndConvert();
        System.out.println(message);
    }

}
