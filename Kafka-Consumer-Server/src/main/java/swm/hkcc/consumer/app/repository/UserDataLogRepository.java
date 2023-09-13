package swm.hkcc.consumer.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import swm.hkcc.consumer.app.dto.UserDataLog;

public interface UserDataLogRepository extends MongoRepository<UserDataLog, String> {
}
