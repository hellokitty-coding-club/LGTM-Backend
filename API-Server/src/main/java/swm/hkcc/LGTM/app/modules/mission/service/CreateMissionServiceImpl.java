package swm.hkcc.LGTM.app.modules.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.auth.exception.InvalidTechTag;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.exception.NotSeniorMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequest;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionResponse;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRepository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTagPerMission;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMissionRepository;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateMissionServiceImpl implements CreateMissionService {

    private final MissionRepository missionRepository;
    private final MemberRepository memberRepository;
    private final TechTagRepository techTagRepository;
    private final TechTagPerMissionRepository techTagPerMissionRepository;

    @Override
    public CreateMissionResponse createMission(Long memberId, CreateMissionRequest request) {
        Member writer = memberRepository.findById(memberId)
                .orElseThrow(NotExistMember::new);
        validateCreateMissionRequest(request, writer);

        Mission mission = Mission.from(request, writer);
        missionRepository.save(mission);

        setTechTagListOfMission(mission, request.getTagList());
        missionRepository.save(mission);

        return CreateMissionResponse.builder()
                .missionId(mission.getMissionId())
                .writerId(memberId)
                .build();
    }

    private void validateCreateMissionRequest(CreateMissionRequest request, Member writer) {
        validateSenior(writer);
        validateTagList(request.getTagList());
        // todo : add validations
    }

    private void setTechTagListOfMission(Mission mission, List<String> tagList) {
        tagList.stream()
                .map(tagName -> techTagRepository.findByName(tagName)
                        .orElseThrow(InvalidTechTag::new))
                .forEach(tagName -> {
                    TechTagPerMission techTagPerMission = TechTagPerMission.builder()
                            .mission(mission)
                            .techTag(tagName)
                            .build();
                    techTagPerMissionRepository.save(techTagPerMission);
                });
    }

    private void validateTagList(List<String> tagList) {
        boolean hasInvalidTag = tagList.stream()
                .anyMatch(tag -> !techTagRepository.existsByName(tag));
        if (hasInvalidTag) {
            throw new InvalidTechTag();
        }
    }

    private void validateSenior(Member writer) {
        if (writer.getSenior() == null)
            throw new NotSeniorMember();
    }
}
