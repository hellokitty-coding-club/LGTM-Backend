package swm.hkcc.LGTM.app.modules.mission.domain.mapper;

import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.Senior;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.dto.MemberProfile;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailViewResponse;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailsDto;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MissionMapper {
    public static MissionDto missionToMissionDto(Mission ongoingMission, List<TechTag> techTags) {
        return MissionDto.builder()
                .missionId(ongoingMission.getMissionId())
                .missionTitle(ongoingMission.getTitle())
                .techTagList(techTags)
                .build();
    }

    public static MissionDetailsDto missionToMissionDetailDto(Mission mission, List<TechTag> techTags, int viewCount, int currentPeopleNumber, boolean isScraped, int scrapCount) {
        return MissionDetailsDto.builder()
                .missionId(mission.getMissionId())
                .missionTitle(mission.getTitle())
                .techTagList(techTags)
                .remainingRegisterDays((int) ChronoUnit.DAYS.between(LocalDate.now(), mission.getRegistrationDueDate()))
                .viewCount(viewCount)
                .price(mission.getPrice())
                .currentPeopleNumber(currentPeopleNumber)
                .maxPeopleNumber(mission.getMaxPeopleNumber())
                .isScraped(isScraped)
                .scrapCount(scrapCount)
                .build();
    }

    public static MissionDetailViewResponse missionAndMemberToDetailView(Mission mission, boolean isScraped, Senior missionWriter, List<TechTag> techTagList, int currentPeopleNumber, String memberType) {
        Member member = missionWriter.getMember();
        return MissionDetailViewResponse.builder()
                .missionId(mission.getMissionId())
                .missionStatus(mission.getMissionStatus().name())
                .missionTitle(mission.getTitle())
                .techTagList(techTagList)
                .missionRepositoryUrl(mission.getMissionRepositoryUrl())
                .registrationDueDate(mission.getRegistrationDueDate())
                .maxPeopleNumber(mission.getMaxPeopleNumber())
                .currentPeopleNumber(currentPeopleNumber)
                .price(mission.getPrice())
                .description(mission.getDescription())
                .recommendTo(mission.getRecommendTo())
                .notRecommendTo(mission.getNotRecommendTo())
                .isScraped(isScraped)
                .memberType(memberType)
                .memberProfile(MemberProfile.builder()
                        .memberId(member.getMemberId())
                        .nickName(member.getNickName())
                        .profileImageUrl(member.getProfileImageUrl())
                        .githubId(member.getGithubId())
                        .company(missionWriter.getCompanyInfo())
                        .build())
                .build();
    }

}
