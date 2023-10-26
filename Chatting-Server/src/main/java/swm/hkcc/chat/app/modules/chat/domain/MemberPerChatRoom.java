package swm.hkcc.chat.app.modules.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.chat.app.modules.member.domain.Member;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MemberPerChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    private boolean isJoined;
}
