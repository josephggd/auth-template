package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.rmq.Message;
import com.kobe2.escrituraauth.rmq.MqProps;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class MqProducer {
    private static final Logger logger = LoggerFactory.getLogger(MqProducer.class);
    private final RabbitTemplate rabbitTemplate;
    private final MqProps mqProps;
    public void sendMessage(Message message){
        logger.info(String.format("Message sent -> %s", message.email()));
        rabbitTemplate.convertAndSend(mqProps.topic, mqProps.key, message);
    }
}
