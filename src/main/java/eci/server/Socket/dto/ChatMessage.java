package eci.server.Socket.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Getter
@Setter
public class ChatMessage {
    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK
    }
    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String message; // 메시지

    private List<Integer> itemNumber;
}