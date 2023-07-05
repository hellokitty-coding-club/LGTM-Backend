package swm.hkcc.LGTM.app.modules.member.domain;

import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SeniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.member.exception.InvalidCareerPeriod;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Senior {

    private static final Integer MINIMUM_CAREER_PERIOD = 12;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seniorId;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;

    private String companyInfo;
    private int careerPeriod;
    private String position;

    public static Senior from(SeniorSignUpRequest request, Member member) {
        validateCareerPeriod(request.getCareerPeriod());
        return Senior.builder()
                .member(member)
                .companyInfo(request.getCompanyInfo())
                .careerPeriod(request.getCareerPeriod())
                .position(request.getPosition())
                .build();
    }

    private static void validateCareerPeriod(Integer careerPeriod) {
        if (careerPeriod < MINIMUM_CAREER_PERIOD) {
            throw new InvalidCareerPeriod();
        }
    }
}
