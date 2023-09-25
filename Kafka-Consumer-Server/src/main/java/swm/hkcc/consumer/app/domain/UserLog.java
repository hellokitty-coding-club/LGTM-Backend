package swm.hkcc.consumer.app.domain;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.Map;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    private String eventLogName;

    private String screenName;

    private Integer logVersion;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> logData;

    private String topic;

    private int partitionNumber;

    private Long receivedTimeStamp;
}
