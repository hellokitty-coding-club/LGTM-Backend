package swm.hkcc.chat.app.modules.chat.model;

import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {
    private String roomId;
    private String roomName;
    private long userCount;
    private Map<String, String> userlist = new HashMap<>();

    public static ChatRoom createRoom(String roomName){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.roomName = roomName;
        return chatRoom;
    }
}

