package start;/*
@author : Administrator
@create : 2020-05-2020/5/9-19:26
创建broker
*/

import org.apache.activemq.broker.BrokerService;

public class EmbedBrokder {


    public static void main(String[] args) {
        BrokerService brokerService = new BrokerService();
        brokerService.setUseJmx(true);
        try {
            brokerService.addConnector("tcp://localhost:61616");
            brokerService.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
