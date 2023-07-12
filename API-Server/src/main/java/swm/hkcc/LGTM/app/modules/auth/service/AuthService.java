package swm.hkcc.LGTM.app.modules.auth.service;

import swm.hkcc.LGTM.app.modules.auth.dto.oauth.GithubUserInfo;
import swm.hkcc.LGTM.app.modules.auth.dto.signIn.SignInResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.JuniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SeniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SignUpResponse;


public interface AuthService {
    SignInResponse githubSignIn(GithubUserInfo githubUserInfo);

    SignUpResponse signupJunior(JuniorSignUpRequest request);

    SignUpResponse signupSenior(SeniorSignUpRequest request);

    boolean checkDuplicateNickname(String nickname);
}
