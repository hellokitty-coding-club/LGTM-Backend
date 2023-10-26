package swm.hkcc.LGTM.app.global.notification.service;

import java.util.Map;

public interface NotificationService {
    void sendNotification(
            Long targetMemberId,
            Map<String, String> data
    );

    void broadcast(Map<String, String> data);
}
