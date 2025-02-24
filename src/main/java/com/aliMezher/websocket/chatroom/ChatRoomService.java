package com.aliMezher.websocket.chatroom;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private static final Logger log = LoggerFactory.getLogger(ChatRoomService.class);
    private final ChatRoomRepository chatRoomRepository;

//    the above method gets the chatId of a pair of chat rooms (the chat rooms of this pair
//    have the same chatters) from the database. and if this pair of chat rooms is not
//    present in the database, then this method will create a new pair of chat rooms
    public Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists
    )
    {

        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, recipientId);

                        return Optional.of(chatId);
                    }

                    return Optional.empty();
                });
    }

//    this method create a new pair of chat rooms and saves them to the database
    private String createChatId(String senderId, String recipientId) {

        var chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom.builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }
}
