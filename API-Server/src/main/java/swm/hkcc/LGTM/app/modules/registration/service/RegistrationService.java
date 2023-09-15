package swm.hkcc.LGTM.app.modules.registration.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.service.MemberValidator;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.exception.NotExistMission;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRepository;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionHistory;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionRegistration;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MemberRegisterSimpleInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.MissionHistoryInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.RegistrationSeniorResponse;
import swm.hkcc.LGTM.app.modules.registration.exception.NotRegisteredMission;
import swm.hkcc.LGTM.app.modules.registration.exception.TooManyLockError;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionHistoryRepository;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;
import swm.hkcc.LGTM.app.modules.registration.repository.RedisLockRepository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMissionRepository;

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
    private final RegistrationValidator registrationValidator;
    private final MemberValidator memberValidator;
    private static final int MAX_LOCK_RETRIES = 10;
    private static final int LOCK_RETRY_DELAY_MS = 100;


    public long registerJunior(Member junior, Long missionId) throws InterruptedException {
        memberValidator.validateJunior(junior);
        Mission mission = missionRepository.findById(missionId).orElseThrow(NotExistMission::new);

        // todo : 동시성 테스트하기
        // todo: rdb lock 고려해보기
        acquireLock(mission.getMissionId());
        try {
            registrationValidator.validateToRegisterMission(mission, junior.getMemberId());
            return processMissionRegistration(junior, mission);
        } finally {
            redisLockRepository.unlock(mission.getMissionId());
        }
    }

    public RegistrationSeniorResponse getSeniorEnrollInfo(Member senior, Long missionId) {
        memberValidator.validateSenior(senior);
        Mission mission = missionRepository.findById(missionId).orElseThrow(NotExistMission::new);
        registrationValidator.validateMissionForSenior(mission, senior.getMemberId());

        List<TechTag> techTagList = techTagPerMissionRepository.findTechTagsByMissionId(mission.getMissionId());

        // todo : isPayed & isPullRequestCreated registration에 추가해서 한번에 조회
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

    public MissionHistoryInfo confirmPayment(Member senior, Long missionId, Long juniorId) {
        memberValidator.validateSenior(senior);
        Mission mission = getValidatedMissionForSenior(missionId, senior.getMemberId());
        memberValidator.validateJunior(juniorId);

        MissionRegistration registration = missionRegistrationRepository.findByMission_MissionIdAndJunior_MemberId(mission.getMissionId(), juniorId)
                .orElseThrow(NotRegisteredMission::new);
        registration.confirmPayment();
        MissionHistory history = MissionHistory.builder()
                .registration(registration)
                .status(registration.getStatus())
                .build();

        missionRegistrationRepository.save(registration);
        missionHistoryRepository.save(history);

        return MissionHistoryInfo.from(history);
    }

    public MissionHistoryInfo completeReview(Member senior, Long missionId, Long juniorId) {
        memberValidator.validateSenior(senior);
        Mission mission = getValidatedMissionForSenior(missionId, senior.getMemberId());
        memberValidator.validateJunior(juniorId);

        MissionRegistration registration = missionRegistrationRepository.findByMission_MissionIdAndJunior_MemberId(mission.getMissionId(), juniorId)
                .orElseThrow(NotRegisteredMission::new);
        registration.completeReview();
        MissionHistory history = MissionHistory.builder()
                .registration(registration)
                .status(registration.getStatus())
                .build();

        missionRegistrationRepository.save(registration);
        missionHistoryRepository.save(history);

        return MissionHistoryInfo.from(history);
    }

    public MissionHistoryInfo registerPayment(Member junior, Long missionId) {
        memberValidator.validateJunior(junior);
        Mission mission = missionRepository.findById(missionId).orElseThrow(NotExistMission::new);

        MissionRegistration registration = missionRegistrationRepository.findByMission_MissionIdAndJunior_MemberId(mission.getMissionId(), junior.getMemberId())
                .orElseThrow(NotRegisteredMission::new);
        registration.registerPayment();
        MissionHistory history = MissionHistory.builder()
                .registration(registration)
                .status(registration.getStatus())
                .build();

        missionRegistrationRepository.save(registration);
        missionHistoryRepository.save(history);

        return MissionHistoryInfo.from(history);
    }

    public MissionHistoryInfo registerPullRequest(Member junior, Long missionId, String githubPullRequestUrl) {
        memberValidator.validateJunior(junior);
        Mission mission = missionRepository.findById(missionId).orElseThrow(NotExistMission::new);

        MissionRegistration registration = missionRegistrationRepository.findByMission_MissionIdAndJunior_MemberId(mission.getMissionId(), junior.getMemberId())
                .orElseThrow(NotRegisteredMission::new);
        registration.registerPullRequest(githubPullRequestUrl);
        MissionHistory history = MissionHistory.builder()
                .registration(registration)
                .status(registration.getStatus())
                .build();

        missionRegistrationRepository.save(registration);
        missionHistoryRepository.save(history);

        return MissionHistoryInfo.from(history);
    }

    private Mission getValidatedMissionForSenior(long missionId, long seniorId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(NotExistMission::new);
        registrationValidator.validateMissionForSenior(mission, seniorId);
        return mission;
    }

    private void acquireLock(Long missionId) throws InterruptedException {
        int retries = MAX_LOCK_RETRIES;  // 최대 10번 재시도
        while (retries-- > 0 && !redisLockRepository.lock(missionId)) {
            Thread.sleep(LOCK_RETRY_DELAY_MS);
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

    private ProcessStatus getLatestProcessStatus(List<MissionHistoryInfo> missionHistoryInfos) {
        return missionHistoryInfos.get(missionHistoryInfos.size() - 1).getStatus();
    }

}
