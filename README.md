# activeMqLearn
   activeMq 命令 
     activemq start \ activemq restart   带日志的启动  activemq start  > run_activemq.log
     activemq stop 
   默认端口 61616  前端访问时 url地址:8161 默认用户名和密码 都是admin
   
   
   协议配置：1.使用nio协议要表现更好  在activemq.xml 中配置  
            <transportConnector name="nio" uri="nio://0.0.0.0:61616?trace=true"/>
        2.配置 auto+nio  可以适配多种协议  当然不同的协议发送代码的客户端是不同的  nio、tcp目前是相同的
          <transportConnector name="auto+nio" uri="auto+nio://0.0.0.0:61608?maximumConnections=1000&amp;
                          wireFormat.maxFrameSize=104857600&amp;
                          org.apache.activemq.transport.nio.SelectorManager.corePoolSize=20&amp;
                          org.apache.activemq.transport.nio.SelectorManager.maximumPoolSize=50" />
                          
        当客户端使用不同的协议和端口 就会调用相应的协议
        
        
   消息的持久化
      kahadb
        默认是kahadb 在activemq.xml中有配置  原理是存储事物日志，保存在data-1.log 日志文件中，当文件满时，会再次创建
        data-2.log ，依次添加日志文件 db.data 存放的索引(btree)，指向的是data-*.log文件    
      leveldb
      jdbc
        1.mysql-conectorjar包  放在activemq 的lib中
        2.设置mysql配置   在broker之外 在bean内进行配置 
          </broker> 
          <!-- MySql DataSource Sample Setup --> 
          <!-- 
          <bean id="mysql-ds" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close"> 
            <property name="driverClassName" value="com.mysql.jdbc.Driver"/> 
            <property name="url" value="jdbc:mysql://localhost/activemq?relaxAutoCommit=true"/> 
            <property name="username" value="activemq"/> 
            <property name="password" value="activemq"/> 
            <property name="poolPreparedStatements" value="true"/> 
          </bean> 
        3.配置使用jdbc 持久化
            <persistenceAdapter> 
                <jdbcPersistenceAdapter dataSource="#my-ds"/> 
              </persistenceAdapter>
            配置出现的问题
            连接mysql 配置连接权限      给予 myuser访问192.168.77.1 database数据库 任意权限
                1、GRANT ALL PRIVILEGES ON database.* TO 'myuser'@'192.168.77.1' IDENTIFIED BY 'mypassword' WITH GRANT OPTION; 
                2.FLUSH   PRIVILEGES; 跟新
                
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
            
        <!-- <journalPersistenceAdapterFactory 
                         journalLogFiles="5"
                          dataDirectory="${basedir}/activemq-data" 
                          dataSource="#mysql-ds"
         /> --> 
         
        activemq+zookeeper+levelDB
         https://blog.csdn.net/zyjavaWeb/article/details/79209572
                
         
                