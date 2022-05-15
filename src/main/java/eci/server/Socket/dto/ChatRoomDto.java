package eci.server.Socket.dto;

import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.Socket.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Getter
public class ChatRoomDto {

    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoomDto(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }
    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {

        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 디자인 파일 검사 결과를 요청했습니다.");
            sendMessage(chatMessage, chatService);
        }

    }

    public void loading(ChatService chatService) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            TimeUnit.SECONDS.sleep(1);
            sendMessage((i+1), chatService);
        }
    }

    public void handleActions2(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {

        List<Integer> itemNumbers = chatMessage.getItemNumber();
        List<ItemNum> itemNumList = new ArrayList<>();
        ItemNum itemNum = new ItemNum();

        for(Integer itemNumber : itemNumbers){
            ItemNum itemNum1 = new ItemNum(itemNumber, true);
            itemNumList.add(itemNum1);
        }
        chatMessage.setMessage(
                String.valueOf(itemNumList)
        );
        sendMessage(chatMessage, chatService);
    }

    public <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }
}