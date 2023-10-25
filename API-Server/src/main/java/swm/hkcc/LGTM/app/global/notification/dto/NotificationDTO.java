package swm.hkcc.LGTM.app.global.notification.dto;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private Long targetMemberId;
    Map<String, String> data;

    public static NotificationDTO from(
            Long targetMemberId,
            Map<String, String> data
    ) {
        return NotificationDTO.builder()
                .targetMemberId(targetMemberId)
                .data(data)
                .build();
    }
}
