package swm.hkcc.LGTM.app.modules.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.exception.NotJuniorMember;
import swm.hkcc.LGTM.app.modules.member.exception.NotSeniorMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberValidator {
    private final MemberRepository memberRepository;
    public void validateSenior(Member member) {
        if (member.getSenior() == null)
            throw new NotSeniorMember();
    }

    public void validateJunior(Member member) {
        if (member.getJunior() == null)
            throw new NotJuniorMember();
    }
    public void validateJunior(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotExistMember::new);
        if (member.getJunior() == null)
            throw new NotJuniorMember();
    }

    // todo : MemberPostion enum 생성 후 적용하기
    //    private void validateMemberPosition(Member member, ExpectedPosition expectedRole) {
    //        if (expectedRole == ExpectedPosition.JUNIOR && member.getJunior() == null) {
    //            throw new NotJuniorMember();
    //        } else if (expectedRole == ExpectedPosition.SENIOR && member.getSenior() == null) {
    //            throw new NotSeniorMember();
    //        }
    //    }
}
