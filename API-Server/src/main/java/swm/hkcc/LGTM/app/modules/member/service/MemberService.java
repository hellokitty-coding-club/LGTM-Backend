package swm.hkcc.LGTM.app.modules.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.auth.constants.MemberType;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {
    private static final String JUNIOR = "JUNIOR";
    private static final String SENIOR = "SENIOR";

    private final MemberRepository memberRepository;

    public Boolean updateDeviceToken(Long memberId, Optional<String> deviceToken) {
        deviceToken.ifPresent(memberRepository::eraseDeviceToken);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotExistMember::new);

        member.setDeviceToken(deviceToken.orElse(null));
        memberRepository.save(member);

        return true;
    }

    public MemberType getMemberType(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotExistMember::new);

        return MemberType.getType(member);
    }
}
