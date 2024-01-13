package swm.hkcc.LGTM.app.modules.mission.domain.mapper;

import swm.hkcc.LGTM.app.modules.auth.constants.MemberType;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.Senior;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.dto.*;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static swm.hkcc.LGTM.app.modules.mission.domain.MissionStatus.MISSION_FINISHED;

public class MissionMapper {
    public static MissionDto missionToMissionDto(Mission mission, List<TechTag> techTags, String missionCategory) {
        return MissionDto.builder()
                .missionId(mission.getMissionId())
                .missionTitle(mission.getTitle())
                .techTagList(techTags)
                .missionCategory(missionCategory)
                .build();
    }

    public static MissionDtoV2 missionToMissionDto(Mission mission, List<TechTag> techTags) {
        return MissionDtoV2.builder()
                .missionId(mission.getMissionId())
                .missionTitle(mission.getTitle())
                .techTagList(techTags)
                .build();
    }

    public static MissionDetailsDto missionToMissionDetailDto(Mission mission, List<TechTag> techTags, int viewCount, int currentPeopleNumber, boolean isScraped, int scrapCount, String missionCategory) {
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
                .missionCategory(missionCategory)
                .build();
    }

    public static MissionDetailViewResponse missionAndMemberToDetailView(Mission mission, boolean isScraped, Senior missionWriter, List<TechTag> techTagList, int currentPeopleNumber, MemberType memberType,  boolean isParticipated) {
        Member member = missionWriter.getMember();
        boolean isClosed = mission.getMissionStatus().equals(MISSION_FINISHED);
        return MissionDetailViewResponse.builder()
                .missionId(mission.getMissionId())
                .missionStatus(mission.getMissionStatus().name())
                .missionTitle(mission.getTitle())
                .techTagList(techTagList)
                .remainingRegisterDays((int) ChronoUnit.DAYS.between(LocalDate.now(), mission.getRegistrationDueDate()))
                .missionRepositoryUrl(mission.getMissionRepositoryUrl())
                .registrationDueDate(mission.getRegistrationDueDate())
                .maxPeopleNumber(mission.getMaxPeopleNumber())
                .currentPeopleNumber(currentPeopleNumber)
                .price(mission.getPrice())
                .createdAt(mission.getCreatedAt())
                .description(mission.getDescription())
                .recommendTo(mission.getRecommendTo())
                .notRecommendTo(mission.getNotRecommendTo())
                .isScraped(isScraped)
                .memberType(memberType.toString())
                .memberProfile(MemberProfile.builder()
                        .memberId(member.getMemberId())
                        .nickName(member.getNickName())
                        .profileImageUrl(member.getProfileImageUrl())
                        .githubId(member.getGithubId())
                        .company(missionWriter.getCompanyInfo())
                        .position(missionWriter.getPosition())
                        .build())
                .isParticipated(isParticipated)
                .isClosed(isClosed)
                .build();
    }

}
