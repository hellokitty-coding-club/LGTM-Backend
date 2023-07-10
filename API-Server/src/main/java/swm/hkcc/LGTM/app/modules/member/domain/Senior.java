package swm.hkcc.LGTM.app.modules.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SeniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.member.constant.Bank;
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

    @NotNull
    private String companyInfo;

    @NotNull
    private int careerPeriod;

    @NotNull
    private String position;

    @NotNull
    private String accountNumber;

    @NotNull
    private Bank bank;

    public static Senior from(SeniorSignUpRequest request, Member member) {
        validateCareerPeriod(request.getCareerPeriod());
        return Senior.builder()
                .member(member)
                .companyInfo(request.getCompanyInfo())
                .careerPeriod(request.getCareerPeriod())
                .position(request.getPosition())
                .accountNumber(request.getAccountNumber())
                .bank(request.getBank())
                .build();
    }

    private static void validateCareerPeriod(Integer careerPeriod) {
        if (careerPeriod < MINIMUM_CAREER_PERIOD) {
            throw new InvalidCareerPeriod();
        }
    }
}
