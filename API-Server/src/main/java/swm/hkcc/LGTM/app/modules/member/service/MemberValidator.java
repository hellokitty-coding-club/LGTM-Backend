package swm.hkcc.LGTM.app.modules.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotSeniorMember;

@Service
@Transactional
public class MemberValidator {

    public void validateSenior(Member writer) {
        if (writer.getSenior() == null)
            throw new NotSeniorMember();
    }
}
