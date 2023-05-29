package com.kobe2.escrituraauth.rmq;

import com.kobe2.escrituraauth.services.MqProducer;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class MqConfig {
    @Value("${custom.mq.virtualHost}")
    public String virtualHost;
    @Value("${custom.mq.host}")
    public String host;
    @Value("${custom.mq.username}")
    public String username;
    @Value("${custom.mq.password}")
    public String password;
    @Value("${custom.mq.queue}")
    public String queue;
    @Bean
    public Queue mqQueue() {
        return new Queue(this.queue);
    }
    @Bean
    public MqProducer sender() {
        return new MqProducer();
    }
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setVirtualHost(this.virtualHost);
        connectionFactory.setHost(this.host);
        connectionFactory.setUsername(this.username);
        connectionFactory.setPassword(this.password);
        return connectionFactory.getRabbitConnectionFactory();
    }
}
