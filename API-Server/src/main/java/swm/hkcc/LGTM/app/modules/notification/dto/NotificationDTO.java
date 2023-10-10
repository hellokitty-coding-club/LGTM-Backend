package swm.hkcc.LGTM.app.modules.notification.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private Long targetMemberId;
    private String title;
    private String body;
}
