package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.ConfirmationToken;
import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.enums.CodePurpose;
import com.kobe2.escrituraauth.rmq.Message;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class MqProducer {
    private static final Logger logger = LoggerFactory.getLogger(MqProducer.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Queue queue;
    private void sendMessage(CodePurpose purpose, EscrituraUser user, ConfirmationToken token){
        Message message = new Message(
                user.getEmail(),
                purpose,
                token.getCode().toString()
        );
        try {
            rabbitTemplate.convertAndSend(queue.getName(), message);
        } catch (Exception e) {
            logger.warn(String.format("Message NOT sent -> %s", message.email()));
        }
    }
    public void sendConfirmationCode(EscrituraUser user, ConfirmationToken token) {
        this.sendMessage(CodePurpose.CONFIRMATION, user, token);
    }
}
