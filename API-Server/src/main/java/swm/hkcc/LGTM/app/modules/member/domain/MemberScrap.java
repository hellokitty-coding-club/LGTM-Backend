package swm.hkcc.LGTM.app.modules.member.domain;

import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberScrap extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_scrap_id")
    private Long memberScrapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrapped_member_id")
    private Member scrappedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrapper_id")
    private Member scrapper;
}
