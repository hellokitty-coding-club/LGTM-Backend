package swm.hkcc.consumer.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.consumer.app.domain.UserLog;

public interface UserDataLogRepository extends JpaRepository<UserLog, Long> {
}
