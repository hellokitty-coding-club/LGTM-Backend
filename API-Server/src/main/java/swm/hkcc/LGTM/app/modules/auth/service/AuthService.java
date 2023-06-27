package swm.hkcc.LGTM.app.modules.auth.service;

import swm.hkcc.LGTM.app.modules.auth.dto.GithubOAuthResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.GithubUserInfo;
import swm.hkcc.LGTM.app.modules.auth.dto.SignInResponse;


public interface AuthService {
    SignInResponse handlingAuthentication(GithubUserInfo githubUserInfo, GithubOAuthResponse githubOAuthResponse);
}
