package swm.hkcc.consumer.app.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import swm.hkcc.consumer.app.dto.UserDataLog;

@Repository
@RequiredArgsConstructor
public class UserDataLogRepository {
    private final DynamoDBRepository dynamoDBRepository;

    public void saveLog(UserDataLog userDataLog) {
        dynamoDBRepository.save(userDataLog);
    }
}
