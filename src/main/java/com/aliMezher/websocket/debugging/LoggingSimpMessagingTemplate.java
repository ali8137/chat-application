package com.aliMezher.websocket.debugging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.core.MessagePostProcessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

public class LoggingSimpMessagingTemplate extends SimpMessagingTemplate {

    private static final Logger logger = LoggerFactory.getLogger(LoggingSimpMessagingTemplate.class);
//    the above is just a logger that logs/prints information we want in the console.
//    this logger is used to print the destination/endpoint of each message after processing it and sending it by the backend server to the frontend server

    public LoggingSimpMessagingTemplate(MessageChannel messageChannel) {
        super(messageChannel);
    }

    @Override
    public void convertAndSendToUser(String user, String destination, Object payload,
                                     @Nullable Map<String, Object> headers, @Nullable MessagePostProcessor postProcessor)
            throws MessagingException
//    we are just overriding the method "public void convertAndSendToUser(String user, String destination, Object payload, @Nullable Map<String, Object> headers, @Nullable MessagePostProcessor postProcessor)" of the class "SimpMessagingTemplate" to add to it the ability to print the destination/endpoint of each message after processing it and sending it by the backend server to the frontend server
    {

        super.convertAndSendToUser(user, destination, payload, headers, postProcessor);

        logger.info("Sending message to destination: {}", super.getUserDestinationPrefix() + user + destination);
    }
}

