package swm.hkcc.LGTM.app.modules.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.auth.constants.TokenType;
import swm.hkcc.LGTM.app.modules.auth.dto.*;
import swm.hkcc.LGTM.app.modules.auth.utils.jwt.TokenProvider;
import swm.hkcc.LGTM.app.modules.member.entity.Member;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Override
    public SignInResponse handlingAuthentication(GithubUserInfo githubUserInfo, GithubOAuthResponse githubOAuthResponse) {
        Optional<Member> member = memberRepository.findOneByGithubId(githubUserInfo.getLogin());

        if (member.isPresent()) {
            return SignInResponse.builder()
                    .userId(member.get().getMemberId())
                    .githubId(githubUserInfo.getLogin())
                    .isRegistered(true)
                    .tokenDto(TokenDetailDto.builder()
                            .accessToken(tokenProvider.createToken(
                                    member.get().getMemberId(),
                                    member.get().getGithubId(),
                                    TokenType.ACCESS_TOKEN
                            ))
                            .refreshToken(tokenProvider.createToken(
                                            member.get().getMemberId(),
                                            member.get().getGithubId(),
                                            TokenType.REFRESH_TOKEN
                                    ))
                            .githubAccessToken(githubOAuthResponse.getAccessToken())
                            .githubRefreshToken(githubOAuthResponse.getRefreshToken())
                            .build())
                    .build();
        } else {
            return SignInResponse.builder()
                    .userId(0L)
                    .githubId(githubUserInfo.getLogin())
                    .isRegistered(false)
                    .tokenDto(TokenDto.builder()
                            .githubAccessToken(githubOAuthResponse.getAccessToken())
                            .githubRefreshToken(githubOAuthResponse.getRefreshToken())
                            .build())
                    .build();
        }
    }
}
