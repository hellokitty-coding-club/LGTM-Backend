package swm.hkcc.LGTM.app.global.api;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.hkcc.LGTM.app.global.api.dto.IntroResponse;
import swm.hkcc.LGTM.app.global.api.dto.PushTestRequest;
import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.global.exception.GeneralException;
import swm.hkcc.LGTM.app.modules.notification.service.NotificationService;

import java.util.Optional;

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

    @PostMapping("/push")
    public ApiDataResponse<Long> pushTest(
            @RequestParam(required = false) Optional<Long> targetMemberId,
            @RequestParam(required = false) Optional<Boolean> isBroadcast,
            @RequestBody @Validated PushTestRequest request
    ) {

        if (isBroadcast.orElse(false)) {
            notificationService.broadcast(request.getData());
            return ApiDataResponse.of(0L);
        }
        return targetMemberId.map(id -> {
            notificationService.sendNotification(id, request.getData());
            return ApiDataResponse.of(id);
        }).orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST, "targetMemberId 또는 isBroadcast 둘 중 하나는 필수입니다."));
    }
}
