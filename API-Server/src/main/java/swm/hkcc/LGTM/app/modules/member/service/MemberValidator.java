package swm.hkcc.LGTM.app.modules.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotSeniorMember;

@Service
@Transactional
public class MemberValidator {
    public void validateSenior(Member member) {
        if (member.getSenior() == null)
            throw new NotSeniorMember();
    }

    public void validateJunior(Member member) {
        if (member.getJunior() == null)
            throw new NotSeniorMember();
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
