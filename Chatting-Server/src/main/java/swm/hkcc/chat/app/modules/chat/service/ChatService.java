package swm.hkcc.chat.app.modules.chat.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.chat.app.modules.chat.domain.ChatRoom;
import swm.hkcc.chat.app.modules.chat.domain.MemberPerChatRoom;
import swm.hkcc.chat.app.modules.chat.dto.CreateRoomRequest;
import swm.hkcc.chat.app.modules.chat.repository.ChatRepository;
import swm.hkcc.chat.app.modules.chat.repository.ChatRoomRepository;
import swm.hkcc.chat.app.modules.chat.repository.MemberPerChatRoomRepository;
import swm.hkcc.chat.app.modules.member.domain.Member;
import swm.hkcc.chat.app.modules.member.exception.NotExistMember;
import swm.hkcc.chat.app.modules.member.repository.MemberRepository;

import java.util.*;

/**
 * 테스트용 web 인터페이스에 맞게 임시로 작성된 서비스 로직입니다. 동작 확인 용으로 사용하고, 나중에 안드로이드 개발 일정이 맞춰지면 그에 맞게 다시 수정해야 합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberPerChatRoomRepository memberPerChatRoomRepository;
    private final MemberRepository memberRepository;

    public List<ChatRoom> findAllRoom(){
        return chatRoomRepository.findAll();
    }

    public ChatRoom findRoomById(Long roomId){
        return getChatRoom(roomId);
    }

    public ChatRoom createChatRoom(CreateRoomRequest request){
        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(request.getRoomName())
                .roomType(request.getRoomType())
                .missionId(request.getMissionId())
                .processStatus(request.getProcessStatus())
                .build();

        chatRoomRepository.save(chatRoom);

        Member member = getMember(request.getNickName());

        memberPerChatRoomRepository.save(MemberPerChatRoom.builder()
                .chatRoom(chatRoom)
                .member(member)
                .isJoined(true)
                .build());

        return chatRoom;
    }

    public Long addUser(Long roomId, String userName){
        ChatRoom room = getChatRoom(roomId);
        Member member = getMember(userName);
        MemberPerChatRoom memberPerChatRoom = MemberPerChatRoom.builder()
                .chatRoom(room)
                .member(member)
                .isJoined(true)
                .build();

        memberPerChatRoomRepository.save(memberPerChatRoom);

        return member.getMemberId();
    }

    private Member getMember(String nickName) {
        return memberRepository.findByNickName(nickName).orElseThrow(NotExistMember::new);
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(NotExistMember::new);
    }

    public String isDuplicateName(Long roomId, String username){
        String tmp = username;
        ChatRoom room = getChatRoom(roomId);
        Member member = getMember(username);
        if (isMemberAlreadyJoin(room, member)) {
            tmp = username + "_" + System.currentTimeMillis();
        }
        return tmp;
    }

    private boolean isMemberAlreadyJoin(ChatRoom room, Member member) {
        return memberPerChatRoomRepository.existsByChatRoomAndMember(room, member);
    }

    public void delUser(Long roomId, Long memberId){
        ChatRoom room = getChatRoom(roomId);
        Member member = getMember(memberId);
        MemberPerChatRoom memberPerChatRoom = memberPerChatRoomRepository.findByChatRoom_roomIdAndMember_memberId(room.getRoomId(), member.getMemberId()).orElseThrow(NotExistMember::new);
        memberPerChatRoomRepository.delete(memberPerChatRoom);
    }

    private ChatRoom getChatRoom(Long roomId){
        return chatRoomRepository.findById(roomId).get();
    }

    public List<String> getUserList(Long roomId) {
        List<MemberPerChatRoom> memberPerChatRooms = memberPerChatRoomRepository.findByChatRoom_RoomId(roomId);
        List<String> memberNames = new ArrayList<>();
        for (MemberPerChatRoom memberPerChatRoom : memberPerChatRooms) {
            memberNames.add(memberPerChatRoom.getMember().getNickName());
        }
        return memberNames;
    }
}

