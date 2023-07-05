package swm.hkcc.LGTM.app.modules.member.domain;

import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SignUpRequest;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String githubId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @Column(unique = true)
    private String deviceToken;

    @Column(nullable = false)
    private String profileImageUrl;

    @Column(nullable = false)
    private String introduction;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Junior junior;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Senior senior;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setRoles(List<Authority> role) {
        this.roles = role;
        role.forEach(o -> o.setMember(this));
    }

    public static Member from(SignUpRequest request) {
        return Member.builder()
                .githubId(request.getGithubId())
                .name(request.getName())
                .nickName(request.getNickName())
                .refreshToken("")
                .deviceToken(request.getDeviceToken())
                .profileImageUrl(request.getProfileImageUrl())
                .introduction(request.getIntroduction())
                .build();
    }
}
