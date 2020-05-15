# activeMqLearn
    activeMq 命令 
     activemq start \ activemq restart   带日志的启动  activemq start  > run_activemq.log
     activemq stop 
    默认端口 61616  前端访问时 url地址:8161 默认用户名和密码 都是admin
   
   
    协议配置：1.使用nio协议要表现更好  在activemq.xml 中配置  
           ` <transportConnector name="nio" uri="nio://0.0.0.0:61616?trace=true"/>`
        2.配置 auto+nio  可以适配多种协议  当然不同的协议发送代码的客户端是不同的  nio、tcp目前是相同的
          `<transportConnector name="auto+nio" uri="auto+nio://0.0.0.0:61608?maximumConnections=1000&amp;
                          wireFormat.maxFrameSize=104857600&amp;
                          org.apache.activemq.transport.nio.SelectorManager.corePoolSize=20&amp;
                          org.apache.activemq.transport.nio.SelectorManager.maximumPoolSize=50" />`
        当客户端使用不同的协议和端口 就会调用相应的协议
        
        
    消息的持久化
      kahadb
        默认是kahadb 在activemq.xml中有配置  原理是存储事物日志，保存在data-1.log 日志文件中，当文件满时，会再次创建
        data-2.log ，依次添加日志文件 db.data 存放的索引(btree)，指向的是data-*.log文件    
      leveldb
      jdbc
        1.mysql-conectorjar包  放在activemq 的lib中
        2.设置mysql配置   在broker之外 在bean内进行配置 
         ` </broker> 
          <!-- MySql DataSource Sample Setup --> 
          <!-- 
          <bean id="mysql-ds" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close"> 
            <property name="driverClassName" value="com.mysql.jdbc.Driver"/> 
            <property name="url" value="jdbc:mysql://localhost/activemq?relaxAutoCommit=true"/> 
            <property name="username" value="activemq"/> 
            <property name="password" value="activemq"/> 
            <property name="poolPreparedStatements" value="true"/> 
          </bean> `
        3.配置使用jdbc 持久化
            `<persistenceAdapter> 
                <jdbcPersistenceAdapter dataSource="#my-ds"/> 
              </persistenceAdapter>`
            配置出现的问题
            连接mysql 配置连接权限      给予 myuser访问192.168.77.1 database数据库 任意权限
                `1、GRANT ALL PRIVILEGES ON database.* TO 'myuser'@'192.168.77.1' IDENTIFIED BY 'mypassword' WITH GRANT OPTION; 
                2.FLUSH   PRIVILEGES; 跟新`
                
             有三张表
             activemq_msgs 
                队列信息和topic 信息都会存在这  不同的是  当消费者消费之后 ，队列的信息都移除了，消费者的消息不会被移除
             activemq_acks
                订阅者订阅的消息
             activemq_lock
                锁表 不需要关注
       jdbc with journal    
            相比较于jdbc而言，性能更好。jdbc 要经常性的io操作数据库，而journal相当于一个缓存，收到一个生成者信息之后存入
         journal中，此时雨果消费者来消费直接从journal中获取即可，不需要操作mysql 数据库。万一消费者没有消费完jouranl中的
         信息，则待一段时间过去之后可以批量插入数据库。
            
        `<journalPersistenceAdapterFactory 
                         journalLogFiles="5"
                          dataDirectory="${basedir}/activemq-data" 
                          dataSource="#mysql-ds"
         />` 
        activemq的高可用性   
        activemq+zookeeper+levelDB
          客户端只能连接到 activemq 主服务器，从服务器只是同步主服务器，在主服务器挂的时候，选举成为主服务器。 
          具体配置如下：
          `https://blog.csdn.net/zyjavaWeb/article/details/79209572`
       异步投递
            mq默认是异步投递，除了设置为同步投递的和 没有使用事物但持久化的为同步发送消息
            同步投递在 slower consumer 中就会阻塞降低程序反应时间，从而对于客户而言不那么
            体验友好。因此 设置异步投递   
             `activeMQConnectionFactory.setUseAsyncSend(true);`
          问题：异步投递  万一mq宕机了 怎么确认mq 确实已经收到了呢？
                回调方法
                
       延时投递和定时投递
       1.添加 
           `schedulerSupport="true"
           <broker xmlns="http://activemq.apache.org/schema/core" 
           brokerName="localhost" dataDirectory="${ activemq. data}" schedulerSupport="true" />`
       2.配置delay period repeat
            ` message.setLongProperty(ScheduledMessage.AMO_SCHEDULED_DELAY, delay);
             message.setLongProperty(ScheduledMes sage.AMO_SCHEDULED_PERIOD, period);
             message.setIntProperty(ScheduledMessage.AMO_SCHEDULED_REPEAT, repeat);`
       3.重复消费(重发)的问题
             消费端开启事务  但是没有session.commit() 或者 已经rollback了
             默认 重发6次之后就会 进入死信队列中 不在重发
       4.死信队列的配置介绍
        1、SharedDeadLetterStrategy
            将所有的DeadLetter保存在一个共享的队列中，这是 ActiveMQ broker 端默认的策略。
            共享队列默认为 “ActiveMQ.DLQ”，可以通过 “deadLetterQueue” 属性来设定。
        2、IndividualDeadLetterStrategy
           1. 把DeadLetter放入各自的死信通道中，
              对于Queue而言，死信通道的前缀默认为“ActiveMQ.DLQ.Queue.”；
              对于Topic而言，死信通道的前缀默认为“ActiveMQ.DLQ.Topic.”；
              比如队列Order，那么它对应的死信通道为“ActiveMQ.DLQ.Queue.Order”，
              这里使用“queuePrefix”“topicPrefix”来指定上述前缀。
              默认情况下，无论是Topic还是Queue，broker将使用Queue来保存DeadLeader，即死信通道通常为Queue；
              不过你也可以指定为Topic。
            2.
             `<policyEntry queue="order">
                  <deadLetterStrategy>
                     <individualDeadLetterStrategy queuePrefix="DLQ."useQueueForQueueMessages="false"/>
                  </deadLetterStrategy>
              </policyEntry>`
              将队列Order中出现的DeadLetter保存在DLQ.Order中，不过此时DLQ.Order为Topic。
              属性“useQueueForTopicMessages”，此值表示是否将Topic的DeadLetter保存在Queue中，默认为true。
        3、配置案例
            自动删除过期消息
                有时需要直接删除过期的消息而不需要发送到死队列中，
                “processExpired”表示是否将过期消息放入死信队列，默认为true；
                `<policyEntry queue=">">
                    <deadLetterStrategy>
                        <sharedDeadLetterStrategy processExpired="false"/>
                    </deadLetterStrategy>
                </policyEntry>`
            存放非持久消息到死信队列中
                默认情况下，Activemq 不会把非持久的死消息发送到死信队列中
                processNonPersistent”表示是否将“非持久化”消息放入死信队列，默认为false。
                非持久性如果你想把非持久的消息发送到死信队列中，需要设置属性processNonPersistent=“true”
                `<policyEntry queue=">">
                    <deadLetterStrategy>
                        <sharedDeadLetterStrategy processExpired="true"/>
                    </deadLetterStrategy>
                </policyEntry>`
          5、如何保证消息不被重复消费呢？也即消息幂等性问题
            网络延迟传输中，会造成进行MQ重试中，在重试过程中，可能会造成重复消费。
            如果消息是做数据库的插入操作，给这个消息做一个唯一主键，那么就算出现重复消费的情况，就会导致主键冲突，
            避免数据库出现脏数据。
            如果上面两种情况还不行，准备一个第三服务方来做消费记录。以redis为例，给消息分配一个全局id，
            只要消费过该消息，将<id，message>以K-V形式写入redis。那消费者开始消费前，先去redis中查询有没消费记录即可