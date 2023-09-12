package swm.hkcc.LGTM.app.modules.registration.repository;

import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MemberRegisterSimpleInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.MissionHistoryInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MissionRegistrationCustomRepository {
    public List<MemberRegisterSimpleInfo> getRegisteredMembersByMission(Long missionId);

    public Optional<LocalDateTime> getStatusDateTime(ProcessStatus status, Mission mission, Member junior);

    public List<MissionHistoryInfo> getMissionHistoryByMissionAndJunior(Mission mission, Member junior);

    public Optional<Member> getSeniorByMissionAndJunior(Mission mission);
}
