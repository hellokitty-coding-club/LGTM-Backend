package swm.hkcc.consumer.app.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommonMessageTest {

    // getMessageByKey
    // key: timestamp
    @Test
    void getMessageByKey_동작_timestamp() {
        CommonMessage commonMessage = CommonMessage.builder()
                .eventLogType("eventLogType")
                .screenName("screenName")
                .target("target")
                .missionId(1L)
                .memberId(1L)
                .timestamp(LocalDateTime.MAX)
                .build();

        TimestampLogMessage timestampLogMessage = (TimestampLogMessage) commonMessage.getMessageByKey("timestamp");
        assertThat(timestampLogMessage.getEventLogType()).isEqualTo("eventLogType");
        assertThat(timestampLogMessage.getScreenName()).isEqualTo("screenName");
        assertThat(timestampLogMessage.getTarget()).isEqualTo("target");
        assertThat(timestampLogMessage.getMissionId()).isEqualTo(1L);
        assertThat(timestampLogMessage.getMemberId()).isEqualTo(1L);
        assertThat(timestampLogMessage.getTimestamp()).isEqualTo(LocalDateTime.MAX);
    }



    // getMessageByKey
    //key: interval
    @Test
    void getMessageByKey_동작_interval() {
        CommonMessage commonMessage = CommonMessage.builder()
                .eventLogType("eventLogType")
                .screenName("screenName")
                .target("target")
                .stayIntervalMs(1000L)
                .missionId(1L)
                .memberId(1L)
                .timestamp(LocalDateTime.MAX)
                .build();

        TimeIntervalLogMessage timeIntervalLogMessage = (TimeIntervalLogMessage) commonMessage.getMessageByKey("interval");
        assertThat(timeIntervalLogMessage.getEventLogType()).isEqualTo("eventLogType");
        assertThat(timeIntervalLogMessage.getScreenName()).isEqualTo("screenName");
        assertThat(timeIntervalLogMessage.getTarget()).isEqualTo("target");
        assertThat(timeIntervalLogMessage.getStayIntervalMs()).isEqualTo(1000L);
        assertThat(timeIntervalLogMessage.getMissionId()).isEqualTo(1L);
        assertThat(timeIntervalLogMessage.getMemberId()).isEqualTo(1L);
        assertThat(timeIntervalLogMessage.getTimestamp()).isEqualTo(LocalDateTime.MAX);
    }

}