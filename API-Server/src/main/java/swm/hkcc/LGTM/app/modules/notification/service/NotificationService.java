package swm.hkcc.LGTM.app.modules.notification.service;

import java.util.Map;

public interface NotificationService {
    void sendNotification(
            Long targetMemberId,
            Map<String, String> data
    );

    void broadcast(Map<String, String> data);
}
