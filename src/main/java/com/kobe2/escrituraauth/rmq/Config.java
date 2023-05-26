package com.kobe2.escrituraauth.rmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    private final MqProps mqProps;

    public Config(MqProps mqProps) {
        this.mqProps = mqProps;
    }

    @Bean
    public AMQP.Queue queue() {
        return new AMQP.Queue();
    }
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(mqProps.topic);
    }
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(mqProps.key);
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
