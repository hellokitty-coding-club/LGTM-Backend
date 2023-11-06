package swm.hkcc.LGTM.app.modules.notification.repository;

import swm.hkcc.LGTM.app.modules.member.domain.Member;

public interface NotificationCustomRepository {
    boolean hasNewNotification(Member member);
}
