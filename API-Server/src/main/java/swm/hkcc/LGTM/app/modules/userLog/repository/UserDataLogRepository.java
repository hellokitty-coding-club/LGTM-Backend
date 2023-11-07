package swm.hkcc.LGTM.app.modules.userLog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.userLog.domain.UserLog;

public interface UserDataLogRepository extends JpaRepository<UserLog, Long> {
}
