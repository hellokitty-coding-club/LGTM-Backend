package swm.hkcc.LGTM.app.global.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.global.notification.dto.NotificationDTO;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;

    public void sendNotification(
            Long targetMemberId,
            Map<String, String> data
    ) {
        NotificationDTO request = NotificationDTO.from(targetMemberId, data);
        sendNotification(request);
    }

    public void sendNotification(NotificationDTO request) {
        Member member = memberRepository.findById(request.getTargetMemberId()).orElseThrow(NotExistMember::new);

        if (member.getDeviceToken() != null) {
            Message message = Message.builder()
                    .setToken(member.getDeviceToken())
                    .putAllData(request.getData())
                    .build();

            try {
                firebaseMessaging.send(message);
            } catch (FirebaseMessagingException e) {
                Sentry.captureException(e);
                e.printStackTrace();
                log.error("FCM - " + e.getMessage());
                log.error("device token - {}", member.getDeviceToken());
            }
        }
    }
}
