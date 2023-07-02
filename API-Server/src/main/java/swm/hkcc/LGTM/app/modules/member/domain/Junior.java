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

    private String position;

    public static Junior create(JuniorSignUpRequest request, Member member) {
        return Junior.builder()
                .member(member)
                .position(request.getPosition())
                .build();
    }
}
