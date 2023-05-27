package com.kobe2.escrituraauth.rmq;

import com.kobe2.escrituraauth.services.MqProducer;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Config {
    private final MqProps mqProps;
    @Bean
    public Queue queue() {
        return new Queue(mqProps.queue);
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
        connectionFactory.setVirtualHost(mqProps.virtualHost);
        connectionFactory.setHost(mqProps.host);
        connectionFactory.setUsername(mqProps.username);
        connectionFactory.setPassword(mqProps.password);
        return connectionFactory.getRabbitConnectionFactory();
    }
}
