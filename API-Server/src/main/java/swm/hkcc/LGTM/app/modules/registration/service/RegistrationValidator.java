package swm.hkcc.LGTM.app.modules.registration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.exception.AlreadyRegisteredMission;
import swm.hkcc.LGTM.app.modules.registration.exception.FullRegisterMembers;
import swm.hkcc.LGTM.app.modules.registration.exception.MissRegisterDeadline;
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
}
