package io.github.angelogalvao.example.csb;

import org.apache.camel.component.jms.JmsComponent;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.ibm.mq.jakarta.jms.MQConnectionFactory;
import com.ibm.msg.client.jakarta.wmq.WMQConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExampleConfig {
    
    @Value("${mq.host}")
    private String mqHost;

    @Value("${mq.port}")
    private int mqPort;

    @Value("${mq.qm}")
    private String mqQueueManager;

    @Value("${mq.channel}")
    private String mqChannel;

    @Value("${mq.user}")
    private String user;

    @Value("${mq.password}")
    private String password;

    /**
     * Create a connection factory to IBM MQ broker.
     * @return
     */
    private MQConnectionFactory createMQConnectionFactory() {

        log.info("Creating the MQConnectionFactory");
        MQConnectionFactory mqQueueConnectionFactory = new MQConnectionFactory();
        try {
            mqQueueConnectionFactory.setHostName(mqHost);
            mqQueueConnectionFactory.setChannel(mqChannel);
            mqQueueConnectionFactory.setPort(mqPort);
            mqQueueConnectionFactory.setQueueManager(mqQueueManager);
            mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            mqQueueConnectionFactory.setStringProperty(WMQConstants.USERID, user);
            mqQueueConnectionFactory.setStringProperty(WMQConstants.PASSWORD, password);
        } catch (Exception e) {
            log.error("Error creating the MQConnectionFactory", e);
            throw new RuntimeException(e);
        }
        return mqQueueConnectionFactory;
    }

    /**
     * Create a Pooled Connection Factory to IBM MQ broker. 
     * @return the pooled connection factory
     */
    @Bean
    public JmsPoolConnectionFactory jmsPoolConnectionFactory() {

        log.info("Creating the JmsPoolConnectionFactory");
        JmsPoolConnectionFactory jmsPoolConnectionFactory = new JmsPoolConnectionFactory();
        jmsPoolConnectionFactory.setConnectionFactory(createMQConnectionFactory());
        jmsPoolConnectionFactory.setMaxConnections(1);
        return jmsPoolConnectionFactory;
    }

    @Bean(name="jms")
    public JmsComponent ibmMqComponent(JmsPoolConnectionFactory jmsPoolConnectionFactory) {

        log.info("Creating the JmsComponent that connects to IBM MQ broker");
        JmsComponent jms = new JmsComponent();
        jms.setConnectionFactory(jmsPoolConnectionFactory);
        jms.setCacheLevelName("CACHE_CONSUMER");
        jms.setTransacted(true);
        return jms;
    }
}
