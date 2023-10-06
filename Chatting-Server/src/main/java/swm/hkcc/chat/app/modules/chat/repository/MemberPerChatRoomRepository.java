package swm.hkcc.chat.app.modules.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.chat.app.modules.chat.domain.ChatRoom;
import swm.hkcc.chat.app.modules.chat.domain.MemberPerChatRoom;
import swm.hkcc.chat.app.modules.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberPerChatRoomRepository extends JpaRepository<MemberPerChatRoom, Long> {
    boolean existsByChatRoomAndMember(ChatRoom room, Member member);

    Optional<MemberPerChatRoom> findByChatRoom_roomIdAndMember_memberId(Long chatRoomId, Long memberId);

    List<MemberPerChatRoom> findByChatRoom_RoomId(Long chatRoomId);
}
