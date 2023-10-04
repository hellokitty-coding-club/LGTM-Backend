package swm.hkcc.chat.app.modules.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swm.hkcc.chat.app.modules.chat.model.ChatRoom;
import swm.hkcc.chat.app.modules.chat.service.ChatService;

/**
 * This controller is currently used for testing purposes with a web interface.
 * In the future, it will be integrated with an Android app.
 */
@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chatroom/v1")
public class ChatRoomControllerV1 {

    private final ChatService chatService;

    @GetMapping
    public String goChatRoom(Model model) {
        model.addAttribute("list", chatService.findAllRoom());
        log.info("SHOW ALL ChatList {}", chatService.findAllRoom());

        return "roomlist";
    }

    @PostMapping
    public String createRoom(@RequestParam String name, RedirectAttributes rttr) {
        ChatRoom room = chatService.createChatRoom(name);
        log.info("CREATE Chat Room {}", room);
        rttr.addFlashAttribute("roomName", room);

        return "redirect:/chatroom";
    }

    @GetMapping("/detail")
    public String roomDetail(Model model, String roomId) {
        log.info("roomId {}", roomId);
        model.addAttribute("room", chatService.findRoomById(roomId));

        return "chatroom";
    }
}
