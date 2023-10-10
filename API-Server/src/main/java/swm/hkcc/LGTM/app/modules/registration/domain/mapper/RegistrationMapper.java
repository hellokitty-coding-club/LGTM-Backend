package swm.hkcc.LGTM.app.modules.registration.domain.mapper;

import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MemberRegisterSimpleInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.MissionHistoryInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.RegistrationSeniorResponse;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.JuniorAdditionalInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.RegistrationJuniorResponse;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.AdditionalInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.RegistrationSeniorDetailResponse;
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

    public static RegistrationSeniorDetailResponse toRegistrationSeniorDetailResponse(Mission mission, Member junior, ProcessStatus status, List<MissionHistoryInfo> missionHistory, AdditionalInfo additionalInfo) {
        RegistrationSeniorDetailResponse response = new RegistrationSeniorDetailResponse();

        response.setMemberId(junior.getMemberId());
        response.setNickname(junior.getNickName());
        response.setGithubId(junior.getGithubId());
        response.setStatus(status);
        response.setMissionHistory(missionHistory);
        response.setAdditionalInfo(additionalInfo.getAdditionalInfo());
        response.setButtonTitle(status.getSeniorBottomTitle());

        return response;
    }

    public static RegistrationJuniorResponse toRegistrationJuniorResponse(
            Mission mission,
            List<TechTag> techTagList,
            List<MissionHistoryInfo> missinHistory,
            ProcessStatus status,
            JuniorAdditionalInfo additionalInfo
    ) {
        RegistrationJuniorResponse response = additionalInfo.createResponse();

        response.setMissionName(mission.getTitle());
        response.setTechTagList(techTagList);
        response.setProcessStatus(status);
        response.setMissionHistory(missinHistory);
        response.setButtonTitle(status.getJuniorBottomTitle());

        return response;
    }

}
