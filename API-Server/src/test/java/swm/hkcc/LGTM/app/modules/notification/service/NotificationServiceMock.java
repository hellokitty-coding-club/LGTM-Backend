package swm.hkcc.LGTM.app.modules.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.notification.service.NotificationService;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
@Profile("test")
public class NotificationServiceMock implements NotificationService {

    public void sendNotification(
            Long targetMemberId,
            Map<String, String> data
    ) {
    }

    public void broadcast(Map<String, String> data) {
    }
}
