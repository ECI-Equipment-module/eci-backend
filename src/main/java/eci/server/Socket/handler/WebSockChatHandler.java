package eci.server.Socket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import eci.server.Socket.dto.ChatMessage;
import eci.server.Socket.dto.ChatRoomDto;
import eci.server.Socket.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 웹소켓 클라이언트로부터 채팅 메시지를 전달받아 채팅 메시지 객체로 변환
 * 전달받은 메시지에 담긴 채팅방 Id로 발송 대상 채팅방 정보를 조회함
 * 해당 채팅방에 입장해있는 모든 클라이언트들(Websocket session)에게 타입에 따른 메시지 발송
 */
    @Slf4j
    @RequiredArgsConstructor
    @Component
    public class WebSockChatHandler extends TextWebSocketHandler {
        private final ObjectMapper objectMapper;
        private final ChatService chatService;

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            String payload = message.getPayload();
            log.info("payload {}", payload);
            ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
            ChatRoomDto room = chatService.findRoomById(chatMessage.getRoomId());
            room.handleActions(session, chatMessage, chatService);
            room.loading(chatService);
            room.handleActions2(session, chatMessage, chatService);
        }
    }
