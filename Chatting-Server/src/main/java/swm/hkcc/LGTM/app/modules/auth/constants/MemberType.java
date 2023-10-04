package swm.hkcc.LGTM.app.modules.auth.constants;

import swm.hkcc.LGTM.app.modules.auth.exception.UnspecifiedMemberType;
import swm.hkcc.LGTM.app.modules.member.domain.Member;

public enum MemberType {
    JUNIOR, SENIOR;

    public static MemberType getType(Member member) {
        if (member.getSenior() != null) {
            return SENIOR;
        } else if (member.getJunior() != null) {
            return JUNIOR;
        } else {
            throw new UnspecifiedMemberType();
        }
    }
}
