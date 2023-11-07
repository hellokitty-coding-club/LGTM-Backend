package swm.hkcc.LGTM.app.modules.mission.domain;

import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequest;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequestV2;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    private Long missionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @Lob
    private String missionRepositoryUrl;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    private MissionStatus missionStatus;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = true, length = 1000)
    private String recommendTo;

    @Column(nullable = true, length = 1000)
    private String notRecommendTo;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate registrationDueDate;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer maxPeopleNumber;

    public static Mission from(CreateMissionRequest request, Member writer) {
        return Mission.builder()
                .writer(writer)
                .missionRepositoryUrl(request.getMissionRepositoryUrl())
                .title(request.getTitle())
                .missionStatus(MissionStatus.RECRUITING)
                .description(request.getDescription())
                .recommendTo(request.getRecommendTo())
                .notRecommendTo(request.getNotRecommendTo())
                .registrationDueDate(request.getRegistrationDueDate())
                .price(request.getPrice())
                .maxPeopleNumber(request.getMaxPeopleNumber())
                .build();
    }

    public static Mission from(CreateMissionRequestV2 request, Member writer) {
        return Mission.builder()
                .writer(writer)
                .missionRepositoryUrl(request.getMissionRepositoryUrl() == null ? "" : request.getMissionRepositoryUrl())
                .title(request.getTitle())
                .missionStatus(MissionStatus.RECRUITING)
                .description(request.getDescription())
                .recommendTo(request.getRecommendTo())
                .notRecommendTo(request.getNotRecommendTo())
                .registrationDueDate(request.getRegistrationDueDate())
                .price(request.getPrice())
                .maxPeopleNumber(request.getMaxPeopleNumber())
                .build();
    }
}
