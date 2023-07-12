package swm.hkcc.LGTM.app.modules.chat.domain;

import jakarta.persistence.*;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;
import swm.hkcc.LGTM.app.modules.member.domain.Member;

public class MemberPerChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_per_chat_room_id")
    private Long memberPerChatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
