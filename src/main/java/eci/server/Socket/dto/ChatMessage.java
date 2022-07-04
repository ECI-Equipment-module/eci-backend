package eci.server.Socket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, ENTER2
    }
    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String message; // 메시지
    private Long itemId;
}
