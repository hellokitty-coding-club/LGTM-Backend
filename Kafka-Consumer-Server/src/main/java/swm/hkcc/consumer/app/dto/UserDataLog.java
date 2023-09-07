package swm.hkcc.consumer.app.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

import java.util.Map;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "UserDataLog")
public class UserDataLog {

    @DynamoDBHashKey
    private String logId;

    @DynamoDBAttribute
    private String eventLogName;

    @DynamoDBAttribute
    private String screenName;

    @DynamoDBAttribute
    private Integer logVersion;

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = ObjectJsonConverter.class)
    private Map<String, Object> logData;

    @DynamoDBAttribute
    private String topic;

    @DynamoDBAttribute
    private int partition;

    @DynamoDBAttribute
    private long receivedTimeStamp;
}
