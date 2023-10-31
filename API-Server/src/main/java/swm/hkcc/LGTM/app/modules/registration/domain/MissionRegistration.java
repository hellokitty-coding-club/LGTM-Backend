package swm.hkcc.LGTM.app.modules.registration.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.exception.InvalidProcessStatus;

import java.io.Serializable;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionRegistration extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    private Long registrationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcessStatus status;

    @Column(nullable = true)
    @ColumnDefault("''")
    private String githubPullRequestUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "junior_id")
    private Member junior;

    @ColumnDefault("false")
    private Boolean isPayed;

    @ColumnDefault("false")
    private Boolean isPullRequestCreated;

    public void confirmPayment() {
        if (this.getStatus() != ProcessStatus.PAYMENT_CONFIRMATION)
            throw new InvalidProcessStatus(this.getStatus());
        this.status = this.status.getNextStatus();
        this.isPayed = true;
    }
    public void completeReview() {
        if(this.getStatus() != ProcessStatus.CODE_REVIEW)
            throw new InvalidProcessStatus(this.getStatus());
        this.status = this.status.getNextStatus();
    }
    public void registerPayment() {
        if(this.getStatus() != ProcessStatus.WAITING_FOR_PAYMENT)
            throw new InvalidProcessStatus(this.getStatus());
        this.status = this.status.getNextStatus();
    }
    public void registerPullRequest(String githubPullRequestUrl) {
        if(this.getStatus() != ProcessStatus.MISSION_PROCEEDING)
            throw new InvalidProcessStatus(this.getStatus());
        this.status = this.status.getNextStatus();
        this.isPullRequestCreated = true;
        this.githubPullRequestUrl = githubPullRequestUrl;
    }
}
