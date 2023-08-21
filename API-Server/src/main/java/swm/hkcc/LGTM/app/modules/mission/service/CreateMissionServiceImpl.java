package swm.hkcc.LGTM.app.modules.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.service.MemberValidator;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequest;
import swm.hkcc.LGTM.app.modules.mission.exception.InvalidGithubUrl;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRepository;
import swm.hkcc.LGTM.app.modules.tag.service.TechTagService;
import swm.hkcc.LGTM.app.modules.tag.service.TechTagValidator;

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
        request.trimTitle();

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

    public void validateGithubUrl(String url) {
        if (!isValidGithubUrl(url) || !existsGithubRepo(url)) {
            throw new InvalidGithubUrl();
        }
    }

    public boolean isValidGithubUrl(String url) {
        String regex = "^https://github\\.com/[^/]+/[^/]+/?$";
        return url.matches(regex);
    }

    public boolean existsGithubRepo(String url) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.headForHeaders(url);  // HEAD 요청으로 해당 URL의 헤더만 받아옵니다.
            return true;
        } catch (HttpClientErrorException e) {
            // 404 등의 에러 코드가 반환될 경우 해당 URL은 존재하지 않습니다.
            return false;
        }
    }

}
