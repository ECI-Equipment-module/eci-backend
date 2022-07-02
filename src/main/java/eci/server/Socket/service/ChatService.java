package eci.server.Socket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.Socket.dto.ChatRoomDto;
import eci.server.Socket.dto.design.DesignSocketDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoomDto> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoomDto> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoomDto findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoomDto createRoom() {
        String randomId = UUID.randomUUID().toString();
        ChatRoomDto chatRoom = ChatRoomDto.builder()
                .roomId(randomId)
                .build();
        chatRooms.put(randomId, chatRoom);

        return chatRoom;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public String socketToJson(NewItem newItem){

        DesignSocketDto dto = DesignSocketDto.toDto(newItem);
        Gson gson = new Gson();
        String json = gson.toJson(dto);

        return json;
    }
}