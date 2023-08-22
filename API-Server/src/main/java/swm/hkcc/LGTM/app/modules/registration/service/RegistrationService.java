package swm.hkcc.LGTM.app.modules.registration.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotJuniorMember;
import swm.hkcc.LGTM.app.modules.member.exception.NotSeniorMember;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.exception.NotExistMission;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRepository;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionHistory;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionRegistration;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MemberRegisterSimpleInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.RegistrationSeniorResponse;
import swm.hkcc.LGTM.app.modules.registration.exception.AlreadyRegisteredMission;
import swm.hkcc.LGTM.app.modules.registration.exception.FullRegisterMembers;
import swm.hkcc.LGTM.app.modules.registration.exception.MissRegisterDeadline;
import swm.hkcc.LGTM.app.modules.registration.exception.TooManyLockError;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionHistoryRepository;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;
import swm.hkcc.LGTM.app.modules.registration.repository.RedisLockRepository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMissionRepository;

import java.time.LocalDate;
import java.util.List;

import static swm.hkcc.LGTM.app.modules.registration.mapper.RegistrationMapper.toRegistrationSeniorResponse;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class RegistrationService {
    private final MissionRepository missionRepository;
    private final MissionRegistrationRepository missionRegistrationRepository;
    private final MissionHistoryRepository missionHistoryRepository;
    private final TechTagPerMissionRepository techTagPerMissionRepository;
    private final RedisLockRepository redisLockRepository;

    public long registerJunior(Member junior, Long missionId) throws InterruptedException {
        validateMemberPosition(junior, ExpectedPosition.JUNIOR);
        Mission mission = getValidMission(missionId, junior.getMemberId());

        acquireLock(mission.getMissionId());
        try {
            return processMissionRegistration(junior, mission);
        } finally {
            redisLockRepository.unlock(mission.getMissionId());
        }
    }



    public RegistrationSeniorResponse getSeniorEnrollInfo(Member senior, Long missionId) {
        // validations
        // 시니어가 아닌 경우
        validateMemberPosition(senior, ExpectedPosition.SENIOR);
        // 자신이 참가한 미션이 아닌 경우
        // 존재하지 않는 미션


        // 미션 정보 조회
        Mission mission = missionRepository.findById(missionId).orElseThrow(NotExistMission::new);
        List<TechTag> techTagList = techTagPerMissionRepository.findTechTagsByMissionId(mission.getMissionId());

        List<MemberRegisterSimpleInfo> memberInfoList = missionRegistrationRepository.getRegisteredMembersByMission(missionId);
        memberInfoList.forEach(memberInfo -> {
            if (memberInfo.getProcessStatus().isPayed()) {
                memberInfo.setPaymentDate(missionRegistrationRepository.getStatusDateTime(ProcessStatus.MISSION_PROCEEDING, mission, senior).orElse(null));
            }
            if (memberInfo.getProcessStatus().isPullRequestCreated()) {
                memberInfo.setMissionFinishedDate(missionRegistrationRepository.getStatusDateTime(ProcessStatus.CODE_REVIEW, mission, senior).orElse(null));
            }
        });

        return toRegistrationSeniorResponse(mission, techTagList, memberInfoList);
    }

    private void validateJunior(Member junior) {
        if (junior.getJunior() == null) {
            throw new NotJuniorMember();
        }
    }

    private void validateSenior(Member senior) {
        if (senior.getSenior() == null) {
            throw new NotSeniorMember();
        }
    }

    private void validateMemberPosition(Member member, ExpectedPosition expectedRole) {
        if (expectedRole == ExpectedPosition.JUNIOR && member.getJunior() == null) {
            throw new NotJuniorMember();
        } else if (expectedRole == ExpectedPosition.SENIOR && member.getSenior() == null) {
            throw new NotSeniorMember();
        }
    }

    private Mission getValidMission(Long missionId, Long memberId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(NotExistMission::new);
        // 이미 등록된 미션인지
        if (missionRegistrationRepository.countByMission_MissionIdAndJunior_MemberId(mission.getMissionId(), memberId) > 0) {
            throw new AlreadyRegisteredMission();
        }
        // 미션 마감일이 지난 경우
        if (mission.getRegistrationDueDate().isBefore(LocalDate.now())) {
            throw new MissRegisterDeadline();
        }
        // 미션 등록인원이 넘치는 경우
        int countRegisters = missionRegistrationRepository.countByMission_MissionId(mission.getMissionId());
        if (mission.getMaxPeopleNumber() <= countRegisters) {
            throw new FullRegisterMembers();
        }
        return mission;
    }

    private void acquireLock(Long missionId) throws InterruptedException {
        int retries = 10;  // 최대 10번 재시도
        while (retries-- > 0 && !redisLockRepository.lock(missionId)) {
            Thread.sleep(100);
        }
        if (retries <= 0) {
            throw new TooManyLockError();
        }
    }

    private Long processMissionRegistration(Member junior, Mission mission) {
        MissionRegistration missionRegistration = MissionRegistration.builder()
                .mission(mission)
                .junior(junior)
                .status(ProcessStatus.WAITING_FOR_PAYMENT)
                .build();
        missionRegistrationRepository.save(missionRegistration);

        MissionHistory missionHistory = MissionHistory.builder()
                .registration(missionRegistration)
                .status(ProcessStatus.WAITING_FOR_PAYMENT)
                .build();
        missionHistoryRepository.save(missionHistory);
        return missionRegistration.getRegistrationId();
    }

}

enum ExpectedPosition {
    JUNIOR, SENIOR
}