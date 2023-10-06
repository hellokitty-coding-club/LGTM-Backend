package swm.hkcc.chat.app.modules.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import swm.hkcc.chat.app.modules.chat.domain.ChatMessage;
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
    public void enterUser(@Payload ChatMessage chat, SimpMessageHeaderAccessor headerAccessor) {
        Long memberId = chatService.addUser(chat.getRoomId(), chat.getSender());

        headerAccessor.getSessionAttributes().put("userUUID", memberId);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

        chat.setMessage(chat.getSender() + " 님 입장!!");
        chatProducer.sendChatMessage(chat);
    }

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatMessage chat) {
        log.info("CHAT {}", chat);
        chatProducer.sendChatMessage(chat);
    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("DisConnEvent {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Long memberId = (Long) headerAccessor.getSessionAttributes().get("userUUID");
        Long roomId = (Long) headerAccessor.getSessionAttributes().get("roomId");

        log.info("headAccessor {}", headerAccessor);

        chatService.delUser(roomId, memberId);

        String username = chatService.getMember(memberId).getNickName();

        if (username != null) {
            log.info("User Disconnected : " + username);

            ChatMessage chat = ChatMessage.builder()
                    .messageType(ChatMessage.MessageType.LEAVE)
                    .sender(username)
                    .message(username + " 님 퇴장!!")
                    .build();

            chatProducer.sendChatMessage(chat);
        }
    }

    @GetMapping("/chat/userlist")
    @ResponseBody
    public List<String> userList(String sroomId) {
        Long roomId = Long.parseLong(sroomId);
        return chatService.getUserList(roomId);
    }

    @GetMapping("/chat/duplicateName")
    @ResponseBody
    public String convertDuplicateName(@RequestParam("roomId") String sroomId, @RequestParam("username") String username) {
        Long roomId = Long.parseLong(sroomId);
        return chatService.isDuplicateName(roomId, username);
    }
}