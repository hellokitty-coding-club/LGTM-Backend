package swm.hkcc.LGTM.app.modules.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.notification.dto.NotificationDTO;
import swm.hkcc.LGTM.app.modules.notification.service.NotificationCenterService;

import java.util.List;

@RestController
@RequestMapping("/v1/notification")
@RequiredArgsConstructor
public class NotificationCenterController {
    private final NotificationCenterService notificationCenterService;

    @GetMapping
    public ApiDataResponse<List<NotificationDTO>> getNotification(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Member member = customUserDetails.getMember();
        return ApiDataResponse.of(notificationCenterService.getNotification(member));
    }
}
