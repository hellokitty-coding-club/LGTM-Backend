package swm.hkcc.LGTM.app.modules.chat.service;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.chat.model.ChatRoom;

import java.util.*;

@Slf4j
@Service
public class ChatService {

    private Map<String, ChatRoom> chatRoomMap;

    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom(){
        return new ArrayList<>(chatRoomMap.values());
    }

    public ChatRoom findRoomById(String roomId){
        return getChatRoom(roomId);
    }

    public ChatRoom createChatRoom(String roomName){
        ChatRoom chatRoom = ChatRoom.createRoom(roomName);
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    public void plusUserCnt(String roomId){
        modifyUserCount(roomId, 1);
    }

    public void minusUserCnt(String roomId){
        modifyUserCount(roomId, -1);
    }

    public String addUser(String roomId, String userName){
        ChatRoom room = getChatRoom(roomId);
        String userUUID = UUID.randomUUID().toString();
        room.getUserlist().put(userUUID, userName);
        return userUUID;
    }

    public String isDuplicateName(String roomId, String username){
        ChatRoom room = getChatRoom(roomId);
        String tmp = username;

        while(room.getUserlist().containsValue(tmp)){
            int ranNum = (int) (Math.random()*100)+1;
            tmp = username+ranNum;
        }

        return tmp;
    }

    public void delUser(String roomId, String userUUID){
        ChatRoom room = getChatRoom(roomId);
        room.getUserlist().remove(userUUID);
    }

    public String getUserName(String roomId, String userUUID){
        ChatRoom room = getChatRoom(roomId);
        return room.getUserlist().get(userUUID);
    }

    public List<String> getUserList(String roomId){
        return new ArrayList<>(getChatRoom(roomId).getUserlist().values());
    }

    private ChatRoom getChatRoom(String roomId){
        return chatRoomMap.get(roomId);
    }

    private void modifyUserCount(String roomId, int count){
        ChatRoom room = getChatRoom(roomId);
        room.setUserCount(room.getUserCount() + count);
    }
}

