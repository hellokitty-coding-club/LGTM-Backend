package swm.hkcc.LGTM.app.modules.registration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.exception.*;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RegistrationValidator {
    private final MissionRegistrationRepository missionRegistrationRepository;

    public void validateToRegisterMission(Mission mission, Long memberId) {
        // 이미 등록된 미션인지
        validateNotRegisteredMission(mission, memberId);
        // 미션 마감일이 지난 경우
        validateMissionDueDate(mission);
        // 미션 등록인원이 넘치는 경우
        validateMissionMaxPeopleNumber(mission);
    }

    public void validateMissionForSenior(Mission mission, Long memberId) {
        // 자신의 미션이 아닌 경우
        validateNotMyMission(mission, memberId);
    }

    public void validateMissionForJunior(Mission mission, Long memberId) {
        // 등록되지 않은 미션인 경
        validateRegisteredMission(mission, memberId);
    }

    private void validateRegisteredMission(Mission mission, Long memberId) {
        if (missionRegistrationRepository.countByMission_MissionIdAndJunior_MemberId(mission.getMissionId(), memberId) == 0) {
            throw new NotRegisteredMission();
        }
    }


    private void validateNotRegisteredMission(Mission mission, Long memberId) {
        if (missionRegistrationRepository.countByMission_MissionIdAndJunior_MemberId(mission.getMissionId(), memberId) > 0) {
            throw new AlreadyRegisteredMission();
        }
    }

    private void validateMissionDueDate(Mission mission) {
        if (mission.getRegistrationDueDate().isBefore(LocalDate.now())) {
            throw new MissRegisterDeadline();
        }
    }

    private void validateMissionMaxPeopleNumber(Mission mission) {
        int countRegisters = missionRegistrationRepository.countByMission_MissionId(mission.getMissionId());
        if (mission.getMaxPeopleNumber() <= countRegisters) {
            throw new FullRegisterMembers();
        }
    }

    private void validateNotMyMission(Mission mission, Long memberId) {
        if (!mission.getWriter().getMemberId().equals(memberId)) {
            throw new NotMyMission();
        }
    }
}
