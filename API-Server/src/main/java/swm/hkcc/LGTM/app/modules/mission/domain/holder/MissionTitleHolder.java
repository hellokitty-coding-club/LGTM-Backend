package swm.hkcc.LGTM.app.modules.mission.domain.holder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionTitleDto;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContent;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.Theme;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ViewType;

import java.util.Map;
import java.util.Optional;

@Component
public class MissionTitleHolder {
    private static final String ON_GOING_MISSION_TITLE_NAME = "진행 중인 미션";
    private static final String RECOMMENDED_MISSION_TITLE_NAME = "맞춤 추천 미션 목록이에요";
    private static final String TOTAL_MISSION_TITLE_NAME = "더 많은 미션 찾아보기";

    private static final Map<MissionContentType, MissionTitleDetails> TITLE_DETAILS_MAP = Map.of(
            MissionContentType.ON_GOING_MISSION_TITLE, MissionTitleDetails.of(ON_GOING_MISSION_TITLE_NAME, Theme.DARK),
            MissionContentType.RECOMMENDED_MISSION_TITLE, MissionTitleDetails.of(RECOMMENDED_MISSION_TITLE_NAME, Theme.LIGHT),
            MissionContentType.TOTAL_MISSION_TITLE, MissionTitleDetails.of(TOTAL_MISSION_TITLE_NAME, Theme.LIGHT)
    );

    public ServerDrivenContent getMissionTitle(MissionContentType missionContentType) {
        MissionTitleDetails details = Optional.ofNullable(TITLE_DETAILS_MAP.get(missionContentType))
                .orElseThrow(() -> new IllegalArgumentException("Invalid MissionContentType: " + missionContentType));

        MissionTitleDto missionTitleDto = MissionTitleDto.builder()
                .title(details.getTitle())
                .build();
        return ServerDrivenContent.from(missionTitleDto, details.getTheme(), ViewType.TITLE);
    }

    @Getter
    @RequiredArgsConstructor
    private static class MissionTitleDetails {
        private final String title;
        private final Theme theme;

        public static MissionTitleDetails of(String title, Theme theme) {
            return new MissionTitleDetails(title, theme);
        }
    }
}
