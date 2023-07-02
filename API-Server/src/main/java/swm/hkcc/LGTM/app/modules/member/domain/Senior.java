package swm.hkcc.LGTM.app.modules.member.domain;

import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SeniorSignUpRequest;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Senior {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seniorId;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;

    private String companyInfo;
    private int careerPeriod;

    public static Senior create(SeniorSignUpRequest request, Member member) {
        return Senior.builder()
                .member(member)
                .companyInfo(request.getCompanyInfo())
                .careerPeriod(request.getCareerPeriod())
                .build();
    }
}
