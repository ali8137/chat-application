package com.aliMezher.websocket.chat;


import com.aliMezher.websocket.chatroom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {

        var chatId = chatRoomService.getChatRoomId(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId(),
                true
        ).orElseThrow(() -> new RuntimeException("Chat room not found"));

        chatMessage.setChatId(chatId);

        repository.save(chatMessage);

        return chatMessage;
    }


//    the above method will return the list of messages in a particular chat room
//    with sender having id "senderId" and recipient having id "recipientId"
    public List<ChatMessage> findChatMessages(
            String senderId, String recipientId
    )
    {

        var chatId = chatRoomService.getChatRoomId(
                senderId,
                recipientId,
                false);

        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }


}
