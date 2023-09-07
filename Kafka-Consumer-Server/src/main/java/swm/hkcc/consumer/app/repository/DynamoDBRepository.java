package swm.hkcc.consumer.app.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import swm.hkcc.consumer.app.dto.UserDataLog;

@EnableScan
public interface DynamoDBRepository extends CrudRepository<UserDataLog, String> {
}
