package swm.hkcc.LGTM.app.modules.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SeniorSignUpRequest;

import java.io.Serializable;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Senior implements Serializable {

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

}
