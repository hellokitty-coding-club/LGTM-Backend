package swm.hkcc.LGTM.app.modules.member.domain;

import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.JuniorSignUpRequest;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Junior {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long juniorId;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;

    private String educationalHistory;

    public static Junior from(JuniorSignUpRequest request, Member member) {
        return Junior.builder()
                .member(member)
                .educationalHistory(request.getEducationalHistory())
                .build();
    }
}
