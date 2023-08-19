package swm.hkcc.LGTM.app.modules.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.global.exception.GeneralException;
import swm.hkcc.LGTM.app.modules.auth.constants.TokenType;
import swm.hkcc.LGTM.app.modules.auth.dto.oauth.GithubUserInfo;
import swm.hkcc.LGTM.app.modules.auth.dto.signIn.SignInResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.CommonUserData;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.JuniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SeniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SignUpResponse;
import swm.hkcc.LGTM.app.modules.auth.exception.DuplicateNickName;
import swm.hkcc.LGTM.app.modules.auth.exception.InvalidTechTag;
import swm.hkcc.LGTM.app.modules.auth.exception.UnspecifiedMemberType;
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
    private static final String JUNIOR = "JUNIOR";
    private static final String SENIOR = "SENIOR";

    private final MemberRepository memberRepository;
    private final JuniorRepository juniorRepository;
    private final SeniorRepository seniorRepository;
    private final TechTagRepository techTagRepository;
    private final TechTagPerMemberRepository techTagPerMemberRepository;
    private final TokenProvider tokenProvider;

    @Override
    @Transactional
    public SignInResponse githubSignIn(GithubUserInfo githubUserInfo) {
        log.debug("githubUserInfo={}", githubUserInfo);

        Optional<Member> member = memberRepository.findByGithubOauthId(githubUserInfo.getId());

        if (member.isPresent()) {
            Member updatedMember = member.get();
            String refreshToken = createRefreshToken(updatedMember);
            updatedMember.setRefreshToken(refreshToken);
            memberRepository.save(updatedMember);

            return SignInResponse.builder()
                    .memberId(updatedMember.getMemberId())
                    .githubId(githubUserInfo.getLogin())
                    .githubOauthId(githubUserInfo.getId())
                    .isRegistered(true)
                    .accessToken(createAccessToken(updatedMember))
                    .refreshToken(refreshToken)
                    .profileImageUrl(githubUserInfo.getAvatar_url())
                    .memberType(getMemberType(updatedMember.getMemberId()))
                    .build();
        }

        return SignInResponse.builder()
                .memberId(0L)
                .githubId(githubUserInfo.getLogin())
                .githubOauthId(githubUserInfo.getId())
                .isRegistered(false)
                .profileImageUrl(githubUserInfo.getAvatar_url())
                .build();

    }

    @Override
    @Transactional
    public SignUpResponse signupJunior(JuniorSignUpRequest request) {
        Member member = createAndSaveMember(request);
        Junior junior = Junior.from(request, member);
        juniorRepository.save(junior);

        return buildSignUpResponse(member);
    }

    @Override
    @Transactional
    public SignUpResponse signupSenior(SeniorSignUpRequest request) {
        Member member = createAndSaveMember(request);
        Senior senior = Senior.from(request, member);
        seniorRepository.save(senior);

        return buildSignUpResponse(member);
    }

    @Override
    public boolean checkDuplicateNickname(String nickname) {
        return memberRepository.existsByNickName(nickname);
    }

    private Member createAndSaveMember(CommonUserData request) {
        validateSignUpRequest(request);
        Member member = Member.from(request);
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        memberRepository.eraseDeviceToken(request.getDeviceToken());
        Member savedMember = memberRepository.save(member);
        setTagListOfMember(member, request.getTagList());
        updateRefreshToken(savedMember);

        return savedMember;
    }

    private void validateSignUpRequest(CommonUserData request) {
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
            throw new InvalidTechTag();
        }
    }

    private String createAccessToken(Member member) {
        return tokenProvider.createToken(
                member.getGithubId(),
                TokenType.ACCESS_TOKEN
        );
    }

    private String createRefreshToken(Member member) {
        return tokenProvider.createToken(
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
                .memberType(getMemberType(member.getMemberId()))
                .build();
    }

    private String getMemberType(Long memberId) {
        if (isJunior(memberId)) {
            return JUNIOR;
        }
        else if (isSenior(memberId)) {
            return SENIOR;
        }
        else {
            throw new UnspecifiedMemberType();
        }
    }

    private boolean isJunior(Long memberId) {
        return juniorRepository.existsByMember_MemberId(memberId);
    }

    private boolean isSenior(Long memberId) {
        return seniorRepository.existsByMember_MemberId(memberId);
    }
}