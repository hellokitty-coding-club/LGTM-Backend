package swm.hkcc.LGTM.app.modules.notification.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.notification.domain.Notification;

import java.util.List;

import static swm.hkcc.LGTM.app.modules.notification.domain.QNotification.notification;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean hasNewNotification(Member member) {
        List<Notification> notifications
                = jpaQueryFactory
                .select(notification)
                .from(notification)
                .where(notification.member.eq(member)
                        .and(notification.isRead.eq(false)))
                .fetch();
        log.info("notifications: {}", notifications.size());
        return !notifications
                .isEmpty();
    }
}
