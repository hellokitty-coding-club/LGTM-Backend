package swm.hkcc.chat.app.modules.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swm.hkcc.chat.app.modules.chat.domain.ChatRoom;
import swm.hkcc.chat.app.modules.chat.dto.CreateRoomRequest;
import swm.hkcc.chat.app.modules.chat.service.ChatService;

/**
 * This controller is currently used for testing purposes with a web interface.
 * In the future, it will be integrated with an Android app.
 */
@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {

    private final ChatService chatService;

    @GetMapping
    public String goChatRoom(Model model) {
        model.addAttribute("list", chatService.findAllRoom());
        log.info("SHOW ALL ChatList {}", chatService.findAllRoom());

        return "roomlist";
    }

    @PostMapping
    public String createRoom(
            @RequestParam String name,
            RedirectAttributes rttr
    ) {
        // 테스트 용
        CreateRoomRequest request = CreateRoomRequest.builder()
                .roomName(name)
                .roomType("private")
                .processStatus("waiting")
                .missionId(1L)
                .build();
        ChatRoom room = chatService.createChatRoom(request);
        log.info("CREATE Chat Room {}", room);
        rttr.addFlashAttribute("roomName", room);

        return "redirect:/chatroom";
    }

    @GetMapping("/detail")
    public String roomDetail(Model model, Long roomId) {
        log.info("roomId {}", roomId);
        model.addAttribute("room", chatService.findRoomById(roomId));

        return "chatroom";
    }
}
