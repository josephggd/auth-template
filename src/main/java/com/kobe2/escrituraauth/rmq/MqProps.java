package com.kobe2.escrituraauth.rmq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqProps {
    @Value("custom.mq.topic")
    public String topic;
    @Value("custom.mq.virtualHost")
    public String virtualHost;
    @Value("custom.mq.host")
    public String host;
    @Value("custom.mq.username")
    public String username;
    @Value("custom.mq.password")
    public String password;
    @Value("custom.mq.key")
    public String key;
    @Value("custom.mq.queue")
    public String queue;
}
