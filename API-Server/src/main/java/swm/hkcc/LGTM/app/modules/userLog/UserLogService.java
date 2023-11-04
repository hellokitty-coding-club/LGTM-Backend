package swm.hkcc.LGTM.app.modules.userLog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.ABTest;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.ABTestService;
import swm.hkcc.LGTM.app.modules.userLog.dto.LogMessage;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserLogService {
    private final ABTestService abTestService;

    public void addLogMessage(LogMessage logMessage) {
        addServerTime(logMessage);
        addAbTestInfo(logMessage);
    }

    private void addServerTime(LogMessage logMessage) {
        logMessage.getLogData().put("serverTimeMs", System.currentTimeMillis());
    }


    private void addAbTestInfo(LogMessage logMessage) {
        Long memberId = (Long) logMessage.getLogData().get("userID");

        for (ABTest abTest : ABTest.values()) {
            String ABTestGroupName = abTestService.getGroupName(memberId, abTest.getTestName());
            logMessage.getLogData().put(abTest.getTestName(), ABTestGroupName);
        }
    }

}
