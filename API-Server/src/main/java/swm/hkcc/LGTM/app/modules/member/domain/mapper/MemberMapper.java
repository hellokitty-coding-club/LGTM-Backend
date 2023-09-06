package swm.hkcc.LGTM.app.modules.member.domain.mapper;

import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.dto.MemberDetailInfo;
import swm.hkcc.LGTM.app.modules.member.dto.MemberDetailProfile;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.util.List;

public class MemberMapper {

    public static MemberDetailProfile toMemberDetailProfile(Member member, MemberDetailInfo memberDetailInfo, List<TechTag> techTagList, List<MissionDto> missionDtos) {
        return MemberDetailProfile.builder()
                .memberId(member.getMemberId())
                .githubId(member.getGithubId())
                .nickName(member.getNickName())
                .profileImageUrl(member.getProfileImageUrl())
                .introduction(member.getIntroduction())
                .techTagList(techTagList)
                .isAgreeWithEventInfo(member.isAgreeWithEventInfo())
                .memberDetailInfo(memberDetailInfo)
                .memberMissionHistory(missionDtos)
                .build();
    }
}
