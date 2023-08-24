package swm.hkcc.LGTM.app.modules.registration.mapper;

import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.dto.MemberRegisterSimpleInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.RegistrationSeniorResponse;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.util.List;

public class RegistrationMapper {

    public static RegistrationSeniorResponse toRegistrationSeniorResponse(
            Mission mission,
            List<TechTag> techTagList,
            List<MemberRegisterSimpleInfo> memberInfoList
    ) {

        return RegistrationSeniorResponse.builder()
                .missionName(mission.getTitle())
                .techTagList(techTagList)
                .memberInfoList(memberInfoList)
                .build();
    }
}
