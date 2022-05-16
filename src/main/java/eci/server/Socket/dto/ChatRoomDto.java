package eci.server.Socket.dto;

import eci.server.Socket.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Getter
public class ChatRoomDto {

    private String roomId;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoomDto(String roomId) {
        this.roomId = roomId;
    }

    public void handleTempActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) throws ParseException {

        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            sessions.add(session);

            chatMessage.setMessage(
                    jsonText.MatchjsonTest()
            );

            sendMessage(chatMessage, chatService);
        }

    }
//
//    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
//
//        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
//            sessions.add(session);
//            chatMessage.setMessage("디자인 파일 검사 결과를 요청했습니다.");
//            sendMessage(chatMessage, chatService);
//        }
//
//    }
//
//    public void loading(ChatService chatService) throws InterruptedException {
//        for (int i = 0; i < 3; i++) {
//            TimeUnit.SECONDS.sleep(1);
//            sendMessage((i+1), chatService);
//        }
//    }
//
//    public void handleActions2(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
//
//        List<Integer> itemNumbers = new ArrayList<>() ;
//
//        itemNumbers.add(1);
//        itemNumbers.add(2);
//
//        List<ItemNum> itemNumList = new ArrayList<>();
//        ItemNum itemNum = new ItemNum();
//
//        for(Integer itemNumber : itemNumbers){
//            ItemNum itemNum1 = new ItemNum(itemNumber, true);
//            itemNumList.add(itemNum1);
//        }
//        chatMessage.setMessage(
//                String.valueOf(itemNumList)
//        );
//        sendMessage(chatMessage, chatService);
//    }

    public <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }


}