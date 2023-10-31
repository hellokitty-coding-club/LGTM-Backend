package swm.hkcc.LGTM.app.modules.mission.dto;

import lombok.Builder;
import lombok.Getter;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.util.List;


@Getter
@Builder
public class MissionDto {
    private Long missionId;
    private String missionTitle;
    private List<TechTag> techTagList;
    private String missionCategory;
}
