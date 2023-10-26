package swm.hkcc.chat.app.modules.member.domain;

import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.chat.app.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileView extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_view_id")
    private Long profileViewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viewer_id")
    private Member viewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_member_id")
    private Member profileMember;
}
