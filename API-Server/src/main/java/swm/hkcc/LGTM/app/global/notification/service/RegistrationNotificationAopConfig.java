package swm.hkcc.LGTM.app.global.notification.service;

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

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationNotificationAopConfig {
    private final NotificationService notificationService;
    private final MemberService memberService;

    @Async
    @AfterReturning("execution(* swm.hkcc.LGTM.app.modules.registration.service.RegistrationService.pushTestMethod(..))")
    public void afterPush(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Member junior = (Member) args[0];
        Mission mission = (Mission) args[1];

//        Long targetMemberId = mission.getWriter().getMemberId();
        Long targetMemberId = 166L;

        Map<String, String> data = Map.of(
                "title", String.format("%s", mission.getTitle()),
                "body", String.format("%s님이 미션에 참여했습니다!", junior.getNickName()),
                "missionId", String.valueOf(mission.getMissionId()),
                "missionTitle", mission.getTitle(),
                "nickName", junior.getNickName(),
                "targetMemberType", "SENIOR",
                "isFromPush", "true"
        );
        notificationService.sendNotification(targetMemberId, data);
        log.info("pushed");
    }

    @Async
    @AfterReturning("execution(* swm.hkcc.LGTM.app.modules.registration.service.RegistrationService.registerJunior(..))")
    public void registerJunior(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Member junior = (Member) args[0];
        Mission mission = (Mission) args[1];
        Long targetMemberId = mission.getWriter().getMemberId();

        Map<String, String> data = Map.of(
                "title", String.format("%s", mission.getTitle()),
                "body", String.format("%s님이 미션에 참여했습니다!", junior.getNickName()),
                "missionId", String.valueOf(mission.getMissionId()),
                "missionTitle", mission.getTitle(),
                "nickName", junior.getNickName(),
                "targetMemberType", MemberType.SENIOR.toString(),
                "isFromPush", "true"
        );
        notificationService.sendNotification(targetMemberId, data);
    }

    @Async
    @AfterReturning("execution(* swm.hkcc.LGTM.app.modules.registration.service.RegistrationService.confirmPayment(..))")
    public void confirmPayment(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Member senior = (Member) args[0];
        Mission mission = (Mission) args[1];
        Long juniorId = (Long) args[2];
        Long targetMemberId = senior.getMemberId();


        Map<String, String> data = Map.of(
                "title", String.format("%s", mission.getTitle()),
                "body", String.format("%s님이 입금을 확인했습니다. 미션을 시작해주세요!", senior.getNickName()),
                "missionId", String.valueOf(mission.getMissionId()),
                "missionTitle", mission.getTitle(),
                "nickName", senior.getNickName(),
                "targetMemberType", MemberType.JUNIOR.toString(),
                "isFromPush", "true"
        );
        notificationService.sendNotification(targetMemberId, data);
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


        Map<String, String> data = Map.of(
                "title", String.format("%s", mission.getTitle()),
                "body", String.format("%s님의 코드리뷰가 완료되었습니다. 리뷰를 보러갈까요?", senior.getNickName()),
                "missionId", String.valueOf(mission.getMissionId()),
                "missionTitle", mission.getTitle(),
                "nickName", senior.getNickName(),
                "targetMemberType", MemberType.JUNIOR.toString(),
                "isFromPush", "true"
        );
        notificationService.sendNotification(targetMemberId, data);
    }

    @Async
    @AfterReturning("execution(* swm.hkcc.LGTM.app.modules.registration.service.RegistrationService.registerPayment(..))")
    public void registerPayment(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Member junior = (Member) args[0];
        Mission mission = (Mission) args[1];
        Long targetMemberId = mission.getWriter().getMemberId();


        Map<String, String> data = Map.of(
                "title", String.format("%s", mission.getTitle()),
                "body", String.format("%s님이 입금을 완료했습니다. 확인 후 입금 확인 버튼을 눌러주세요!", junior.getNickName()),
                "missionId", String.valueOf(mission.getMissionId()),
                "missionTitle", mission.getTitle(),
                "nickName", junior.getNickName(),
                "targetMemberType", MemberType.SENIOR.toString(),
                "isFromPush", "true"
        );
        notificationService.sendNotification(targetMemberId, data);
    }

    @Async
    @AfterReturning("execution(* swm.hkcc.LGTM.app.modules.registration.service.RegistrationService.registerPullRequest(..))")
    public void registerPullRequest(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Member junior = (Member) args[0];
        Mission mission = (Mission) args[1];
        Long targetMemberId = mission.getWriter().getMemberId();


        Map<String, String> data = Map.of(
                "title", String.format("%s", mission.getTitle()),
                "body", String.format("%s님이 미션을 제출했습니다! 리뷰를 시작해주세요!", junior.getNickName()),
                "missionId", String.valueOf(mission.getMissionId()),
                "missionTitle", mission.getTitle(),
                "nickName", junior.getNickName(),
                "targetMemberType", MemberType.SENIOR.toString(),
                "isFromPush", "true"
        );
        notificationService.sendNotification(targetMemberId, data);
    }
}
