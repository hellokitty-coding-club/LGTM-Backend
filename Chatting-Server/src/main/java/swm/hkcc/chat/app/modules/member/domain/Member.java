package swm.hkcc.chat.app.modules.member.domain;

import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.chat.app.global.entity.BaseEntity;
import swm.hkcc.chat.app.modules.auth.dto.signUp.CommonUserData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String githubId;

    @Column(nullable = false, unique = true)
    private Integer githubOauthId;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @Column(nullable = true, unique = false)
    private String deviceToken;

    @Column(nullable = false)
    private String profileImageUrl;

    @Column(nullable = false)
    private String introduction;

    @Column
    private boolean isAgreeWithEventInfo;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Junior junior;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Senior senior;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setRoles(List<Authority> role) {
        this.roles = role;
        role.forEach(o -> o.setMember(this));
    }

    public static Member from(CommonUserData request) {
        return Member.builder()
                .githubId(request.getGithubId())
                .githubOauthId(request.getGithubOauthId())
                .nickName(request.getNickName())
                .refreshToken("")
                .deviceToken(request.getDeviceToken())
                .profileImageUrl(request.getProfileImageUrl())
                .introduction(request.getIntroduction())
                .isAgreeWithEventInfo(request.isAgreeWithEventInfo())
                .build();
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setJunior(Junior junior) {
        this.junior = junior;
    }

    public void setSenior(Senior senior) {
        this.senior = senior;
    }
}
