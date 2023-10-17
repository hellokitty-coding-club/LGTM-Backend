package swm.hkcc.LGTM.app.modules.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.util.List;


@AllArgsConstructor
@Getter
@Builder
public class MissionDetailsDto {
    private Long missionId;
    private String missionTitle;
    private List<TechTag> techTagList;
    private int remainingRegisterDays;
    private int viewCount;
    private int price;
    private int currentPeopleNumber;
    private int maxPeopleNumber;
    private boolean isScraped;
    private int scrapCount;
    private String missionCategory;
}
