package com.aliMezher.websocket.debugging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebSocketSessionLogger
//added this class to debug some issue. this class will log/print all the active sessions of all the users in websocket. it will be injected as a bean into the class "ChatController"
//    I don't know though why the logged statements are not displayed/gotten in the console
{

    @Autowired
    private SimpUserRegistry simpUserRegistry;
//    SimpUserRegistry: This is a Spring-provided interface that keeps track of connected users and their sessions. It allows you to query for active users and their session information.



    public void logActiveSessions()
//            this method will log/print all the active sessions of all the users
    {
        log.info("okay man, relax");

        simpUserRegistry.getUsers().forEach(user -> {
            log.info("User: {}", user.getName());

            user.getSessions().forEach(session -> {
                log.info("Session: {}", session.getId());
            });
        });
    }

//    public Map<String,List<String>> logActiveSessions()
////            this method will log/print all the active sessions of all the users
//    {
//        Map<String,List<String>> userSessions = new HashMap<>();
//
//        simpUserRegistry.getUsers().forEach(user -> {
////            log.info("User: {}", user.getName());
//
//            List<String> sessions = new ArrayList<>();
//
//
//            user.getSessions().forEach(session -> {
////                log.info("Session: {}", session.getId());
//
//                sessions.add(session.getId());
//
//            });
//
//            userSessions.put(user.getName(),sessions);
//        });
//
//        return userSessions;
//    }

}

