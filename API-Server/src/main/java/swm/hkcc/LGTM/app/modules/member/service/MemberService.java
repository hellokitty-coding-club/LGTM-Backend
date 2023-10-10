package swm.hkcc.LGTM.app.modules.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.auth.constants.MemberType;
import swm.hkcc.LGTM.app.modules.member.domain.Junior;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.Senior;
import swm.hkcc.LGTM.app.modules.member.dto.JuniorDetailInfo;
import swm.hkcc.LGTM.app.modules.member.dto.MemberDetailProfile;
import swm.hkcc.LGTM.app.modules.member.dto.SeniorDetailInfo;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.domain.mapper.MissionMapper;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRepository;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionRegistration;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTagPerMember;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMemberRepository;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMissionRepository;

import java.util.List;
import java.util.Optional;

import static swm.hkcc.LGTM.app.modules.member.domain.mapper.MemberMapper.toMemberDetailProfile;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final TechTagPerMemberRepository techTagPerMemberRepository;
    private final MissionRegistrationRepository missionRegistrationRepository;
    private final MissionRepository missionRepository;
    private final TechTagPerMissionRepository techTagPerMissionRepository;

    public Boolean updateDeviceToken(Long memberId, Optional<String> deviceToken) {
        deviceToken.ifPresent(memberRepository::eraseDeviceToken);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotExistMember::new);

        member.setDeviceToken(deviceToken.orElse(null));
        memberRepository.save(member);

        return true;
    }

    public MemberType getMemberType(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotExistMember::new);

        return MemberType.getType(member);
    }

    public MemberDetailProfile getJuniorProfile(Long memberId, MemberType memberType, boolean isMyProfile) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotExistMember::new);
        List<TechTag> techTagList = getTechTagList(memberId);

        Junior junior = member.getJunior();
        List<MissionRegistration> juniorMissionHistory = missionRegistrationRepository.findAllByJuniorMemberIdWithMission(memberId); // N+1 문제 방지를 위해 fetch join 사용

        JuniorDetailInfo juniorDetailInfo = JuniorDetailInfo.builder()
                .educationalHistory(junior.getEducationalHistory())
                .build();

        List<MissionDto> missionDtos = juniorMissionHistory.stream()
                .map(MissionRegistration::getMission)
                .map(mission -> MissionMapper.missionToMissionDto(
                        mission,
                        techTagPerMissionRepository.findTechTagsByMissionId(mission.getMissionId())
                ))
                .toList();

        return toMemberDetailProfile(member, memberType, isMyProfile, juniorDetailInfo, techTagList, missionDtos);
    }

    public MemberDetailProfile getSeniorProfile(Long memberId, MemberType memberType, boolean isMyProfile) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotExistMember::new);
        List<TechTag> techTagList = getTechTagList(memberId);

        Senior senior = member.getSenior();
        List<Mission> seniorMissionHistory = missionRepository.findAllByWriter_MemberId(memberId);

        SeniorDetailInfo seniorDetailInfo = SeniorDetailInfo.builder()
                .companyInfo(senior.getCompanyInfo())
                .careerPeriod(senior.getCareerPeriod())
                .position(senior.getPosition())
                .build();

        List<MissionDto> missionDtos = seniorMissionHistory.stream()
                .map(mission -> MissionMapper.missionToMissionDto(
                        mission,
                        techTagPerMissionRepository.findTechTagsByMissionId(mission.getMissionId())
                ))
                .toList();

        return toMemberDetailProfile(member, memberType, isMyProfile, seniorDetailInfo, techTagList, missionDtos);
    }

    private List<TechTag> getTechTagList(Long memberId) {
        return techTagPerMemberRepository.findWithTechTagByMemberId(memberId) // N+1 문제 방지를 위해 fetch join 사용
                .stream()
                .map(TechTagPerMember::getTechTag)
                .toList();
    }
}
