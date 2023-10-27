package swm.hkcc.LGTM.app.modules.notification.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.notification.domain.Notification;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private Long notificationId;
    private String title;
    private String body;
    private Boolean isRead;

    public static NotificationDTO from(Notification notification) {
        return NotificationDTO.builder()
                .notificationId(notification.getNotificationId())
                .title(notification.getTitle())
                .body(notification.getBody())
                .isRead(notification.isRead())
                .build();
    }
}
