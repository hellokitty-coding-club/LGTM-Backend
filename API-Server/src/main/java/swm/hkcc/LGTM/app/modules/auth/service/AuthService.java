package swm.hkcc.LGTM.app.modules.auth.service;

import swm.hkcc.LGTM.app.modules.auth.dto.oauth.GithubUserInfo;
import swm.hkcc.LGTM.app.modules.auth.dto.signIn.SignInResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.JuniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SeniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SignUpResponse;


public interface AuthService {
    SignInResponse githubSignIn(GithubUserInfo githubUserInfo);

    SignUpResponse juniorSignUp(JuniorSignUpRequest request);

    SignUpResponse seniorSignUp(SeniorSignUpRequest request);

    boolean isNicknameDuplicate(String nickname);
}
