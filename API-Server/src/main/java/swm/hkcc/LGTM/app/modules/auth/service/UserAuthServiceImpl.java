package swm.hkcc.LGTM.app.modules.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.auth.dto.GithubOAuthResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.GithubUserInfo;
import swm.hkcc.LGTM.app.modules.auth.dto.SignInResponse;
import swm.hkcc.LGTM.app.modules.auth.entity.User;
import swm.hkcc.LGTM.app.modules.auth.repository.UserAuthRepository;
import swm.hkcc.LGTM.app.modules.auth.utils.jwt.TokenProvider;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final UserAuthRepository userAuthRepository;

    private final TokenProvider tokenProvider;

    @Override
    public SignInResponse getUserLoginInfo(GithubOAuthResponse githubOAuthResponse, GithubUserInfo githubUserInfo) {
        Optional<User> user = userAuthRepository.findUserByGithubId(githubUserInfo.getLogin());

        if (user.isPresent()) {
            // Todo : 로그인 처리
            // Todo : 해당 메소드의 반환값은 우리 서버의 access token, refresh token, user 정보를 담은 dto일 것


            return SignInResponse.builder()
                    .accessToken(tokenProvider.createToken())//todo
                    .refreshToken(user.get().getRefreshToken())
                    .build();
        } else {
            // Todo : 로그인 되지 않았을경우, 회원가입 하도록 메시지 전달
            return null;
        }
    }
}
