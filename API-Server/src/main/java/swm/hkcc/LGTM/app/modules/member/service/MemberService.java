package swm.hkcc.LGTM.app.modules.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public Boolean updateDeviceToken(Long memberId, String deviceToken) {
        // todo : deviceToken validation

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotExistMember::new);

        member.setDeviceToken(deviceToken);
        memberRepository.save(member);
        return true;
    }
}
