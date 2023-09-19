package swm.hkcc.LGTM.app.modules.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.service.MemberValidator;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.exception.ImpossibleToDeleteMission;
import swm.hkcc.LGTM.app.modules.mission.exception.NotExistMission;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRepository;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;
import swm.hkcc.LGTM.app.modules.registration.service.RegistrationValidator;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTagPerMission;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMissionRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class DeleteMissionService {
    private final MissionRepository missionRepository;
    private final MissionRegistrationRepository missionRegistrationRepository;
    private final TechTagPerMissionRepository techTagPerMissionRepository;
    private final RegistrationValidator registrationValidator;
    private final MemberValidator memberValidator;

    public void deleteMission(Member senior, Long missionId) {
        memberValidator.validateSenior(senior);
        Mission mission = getValidatedMissionForSenior(missionId, senior.getMemberId());

        List<TechTagPerMission> tagPerMissionListList = techTagPerMissionRepository.findByMission(mission);
        techTagPerMissionRepository.deleteAllInBatch(tagPerMissionListList);

        missionRepository.delete(mission);
    }

    private Mission getValidatedMissionForSenior(long missionId, long seniorId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(NotExistMission::new);
        registrationValidator.validateMissionForSenior(mission, seniorId);
        validatePossibleToDelete(mission);
        return mission;
    }

    private void validatePossibleToDelete(Mission mission) {
        if (missionRegistrationRepository.countByMission(mission) != 0)
            throw new ImpossibleToDeleteMission();
    }
}
