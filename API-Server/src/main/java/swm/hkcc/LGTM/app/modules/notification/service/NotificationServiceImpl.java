package swm.hkcc.LGTM.app.modules.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.notification.domain.Notification;
import swm.hkcc.LGTM.app.modules.notification.repository.NotificationRepository;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
@Profile({"dev", "prod"})
public class NotificationServiceImpl implements NotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public void sendNotification(
            Long targetMemberId,
            Map<String, String> data
    ) {
        Member member = memberRepository.findById(targetMemberId).orElseThrow(NotExistMember::new);
        sendNotification(member, data);
        saveNotification(member, data);
    }

    @Override
    public void broadcast(Map<String, String> data) {
        List<Member> members =  memberRepository.findAll();
        members.forEach(member -> sendNotification(member, data));
        members.forEach(member -> saveNotification(member, data));
    }

    private void sendNotification(
            Member member,
            Map<String, String> data
    ) {
        if (member.getDeviceToken() == null) return;

        Message message = Message.builder()
                .setToken(member.getDeviceToken())
                .putAllData(data)
                .build();
        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            Sentry.captureException(e);
            log.error("FCM - " + e.getMessage());
            log.error("device token - {}", member.getDeviceToken());
        }
    }

    private void saveNotification(
            Member member,
            Map<String, String> data
    ) {
        Notification notification = Notification.from(member, data);
        notificationRepository.save(notification);
    }
}
