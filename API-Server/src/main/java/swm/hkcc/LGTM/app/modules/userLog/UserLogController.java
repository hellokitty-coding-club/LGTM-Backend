package swm.hkcc.LGTM.app.modules.userLog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.userLog.dto.LogMessage;
import swm.hkcc.LGTM.app.modules.userLog.producer.LogProducer;
import swm.hkcc.LGTM.app.modules.userLog.service.UserLogService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("v1/log")
public class UserLogController {
    private final LogProducer logProducer;
    private final UserLogService userLogService;

    @PostMapping
    public ApiDataResponse<?> userlog(
            @RequestBody LogMessage logMessage
    ) {
//        String topic = logProducer.sendMessage(logMessage);
        userLogService.saveLog(logMessage);
        return ApiDataResponse.of(0);
    }
}
