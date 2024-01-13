package swm.hkcc.LGTM.app.modules.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import swm.hkcc.LGTM.app.modules.notification.domain.Notification;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private Long notificationId;
    private String title;
    private String body;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationDTO from(Notification notification) {
        return NotificationDTO.builder()
                .notificationId(notification.getNotificationId())
                .title(notification.getTitle())
                .body(notification.getBody())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
