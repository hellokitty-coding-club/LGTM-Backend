package swm.hkcc.LGTM.app.modules.mission.constant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import swm.hkcc.LGTM.app.modules.auth.constants.MemberType;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.ViewType;

import static org.assertj.core.api.Assertions.assertThat;

class MissionContentTypeTest {

    @ParameterizedTest
    @EnumSource(MissionContentType.class)
    @DisplayName("ViewType.TITLE인 경우 getTitleName() 메서드를 호출하면 알맞은 문자열을 반환한다.")
    void getTitleNameTest(MissionContentType missionContentType) {
        if (
                missionContentType.getViewType() != ViewType.TITLE &&
                        missionContentType.getViewType() != ViewType.SUB_ITEM
        ) return;

        // when
        String titleNameJunior = missionContentType.getTitleName(MemberType.JUNIOR);
        String titleNameSenior = missionContentType.getTitleName(MemberType.SENIOR);

        // then
        if (missionContentType.getTitleName() == null) {
            assertThat(titleNameJunior).isEqualTo(missionContentType.getTitleNameForJunior());
            assertThat(titleNameSenior).isEqualTo(missionContentType.getTitleNameForSenior());
        } else {
            assertThat(titleNameJunior).isEqualTo(missionContentType.getTitleName());
            assertThat(titleNameSenior).isEqualTo(missionContentType.getTitleName());
        }

    }
}