package com.aliMezher.websocket.debugging;


import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomSubscriptionInterceptor implements ChannelInterceptor
//this class is to debug the subscription and find when each user logs, what web socket endpoint he subscribed to
{

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel)
//    i think we can add @Nullable annotation before the above two parameters to avoid null pointer exception
    {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String sessionId = accessor.getSessionId();
            String destination = accessor.getDestination();
            log.info("User with sessionId: {} ", sessionId);
            log.info(" subscribed to: {}", destination);
        }

        return message;
    }
}





//import org.springframework.messaging.Message;
//import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler;
//import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
//import org.springframework.messaging.simp.broker.SubscriptionRegistry;
//import org.springframework.messaging.simp.user.SimpUserRegistry;
//import org.springframework.messaging.simp.user.SimpUser;
//import org.springframework.messaging.simp.user.SimpUserSubscription;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;
//
//import java.util.Set;
//
//@Component
//public class CustomWebSocketHandler extends SimpAnnotationMethodMessageHandler {
//
//    private final SubscriptionRegistry subscriptionRegistry;
//
//    public CustomWebSocketHandler(SubProtocolWebSocketHandler webSocketHandler, SimpUserRegistry userRegistry, SubscriptionRegistry subscriptionRegistry) {
//        super(webSocketHandler, userRegistry);
//        this.subscriptionRegistry = subscriptionRegistry;
//    }
//
//    @Override
//    public void handleSubscribe(Message<?> message) {
//        super.handleSubscribe(message);
//        String sessionId = (String) message.getHeaders().get("simpSessionId");
//        String destination = (String) message.getHeaders().get("simpDestination");
//        System.out.println("User with sessionId: " + sessionId + " subscribed to: " + destination);
//    }
//
//    public void logSubscriptions() {
//        for (SimpUser user : this.subscriptionRegistry.getUsers()) {
//            System.out.println("User: " + user.getName());
//            Set<SimpUserSubscription> subscriptions = user.getSubscriptions();
//            for (SimpUserSubscription subscription : subscriptions) {
//                System.out.println("Subscription: " + subscription.getDestination());
//            }
//        }
//    }
//
//    @Override
//    public void onApplicationEvent(BrokerAvailabilityEvent event) {
//        // Handle broker availability event if needed
//    }
//}
//
