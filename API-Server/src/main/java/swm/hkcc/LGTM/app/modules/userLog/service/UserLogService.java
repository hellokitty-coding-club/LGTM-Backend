package swm.hkcc.LGTM.app.modules.userLog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.userLog.domain.UserLog;
import swm.hkcc.LGTM.app.modules.userLog.dto.LogMessage;
import swm.hkcc.LGTM.app.modules.userLog.repository.UserDataLogRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLogService {
    private final UserDataLogRepository userDataLogRepository;

    public void saveLog(LogMessage logMessage) {
        UserLog userLog = UserLog.from(logMessage);
        userDataLogRepository.save(userLog);
    }
}
