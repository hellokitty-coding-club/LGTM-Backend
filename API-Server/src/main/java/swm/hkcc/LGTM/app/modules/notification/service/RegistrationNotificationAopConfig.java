package swm.hkcc.LGTM.app.modules.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.auth.constants.MemberType;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.service.MemberService;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationNotificationAopConfig {
    private final NotificationService notificationService;
    private final MemberService memberService;

    @Async
    @AfterReturning("execution(* swm.hkcc.LGTM.app.modules.registration.service.RegistrationService.registerJunior(..))")
    public void registerJunior(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Member junior = (Member) args[0];
        Mission mission = (Mission) args[1];
        Long targetMemberId = mission.getWriter().getMemberId();
        sendNotification(targetMemberId, mission, "%s님이 미션에 참여했습니다!", junior.getNickName(), MemberType.SENIOR, ProcessStatus.WAITING_FOR_PAYMENT);
    }

    @Async
    @AfterReturning("execution(* swm.hkcc.LGTM.app.modules.registration.service.RegistrationService.confirmPayment(..))")
    public void confirmPayment(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Member senior = (Member) args[0];
        Mission mission = (Mission) args[1];
        Long juniorId = (Long) args[2];
        Long targetMemberId = senior.getMemberId();
        sendNotification(targetMemberId, mission, "%s님이 입금을 확인했습니다. 미션을 시작해주세요!", senior.getNickName(), MemberType.JUNIOR, ProcessStatus.MISSION_PROCEEDING);
    }

    @Async
    @AfterReturning("execution(* swm.hkcc.LGTM.app.modules.registration.service.RegistrationService.completeReview(..))")
    public void completeReview(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Member senior = (Member) args[0];
        Mission mission = (Mission) args[1];
        Long juniorId = (Long) args[2];
        Member junior = memberService.getMember(juniorId);
        Long targetMemberId = senior.getMemberId();
        sendNotification(targetMemberId, mission, "%s님이 코드리뷰를 완료했습니다. 리뷰를 보러갈까요?", junior.getNickName(), MemberType.JUNIOR, ProcessStatus.MISSION_FINISHED);
    }

    @Async
    @AfterReturning("execution(* swm.hkcc.LGTM.app.modules.registration.service.RegistrationService.registerPayment(..))")
    public void registerPayment(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Member junior = (Member) args[0];
        Mission mission = (Mission) args[1];
        Long targetMemberId = mission.getWriter().getMemberId();
        sendNotification(targetMemberId, mission, "%s님이 입금을 완료했습니다. 확인 후 입금 확인 버튼을 눌러주세요!", junior.getNickName(), MemberType.SENIOR, ProcessStatus.PAYMENT_CONFIRMATION);
    }

    @Async
    @AfterReturning("execution(* swm.hkcc.LGTM.app.modules.registration.service.RegistrationService.registerPullRequest(..))")
    public void registerPullRequest(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Member junior = (Member) args[0];
        Mission mission = (Mission) args[1];
        Long targetMemberId = mission.getWriter().getMemberId();
        sendNotification(targetMemberId, mission, "%s님이 미션을 제출했습니다! 리뷰를 시작해주세요!", junior.getNickName(), MemberType.SENIOR, ProcessStatus.CODE_REVIEW);
    }

    private void sendNotification(Long targetMemberId, Mission mission, String bodyFormat, String name, MemberType memberType, ProcessStatus processStatus) {
        Map<String, String> data = Map.of(
                "title", String.format("%s", mission.getTitle()),
                "body", String.format(bodyFormat, name),
                "missionId", String.valueOf(mission.getMissionId()),
                "missionTitle", mission.getTitle(),
                "pushCategory", processStatus.toString(),
                "nickName", name,
                "targetMemberType", memberType.toString(),
                "isFromPush", "true"
        );
        notificationService.sendNotification(targetMemberId, data);
    }
}

