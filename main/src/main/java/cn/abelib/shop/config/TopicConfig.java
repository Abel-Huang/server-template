package cn.abelib.shop.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 *
 * @author abel
 * @date 2018/4/5
 */
@Configuration
public class TopicConfig {
    @Bean
    public Queue queue(){
        return new ActiveMQQueue("shopping.queue");
    }

    @Bean
    public Topic productTopic(){
        return new ActiveMQTopic("shopping.topic");
    }
}
