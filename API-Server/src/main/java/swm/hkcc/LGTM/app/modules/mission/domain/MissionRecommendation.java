package swm.hkcc.LGTM.app.modules.mission.domain;


import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.LGTM.app.modules.member.domain.Member;

import java.io.Serializable;
import java.util.Objects;

@Table(name = "mission_recommendation")
@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionRecommendation implements Serializable {

    @Embeddable
    public static class MissionRecommendationId implements Serializable {
        @Column(name = "mission_id")
        private Long missionId;

        @Column(name = "member_id")
        private Long memberId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MissionRecommendationId that = (MissionRecommendationId) o;
            return Objects.equals(missionId, that.missionId) && Objects.equals(memberId, that.memberId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(missionId, memberId);
        }
    }

    @EmbeddedId
    private MissionRecommendationId id;

    @Column(name = "total_rate", nullable = false)
    private int totalRate;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", insertable = false, updatable = false)
    private Member member;

    @ManyToOne
    @MapsId("missionId")
    @JoinColumn(name = "mission_id", referencedColumnName = "mission_id", insertable = false, updatable = false)
    private Mission mission;
}