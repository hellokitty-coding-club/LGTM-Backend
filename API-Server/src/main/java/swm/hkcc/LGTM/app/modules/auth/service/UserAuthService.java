package swm.hkcc.LGTM.app.modules.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.auth.dto.GithubOAuthResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.GithubUserInfo;
import swm.hkcc.LGTM.app.modules.auth.dto.UserAuthResponse;

@Service
public interface UserAuthService {

    public UserAuthResponse getUserLoginInfo(GithubOAuthResponse githubOAuthResponse,
                                             GithubUserInfo githubUserInfo);

}
