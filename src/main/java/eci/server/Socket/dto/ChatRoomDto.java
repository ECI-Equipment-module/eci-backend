package eci.server.Socket.dto;

import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.Socket.dto.design.DesignSocketDto;
import eci.server.Socket.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.text.ParseException;
import java.util.*;

@Getter

public class ChatRoomDto {

    private String roomId;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoomDto(String roomId) {
        this.roomId = roomId;
    }

    public void handleTempActions(
            WebSocketSession session,
            ChatMessage chatMessage,
            ChatService chatService,
            NewItemRepository newItemRepository) throws ParseException {

        NewItem targetItem = newItemRepository.findById(
                chatMessage.getItemId()
        ).orElseThrow(ItemNotFoundException::new);

        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            sessions.add(session);

            chatMessage.setMessage(
                    //jsonText.MatchjsonTest()
                    //DesignSocketDto.toDto(targetItem).toString()
                    chatService.socketToJson(targetItem)
            );

            sendMessage(chatMessage, chatService);
        }
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER2)) {
            sessions.add(session);

            chatMessage.setMessage(
                    chatService.socketToJson(targetItem)
            );

            sendMessage(chatMessage, chatService);
        }


    }



    public <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }


}