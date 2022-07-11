//package eci.server.Socket.controller;
//
//import eci.server.ItemModule.dto.response.Response;
//import eci.server.Socket.dto.CreateRoomRequest;
//import eci.server.aop.AssignMemberId;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/chat")
//
//public class ChatRoomController {
//    private final ChatRoomRepository chatRoomRepository;
//    // 채팅 리스트 화면
//    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
//    @GetMapping("/room")
//    public Response rooms(
//            @PathVariable String id
//    ) {
//        return Response.success(
//                chatRoomRepository.findRoomById(id)
//
//        );
//    }
//    // 모든 채팅방 목록 반환
//    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
//    @GetMapping("/rooms")
//    @ResponseBody
//    public Response room() {
//        return Response.success(
//                chatRoomRepository.findAllRoom()
//        );
//    }
//    // 채팅방 생성
//    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
//    @PostMapping("/room")
//    @ResponseStatus(HttpStatus.CREATED)
//    @ResponseBody
//    @AssignMemberId
//    public Response createRoom(@Valid @ModelAttribute
//             CreateRoomRequest req) {
//        return Response.success(
//                chatRoomRepository.createChatRoom(req.getName())
//        );
//    }
//    // 채팅방 입장 화면
//    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
//    @GetMapping("/room/enter")
//    public Response roomDetail(
//            @PathVariable String roomId) {
//
//        return Response.success(
//           chatRoomRepository.findRoomById(roomId)
//        );
//    }
//
//}