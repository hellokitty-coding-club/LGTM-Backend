package swm.hkcc.LGTM.app.modules.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;

@Getter
@Setter
@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    private Integer userId;
    private String nickName;
    private String githubId;
    private String githubRefreshToken;
    private String refreshToken;
    private String deviceToken;
    private String profileImageUrl;
    private String introduction;
}
