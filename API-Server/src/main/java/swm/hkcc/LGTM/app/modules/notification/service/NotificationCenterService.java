package swm.hkcc.LGTM.app.modules.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.notification.domain.Notification;
import swm.hkcc.LGTM.app.modules.notification.dto.NotificationDTO;
import swm.hkcc.LGTM.app.modules.notification.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationCenterService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public List<NotificationDTO> getNotification(Member member) {
        List<Notification> notifications = notificationRepository.findAllByMember(member);
        List<Notification> unreadNotifications = new ArrayList<>();
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        notifications.stream()
                .forEach(notification -> {
                    notificationDTOList.add(NotificationDTO.from(notification));
                    if (!notification.isRead()) {
                        notification.setRead(true);
                        unreadNotifications.add(notification);
                    }
                });

        if (!unreadNotifications.isEmpty())
            notificationRepository.saveAll(unreadNotifications);

        return notificationDTOList;
    }

    public boolean getNewNotification(Member member) {
        return notificationRepository.hasNewNotification(member);
    }
}
