package swm.hkcc.LGTM.app.modules.notification.dto;

import lombok.Data;

@Data
public class NewNotificationDTO {
    private boolean hasNewNotification;

    public static NewNotificationDTO of(boolean hasNewNotification) {
        NewNotificationDTO newNotificationDTO = new NewNotificationDTO();
        newNotificationDTO.hasNewNotification = hasNewNotification;
        return newNotificationDTO;
    }
}
