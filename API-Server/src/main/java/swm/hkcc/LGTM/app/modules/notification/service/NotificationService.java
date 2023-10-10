package swm.hkcc.LGTM.app.modules.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.notification.dto.NotificationDTO;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;

    public void sendNotification(NotificationDTO request) {
        Member member = memberRepository.findById(request.getTargetMemberId()).orElseThrow(NotExistMember::new);

        if (member.getDeviceToken() != null) {
            Notification notification = Notification.builder()
                    .setTitle(request.getTitle())
                    .setBody(request.getBody())
                    .build();

            Message message = Message.builder()
                    .setToken(member.getDeviceToken())
                    .setNotification(notification)
                    .build();

            try {
                firebaseMessaging.send(message);
            } catch (FirebaseMessagingException e) {
                Sentry.captureException(e);
                log.error("FCM - " + e.getMessage());
                log.error("device token - {}", member.getDeviceToken());
            }
        }
        // todo : member.getDeviceToken() 없는 경우
    }
}
