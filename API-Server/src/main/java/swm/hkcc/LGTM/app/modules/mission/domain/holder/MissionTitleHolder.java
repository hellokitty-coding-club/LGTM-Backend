package swm.hkcc.LGTM.app.modules.mission.domain.holder;

import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionTitleDto;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContent;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.Theme;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ViewType;

import java.util.Map;

@Component
public class MissionTitleHolder {

    private static final Map<MissionContentType, MissionTitleDetails> TITLE_DETAILS_MAP = Map.of(
            MissionContentType.ON_GOING_MISSION_TITLE, MissionTitleDetails.of("진행중인 미션", Theme.DARK),
            MissionContentType.RECOMMENDED_MISSION_TITLE, MissionTitleDetails.of("맞춤 추천 미션 목록이에요", Theme.LIGHT),
            MissionContentType.TOTAL_MISSION_TITLE, MissionTitleDetails.of("더 많은 미션 찾아보기", Theme.LIGHT)
    );

    public ServerDrivenContent getMissionTitle(MissionContentType missionContentType) {
        MissionTitleDetails details = TITLE_DETAILS_MAP.get(missionContentType);

        if (details == null) {
            return null;
        }

        MissionTitleDto missionTitleDto = MissionTitleDto.builder()
                .title(details.getTitle())
                .build();
        return ServerDrivenContent.from(missionTitleDto, details.getTheme(), ViewType.TITLE);
    }

    private static class MissionTitleDetails {
        private final String title;
        private final Theme theme;

        private MissionTitleDetails(String title, Theme theme) {
            this.title = title;
            this.theme = theme;
        }

        public static MissionTitleDetails of(String title, Theme theme) {
            return new MissionTitleDetails(title, theme);
        }

        public String getTitle() {
            return title;
        }

        public Theme getTheme() {
            return theme;
        }
    }
}
