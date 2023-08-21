package swm.hkcc.LGTM.app.modules.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.auth.exception.UnspecifiedMemberType;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.repository.JuniorRepository;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.member.repository.SeniorRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {
    private static final String JUNIOR = "JUNIOR";
    private static final String SENIOR = "SENIOR";

    private final MemberRepository memberRepository;
    private final JuniorRepository juniorRepository;
    private final SeniorRepository seniorRepository;

    public Boolean updateDeviceToken(Long memberId, Optional<String> deviceToken) {
        deviceToken.ifPresent(memberRepository::eraseDeviceToken);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotExistMember::new);

        member.setDeviceToken(deviceToken.orElse(null));
        memberRepository.save(member);

        return true;
    }

    public String getMemberType(Long memberId) {
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
