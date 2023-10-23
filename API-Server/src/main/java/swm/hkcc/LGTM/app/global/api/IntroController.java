package swm.hkcc.LGTM.app.global.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.api.dto.IntroResponse;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.global.notification.service.NotificationService;

import java.util.Map;

@RestController
@RequestMapping("/v1/intro")
@RequiredArgsConstructor
public class IntroController {
    private final NotificationService notificationService;

    @GetMapping
    public ApiDataResponse<IntroResponse> getIntro() {
        // 임시로 100,100 넣어둠
        return ApiDataResponse.of(new IntroResponse(100, 100));
    }

    @GetMapping("/push")
    public ApiDataResponse<IntroResponse> pushTest(
            @RequestParam Long targetMemberId
    ) {
        Map<String, String> data = Map.of(
                "title", "테스트 푸시 제목",
                "body", "테스트 푸시 본문",
                "isFromPush", "true"
        );
        notificationService.sendNotification(targetMemberId, data);
        // 임시로 100,100 넣어둠
        return ApiDataResponse.of(new IntroResponse(100, 100));
    }
}
