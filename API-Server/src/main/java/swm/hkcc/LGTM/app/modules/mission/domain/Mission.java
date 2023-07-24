package swm.hkcc.LGTM.app.modules.mission.domain;

import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequest;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Mission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    private Long missionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @Lob
    @Column(nullable = false)
    private String missionRepositoryUrl;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    private MissionStatus missionStatus;

    @Lob
    @Column(nullable = false)
    private String thumbnailImageUrl;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, length = 1000)
    private String reomnnandTo;

    @Column(nullable = false, length = 1000)
    private String notReomnnandTo;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate registrationDueDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate assignmentDueDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate reviewCompletationDueDate;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer maxPeopleNumber;

    public static Mission from(CreateMissionRequest request, Member writer) {
        return Mission.builder()
                .writer(writer)
                .missionRepositoryUrl(request.getMissionRepositoryUrl())
                .title(request.getTitle())
                .missionStatus(MissionStatus.from(request.getMissionStatus()))
                .thumbnailImageUrl(request.getThumbnailImageUrl())
                .description(request.getDescription())
                .reomnnandTo(request.getReomnnandTo())
                .notReomnnandTo(request.getNotReomnnandTo())
                .registrationDueDate(request.getRegistrationDueDate())
                .assignmentDueDate(request.getAssignmentDueDate())
                .reviewCompletationDueDate(request.getReviewCompletationDueDate())
                .price(request.getPrice())
                .maxPeopleNumber(request.getMaxPeopleNumber())
                .build();
    }
}
