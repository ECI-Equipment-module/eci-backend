package eci.server.Socket.controller;

import eci.server.Socket.dto.ChatRoomDto;
import eci.server.Socket.service.ChatService;
import eci.server.aop.AssignMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/design-file")
public class ChatController {

    private final ChatService chatService;
    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping
    @AssignMemberId
    //아무것도 없이 방만 생성 가능
    public ChatRoomDto createRoom(
    ) {
        return chatService.createRoom();
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping
    public List<ChatRoomDto> findAllRoom() {
        return chatService.findAllRoom();
    }
}
