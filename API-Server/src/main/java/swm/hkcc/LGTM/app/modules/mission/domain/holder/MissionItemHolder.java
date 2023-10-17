package swm.hkcc.LGTM.app.modules.mission.domain.holder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.auth.constants.MemberType;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentData;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionEmptyViewTypeDto;
import swm.hkcc.LGTM.app.modules.mission.service.MissionService;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContent;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.ViewType;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class MissionItemHolder {

    public static final String TOTAL_MISSION_EMPTY_VIEW = "total_mission_empty_view";
    public static final String ONGOING_MISSION_EMPTY_VIEW = "ongoing_mission_empty_view";
    public static final String RECOMMENDED_MISSION_EMPTY_VIEW = "recommended_mission_empty_view";

    private final MissionService missionService;

    public Function<Long, MissionContentData> getMissionListFunction(MissionContentType missionContentType, MemberType memberType) {
        return switch (missionContentType) {
            case TOTAL_MISSION_LIST_V1 -> missionService::getTotalMissions;
            case RECOMMENDED_MISSION_LIST_V1 -> missionService::getRecommendMissions;
            case ON_GOING_MISSION_LIST_V1 -> switch (memberType) {
                case JUNIOR -> missionService::getJuniorOngoingMissions;
                case SENIOR -> missionService::getSeniorOngoingMissions;
                default -> null;
            };
            default -> null;
        };
    }

    public ServerDrivenContent getMissionEmptyView(MissionContentType missionContentType) {
        return switch (missionContentType) {
            case TOTAL_MISSION_LIST_V1 -> ServerDrivenContent.from(convertToDto(TOTAL_MISSION_EMPTY_VIEW), missionContentType.getTheme(), ViewType.EMPTY);
            case ON_GOING_MISSION_LIST_V1 -> ServerDrivenContent.from(convertToDto(ONGOING_MISSION_EMPTY_VIEW), missionContentType.getTheme(), ViewType.EMPTY);
            case RECOMMENDED_MISSION_LIST_V1 -> ServerDrivenContent.from(convertToDto(RECOMMENDED_MISSION_EMPTY_VIEW), missionContentType.getTheme(), ViewType.EMPTY);
            default -> null;
        };
    }

    private MissionEmptyViewTypeDto convertToDto(String emptyViewTypeName) {
        return MissionEmptyViewTypeDto.builder()
                .emptyViewTypeName(emptyViewTypeName)
                .build();
    }

}
