package swm.hkcc.chat.app.modules.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import swm.hkcc.chat.app.modules.chat.model.ChatDto;
import swm.hkcc.chat.app.modules.chat.service.ChatProducer;
import swm.hkcc.chat.app.modules.chat.service.ChatService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatProducer chatProducer;
    private final ChatService chatService;

    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatDto chat, SimpMessageHeaderAccessor headerAccessor) {
        chatService.plusUserCnt(chat.getRoomId());

        String userUUID = chatService.addUser(chat.getRoomId(), chat.getSender());

        headerAccessor.getSessionAttributes().put("userUUID", userUUID);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

        chat.setMessage(chat.getSender() + " 님 입장!!");
        chatProducer.sendChatMessage(chat);
//        template.convertAndSend("/sub/chatroom/detail/" + chat.getRoomId(), chat);
    }

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatDto chat) {
        log.info("CHAT {}", chat);
        chat.setMessage(chat.getMessage());
        chatProducer.sendChatMessage(chat);
//        template.convertAndSend("/sub/chatroom/detail/" + chat.getRoomId(), chat);

    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("DisConnEvent {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        log.info("headAccessor {}", headerAccessor);

        chatService.minusUserCnt(roomId);

        String username = chatService.getUserName(roomId, userUUID);
        chatService.delUser(roomId, userUUID);

        if (username != null) {
            log.info("User Disconnected : " + username);

            ChatDto chat = ChatDto.builder()
                    .type(ChatDto.MessageType.LEAVE)
                    .sender(username)
                    .message(username + " 님 퇴장!!")
                    .build();

            chatProducer.sendChatMessage(chat);
            // template.convertAndSend("/sub/chatroom/detail/" + roomId, chat);
        }
    }

    @GetMapping("/chat/userlist")
    @ResponseBody
    public List<String> userList(String roomId) {

        return chatService.getUserList(roomId);
    }

    @GetMapping("/chat/duplicateName")
    @ResponseBody
    public String isDuplicateName(@RequestParam("roomId") String roomId, @RequestParam("username") String username) {
        return chatService.isDuplicateName(roomId, username);
    }
}