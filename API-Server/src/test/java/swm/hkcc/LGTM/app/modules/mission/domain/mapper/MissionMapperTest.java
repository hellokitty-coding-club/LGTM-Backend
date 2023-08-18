package swm.hkcc.LGTM.app.modules.mission.domain.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionStatus;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailsDto;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MissionMapperTest {

    private Mission missionSample;
    private List<TechTag> techTagSampleList;

    @BeforeEach
    void setUp() {
        techTagSampleList = Arrays.asList(new TechTag(1L, "Java"), new TechTag(2L,"Spring"));

        missionSample = Mission.builder()
                .missionId(1L)
                .title("Test Title")
                .registrationDueDate(LocalDate.now().plusDays(5))
                .price(100)
                .maxPeopleNumber(50)
                .description("Test Description")
                .missionRepositoryUrl("Test Repository URL")
                .missionStatus(MissionStatus.MISSION_PROCEEDING)
                .reomnnandTo("Developers")
                .notReomnnandTo("Designers")
                .build();
    }

    @Test
    @DisplayName("Mission 객체를 MissionDto로 변환하는 테스트")
    void testMissionToMissionDto() {
        MissionDto result = MissionMapper.missionToMissionDto(missionSample, techTagSampleList);

        assertEquals(missionSample.getMissionId(), result.getMissionId());
        assertEquals(missionSample.getTitle(), result.getMissionTitle());
        assertEquals(techTagSampleList, result.getTechTagList());
    }

    @Test
    @DisplayName("Mission 객체를 MissionDetailDto로 변환하는 테스트")
    void testMissionToMissionDetailDto() {
        int viewCount = 100;
        int currentPeopleNumber = 25;
        boolean isScraped = true;
        int scrapCount = 30;

        MissionDetailsDto result = MissionMapper.missionToMissionDetailDto(missionSample, techTagSampleList, viewCount, currentPeopleNumber, isScraped, scrapCount);

        assertEquals(missionSample.getMissionId(), result.getMissionId());
        assertEquals(missionSample.getTitle(), result.getMissionTitle());
        assertEquals(techTagSampleList, result.getTechTagList());
        assertEquals(5, result.getRemainingRegisterDays());
        assertEquals(missionSample.getPrice(), result.getPrice());
        assertEquals(viewCount, result.getViewCount());
        assertEquals(currentPeopleNumber, result.getCurrentPeopleNumber());
        assertEquals(missionSample.getMaxPeopleNumber(), result.getMaxPeopleNumber());
        assertEquals(isScraped, result.isScraped());
        assertEquals(scrapCount, result.getScrapCount());
    }
}
