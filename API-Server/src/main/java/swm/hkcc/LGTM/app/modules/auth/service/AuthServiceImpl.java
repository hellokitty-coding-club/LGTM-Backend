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
import swm.hkcc.LGTM.app.modules.auth.exception.DuplicateNickName;
import swm.hkcc.LGTM.app.modules.auth.utils.jwt.TokenProvider;
import swm.hkcc.LGTM.app.modules.member.domain.Authority;
import swm.hkcc.LGTM.app.modules.member.domain.Junior;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.Senior;
import swm.hkcc.LGTM.app.modules.member.repository.JuniorRepository;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.member.repository.SeniorRepository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTagPerMember;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMemberRepository;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final JuniorRepository juniorRepository;
    private final SeniorRepository seniorRepository;
    private final TechTagRepository techTagRepository;
    private final TechTagPerMemberRepository techTagPerMemberRepository;
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
        Junior junior = Junior.from(request, member);
        juniorRepository.save(junior);

        return buildSignUpResponse(member);
    }

    @Override
    @Transactional
    public SignUpResponse seniorSignUp(SeniorSignUpRequest request) {
        Member member = createAndSaveMember(request);
        Senior senior = Senior.from(request, member);
        seniorRepository.save(senior);

        return buildSignUpResponse(member);
    }

    private Member createAndSaveMember(SignUpRequest request) {
        validateSignUpRequest(request);
        Member member = Member.from(request);
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        Member savedMember = memberRepository.save(member);
        setTagListOfMember(member, request.getTagList());
        updateRefreshToken(savedMember);

        return savedMember;
    }

    private void validateSignUpRequest(SignUpRequest request) {
        validateDuplicateNickName(request.getNickName());
        validateTagList(request.getTagList());
    }

    private void validateDuplicateNickName(String nickName) {
        if (memberRepository.existsByNickName(nickName)) {
            throw new DuplicateNickName();
        }
    }

    private void validateTagList(List<String> tagList) {
        boolean hasInvalidTag = tagList.stream()
                .anyMatch(tag -> !techTagRepository.existsByName(tag));

        if (hasInvalidTag) {
            throw new IllegalArgumentException("One or more tags are invalid");
        }
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

    private void setTagListOfMember(Member member, List<String> tagList) {
        tagList.stream()
                .map(tagName -> techTagRepository.findByName(tagName)
                        .orElseGet(() -> techTagRepository.save(TechTag.builder().name(tagName).build())))
                .forEach(techTag -> {
                    TechTagPerMember techTagPerMember = TechTagPerMember.builder()
                            .member(member)
                            .techTag(techTag)
                            .build();
                    techTagPerMemberRepository.save(techTagPerMember);
                });
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
