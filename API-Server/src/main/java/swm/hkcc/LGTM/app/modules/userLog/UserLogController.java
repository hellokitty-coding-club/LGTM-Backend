package swm.hkcc.LGTM.app.modules.userLog;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.userLog.dto.TimeIntervalLogMessage;
import swm.hkcc.LGTM.app.modules.userLog.dto.TimestampLogMessage;
import swm.hkcc.LGTM.app.modules.userLog.dto.UserLogRequest;
import swm.hkcc.LGTM.app.modules.userLog.producer.IntervalLogProducer;
import swm.hkcc.LGTM.app.modules.userLog.producer.StampLogProducer;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("v1/log")
public class UserLogController {
    private final StampLogProducer stampLogProducer;
    private final IntervalLogProducer intervalLogProducer;

    @PostMapping
    public ApiDataResponse<?> timestampLog(
            @RequestBody @Valid UserLogRequest userLogRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long memberId = customUserDetails.getMemberId();

        String topic = "";
        if (userLogRequest.getStayIntervalMs() == null) {
            topic = stampLogProducer.sendMessage(
                    TimestampLogMessage.from(userLogRequest, memberId, LocalDateTime.now())
            );
        } else {
            topic = intervalLogProducer.sendMessage(
                    TimeIntervalLogMessage.from(userLogRequest, memberId, LocalDateTime.now())
            );
        }

        return ApiDataResponse.of(topic);
    }
}
