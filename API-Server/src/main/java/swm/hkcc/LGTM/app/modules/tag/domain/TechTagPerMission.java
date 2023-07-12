package swm.hkcc.LGTM.app.modules.tag.domain;

import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechTagPerMission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_tag_per_mission_id")
    private Long techTagPerMissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_tag_id")
    private TechTag techTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;
}
