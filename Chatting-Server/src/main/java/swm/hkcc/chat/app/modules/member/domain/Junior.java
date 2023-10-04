package swm.hkcc.chat.app.modules.member.domain;

import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.chat.app.modules.auth.dto.signUp.JuniorSignUpRequest;

import java.io.Serializable;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Junior implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long juniorId;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;

    private String educationalHistory;

    private String realName;

    public static Junior from(JuniorSignUpRequest request, Member member) {
        Junior junior = Junior.builder()
                .member(member)
                .educationalHistory(request.getEducationalHistory())
                .realName(request.getRealName())
                .build();
        member.setJunior(junior);
        return junior;
    }
}
