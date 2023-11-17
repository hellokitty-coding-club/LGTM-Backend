package swm.hkcc.LGTM.app.modules.mission.domain;

import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;
import swm.hkcc.LGTM.app.modules.member.domain.Member;

import java.io.Serializable;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionView extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_view_id")
    private Long missionViewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viewer_id")
    private Member viewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    public static MissionView from(Mission mission, Member member) {
        return MissionView.builder()
                .mission(mission)
                .viewer(member)
                .build();
    }
}
