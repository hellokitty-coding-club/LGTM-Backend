package swm.hkcc.LGTM.app.modules.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.member.service.MemberValidator;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequest;
import swm.hkcc.LGTM.app.modules.mission.exception.InvalidGithubUrl;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRepository;
import swm.hkcc.LGTM.app.modules.tag.service.TechTagService;
import swm.hkcc.LGTM.app.modules.tag.service.TechTagValidator;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CreateMissionServiceImpl implements CreateMissionService {
    private final TechTagService techTagService;
    private final MissionRepository missionRepository;
    private final MemberValidator memberValidator;
    private final TechTagValidator techTagValidator;

    @Override
    public Mission createMission(Member writer, CreateMissionRequest request) {
        validateRequest(writer, request);
        request.trim();

        Mission mission = Mission.from(request, writer);
        missionRepository.save(mission);
        techTagService.setTechTagListOfMission(mission, request.getTagList());

        return mission;
    }

    private void validateRequest(Member writer, CreateMissionRequest request) {
        memberValidator.validateSenior(writer);
        techTagValidator.validateTagList(request.getTagList());
        validateGithubUrl(request.getMissionRepositoryUrl());
    }

    private void validateGithubUrl(String url) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            throw new InvalidGithubUrl();
        }
    }
}
