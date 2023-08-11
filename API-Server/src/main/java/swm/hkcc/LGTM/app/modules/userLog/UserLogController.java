package swm.hkcc.LGTM.app.modules.userLog;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.userLog.dto.TimestampLogMessage;
import swm.hkcc.LGTM.app.modules.userLog.dto.UserLogRequest;
import swm.hkcc.LGTM.app.modules.userLog.producer.UserLogProducer;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("v1/log")
public class UserLogController {
    private final UserLogProducer userLogProducer;

//    @PostMapping("/simple")
//    public String simpleLog() {
//        userLogProducer.sendMessage("simple log");
//        return "complete";
//    }

    @PostMapping("/timestamp")
    public String timestampLog(
            @RequestBody @Valid UserLogRequest userLogRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long memberId = customUserDetails.getMemberId();

        if (userLogRequest.getStayIntervalMs() == null) {
            userLogProducer.sendMessage(
                    TimestampLogMessage.from(userLogRequest, memberId, LocalDateTime.now())
            );
        } else {
            ;
        }

        return "complete";
    }
}
