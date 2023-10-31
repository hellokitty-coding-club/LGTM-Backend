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
        addMissionCategoryInfo(logMessage);
    }

    private void addServerTime(LogMessage logMessage) {
        logMessage.getLogData().put("serverTime", System.currentTimeMillis());
    }


    private void addAbTestInfo(LogMessage logMessage) {
        Long memberId = (Long) logMessage.getLogData().get("userID");

        for (ABTest abTest : ABTest.values()) {
            String ABTestGroupName = abTestService.getGroupName(memberId, abTest.getTestName());
            logMessage.getLogData().put(abTest.getTestName(), ABTestGroupName);
        }
    }

    private void addMissionCategoryInfo(LogMessage logMessage) {
        if (!logMessage.getLogData().containsKey("sduiList") || !logMessage.getLogData().containsKey("clickedIndex"))
            return;

        try {
            List<Object> sduiList = (List<Object>) logMessage.getLogData().get("sduiList");
            int clickedIndex = (int) logMessage.getLogData().get("clickedIndex");

            String category = "";
            for (int i = 0; i < clickedIndex; i++) {
                Map<String, Object> sdui = (Map<String, Object>) sduiList.get(i);
                if (sdui.get("viewType").equals("TITLE"))
                    //{"theme": "WHITE", "content": {"title": "진행 중인 미션"}, "viewType": "TITLE"}
                    category = (String) ((Map<String, Object>) sdui.get("content")).get("title");
            }
            logMessage.getLogData().put("clickedCategory", category);
        } catch (Exception e) {
            return;
        }
    }
}
