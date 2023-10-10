package swm.hkcc.LGTM.app.modules.userLog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.userLog.dto.LogMessage;
import swm.hkcc.LGTM.app.modules.userLog.producer.LogProducer;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("v1/log")
public class UserLogController {
    private final LogProducer logProducer;

    @PostMapping
    public ApiDataResponse<?> userlog(
            @RequestBody LogMessage logMessage
    ) {
        String topic = logProducer.sendMessage(logMessage);

        return ApiDataResponse.of(topic);
    }
}
