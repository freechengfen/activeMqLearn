package activemq.springdemo.config;/*
@author : Administrator
@create : 2020-05-2020/5/11-14:54

*/

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Component;

import javax.jms.Queue;


@Component
@EnableJms
public class ActiveMqConfig {

   @Value("${myDestination}")
    public String destination;

    @Bean
    public Queue getQueue(){
        return new ActiveMQQueue(destination);
    }


}
