package swm.hkcc.LGTM.app.modules.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.auth.constants.TokenType;
import swm.hkcc.LGTM.app.modules.auth.dto.oauth.GithubUserInfo;
import swm.hkcc.LGTM.app.modules.auth.dto.signIn.SignInResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.JuniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SeniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SignUpResponse;
import swm.hkcc.LGTM.app.modules.auth.utils.jwt.TokenProvider;
import swm.hkcc.LGTM.app.modules.member.domain.Authority;
import swm.hkcc.LGTM.app.modules.member.domain.Junior;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.Senior;
import swm.hkcc.LGTM.app.modules.member.repository.JuniorRepository;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.member.repository.SeniorRepository;

import java.util.Collections;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final JuniorRepository juniorRepository;
    private final SeniorRepository seniorRepository;
    private final TokenProvider tokenProvider;

    @Override
    @Transactional(readOnly = true)
    public SignInResponse githubSignIn(GithubUserInfo githubUserInfo) {
        log.info("githubUserInfo={}", githubUserInfo);

        Optional<Member> member = memberRepository.findOneByGithubId(githubUserInfo.getLogin());

        if (member.isPresent()) {
            return SignInResponse.builder()
                    .memberId(member.get().getMemberId())
                    .githubId(githubUserInfo.getLogin())
                    .isRegistered(true)
                    .accessToken(createAccessToken(member.get()))
                    .refreshToken(createRefreshToken(member.get()))
                    .build();
        }

        return SignInResponse.builder()
                .memberId(0L)
                .githubId(githubUserInfo.getLogin())
                .isRegistered(false)
                .build();

    }

    @Override
    @Transactional
    public SignUpResponse juniorSignUp(JuniorSignUpRequest request) {
        Member member = createAndSaveMember(request);
        Junior junior = Junior.create(request, member);
        juniorRepository.save(junior);

        return buildSignUpResponse(member);
    }

    @Override
    @Transactional
    public SignUpResponse seniorSignUp(SeniorSignUpRequest request) {
        Member member = createAndSaveMember(request);
        Senior senior = Senior.create(request, member);
        seniorRepository.save(senior);

        return buildSignUpResponse(member);
    }

    private Member createAndSaveMember(SignUpRequest request) {
        Member member = Member.create(request);
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        //tag list 추가
        Member savedMember = memberRepository.save(member);
        updateRefreshToken(savedMember);

        return savedMember;
    }

    private String createAccessToken(Member member) {
        return tokenProvider.createToken(
                member.getMemberId(),
                member.getGithubId(),
                TokenType.ACCESS_TOKEN
        );
    }

    private String createRefreshToken(Member member) {
        return tokenProvider.createToken(
                member.getMemberId(),
                member.getGithubId(),
                TokenType.REFRESH_TOKEN
        );
    }

    private void updateRefreshToken(Member member) {
        String refreshToken = createRefreshToken(member);
        member.setRefreshToken(refreshToken);
    }

    private SignUpResponse buildSignUpResponse(Member member) {
        return SignUpResponse.builder()
                .memberId(member.getMemberId())
                .githubId(member.getGithubId())
                .accessToken(createAccessToken(member))
                .refreshToken(member.getRefreshToken())
                .build();
    }
}
