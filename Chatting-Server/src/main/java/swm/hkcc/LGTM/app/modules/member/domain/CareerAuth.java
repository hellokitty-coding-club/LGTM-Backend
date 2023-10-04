package swm.hkcc.LGTM.app.modules.member.domain;

import jakarta.persistence.*;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;

public class CareerAuth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_auth_id")
    private Long careerAuthId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Lob
    @Column(nullable = false)
    private String certificationUrl;
    @Enumerated(EnumType.STRING)
    private CertificationStatus certificationStatus;
}
