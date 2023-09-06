package swm.hkcc.LGTM.app.modules.registration.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.modules.member.domain.Authority;
import swm.hkcc.LGTM.app.modules.member.domain.Junior;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.Senior;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.exception.NotJuniorMember;
import swm.hkcc.LGTM.app.modules.member.exception.NotSeniorMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.exception.NotExistMission;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MissionHistoryInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.RegistrationSeniorDetailFeedbackResponse;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.RegistrationSeniorDetailPayResponse;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.RegistrationSeniorDetailPullRequestResponse;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.RegistrationSeniorDetailResponse;
import swm.hkcc.LGTM.app.modules.registration.exception.NotMyMission;
import swm.hkcc.LGTM.app.modules.registration.exception.NotRegisteredMission;
import swm.hkcc.LGTM.app.modules.registration.service.RegistrationService;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static swm.hkcc.LGTM.utils.CustomMDGenerator.tableHead;
import static swm.hkcc.LGTM.utils.CustomMDGenerator.tableRow;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class SeniorDashboardDetailControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private MemberRepository memberRepository;

    private Member mockJunior;
    private Member mockSenior;
    private Mission mockMission;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }


    @Test
    @DisplayName("시니어 미션 참가자 상세 조회 동작 테스트")
    void 시니어_미션_참가자_상세_조회_동작_테스트() throws Exception {
        // given
        Mission mission = getMockMission();
        Member junior = getMockJunior();

        Member member = getMockSenior();
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(member));

        RegistrationSeniorDetailResponse response = new RegistrationSeniorDetailResponse();
        response.setMemberId(junior.getMemberId());
        response.setNickname(junior.getNickName());
        response.setGithubId(junior.getGithubId());
        response.setStatus(ProcessStatus.WAITING_FOR_PAYMENT);
        response.setMissionHistory(List.of(
                new MissionHistoryInfo(ProcessStatus.WAITING_FOR_PAYMENT, LocalDateTime.now())
        ));
        response.setButtonTitle(ProcessStatus.WAITING_FOR_PAYMENT.getSeniorBottomTitle());
        given(registrationService.getSeniorEnrollDetail(any(), any(), any())).willReturn(response);
        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/senior/{juniorId}", mission.getMissionId(), junior.getMemberId())
                        .header(
                                "Authorization",
                                // todo : mock member로부터 토큰 생성해서 넣기
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tc2VuaW9yIiwiaWF0IjoxNjkwNTAyNzI0LCJleHAiOjE3ODUxMTA3MjR9.gKBXkTs-71pdu6wGE3_aP5oSXaAeO8tkN-tYi_mB0es"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.data.memberId").value(junior.getMemberId()))
                .andExpect(jsonPath("$.data.nickname").value(junior.getNickName()))
                .andExpect(jsonPath("$.data.githubId").value(junior.getGithubId()))
                .andExpect(jsonPath("$.data.status").value(ProcessStatus.WAITING_FOR_PAYMENT.name()))
                .andExpect(jsonPath("$.data.missionHistory[0].status").value(ProcessStatus.WAITING_FOR_PAYMENT.name()))
                .andExpect(jsonPath("$.data.buttonTitle").value(ProcessStatus.WAITING_FOR_PAYMENT.getSeniorBottomTitle()));


        // document
        actions.andDo(document(
                "get-senior-detail",// 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
                resource(ResourceSnippetParameters.builder()
                        .summary("[미션 진행] 시니어 미션 참가자별 상세 조회(바텀시트)")
                        .tag("미션 진행")
                        .description(CustomMDGenerator.builder()
                                .h1("[Descriptions]")
                                .h3("시니어가 가 자신의 미션 참가자의 상세 정보를 조회한다.")
                                .h1("[Request Headers]")
                                .table(
                                        tableHead("Request values", "Data Type", "Description"),
                                        //Authorization
                                        tableRow("Authorization", "String", "액세스 토큰")
                                )
                                .h1("[Path Variables]")
                                .table(
                                        tableHead("Request values", "Data Type", "Description"),
                                        tableRow("missionId", "Long", "미션 아이디"),
                                        tableRow("juniorId", "Long", "참가자 아이디")
                                )
                                .line()
                                .h1("[Errors]")
                                .table(
                                        tableHead("HTTP Status", "Response Code", "Message"),

                                        tableRow(
                                                ResponseCode.NOT_SENIOR_MEMBER.getHttpStatus().toString(),
                                                ResponseCode.NOT_SENIOR_MEMBER.getCode().toString(),
                                                ResponseCode.NOT_SENIOR_MEMBER.getMessage()
                                        ),
                                        tableRow(
                                                ResponseCode.NOT_EXIST_MISSION.getHttpStatus().toString(),
                                                ResponseCode.NOT_EXIST_MISSION.getCode().toString(),
                                                ResponseCode.NOT_EXIST_MISSION.getMessage()
                                        ),
                                        tableRow(
                                                ResponseCode.NOT_MY_MISSION.getHttpStatus().toString(),
                                                ResponseCode.NOT_MY_MISSION.getCode().toString(),
                                                ResponseCode.NOT_MY_MISSION.getMessage()
                                        ),
                                        tableRow(
                                                ResponseCode.NOT_EXIST_MEMBER.getHttpStatus().toString(),
                                                ResponseCode.NOT_EXIST_MEMBER.getCode().toString(),
                                                ResponseCode.NOT_EXIST_MEMBER.getMessage()
                                        ),

                                        tableRow(
                                                ResponseCode.NOT_JUNIOR_MEMBER.getHttpStatus().toString(),
                                                ResponseCode.NOT_JUNIOR_MEMBER.getCode().toString(),
                                                ResponseCode.NOT_JUNIOR_MEMBER.getMessage()
                                        ),
                                        tableRow(
                                                ResponseCode.NOT_REGISTERED_MISSION.getHttpStatus().toString(),
                                                ResponseCode.NOT_REGISTERED_MISSION.getCode().toString(),
                                                ResponseCode.NOT_REGISTERED_MISSION.getMessage()
                                        )

                                )
                                .build())
                        .requestHeaders(headerWithName("Authorization").description("access token"))
                        .pathParameters(
                                parameterWithName("missionId").type(SimpleType.NUMBER).description("미션 아이디"),
                                parameterWithName("juniorId").type(SimpleType.NUMBER).description("참가자 멤버 아이디")
                        )
                        .responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("responseCode").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.memberId").description("참가자 아이디"),
                                fieldWithPath("data.nickname").description("참가자 닉네임"),
                                fieldWithPath("data.githubId").description("참가자 깃허브 아이디"),
                                fieldWithPath("data.status").description("참가자 미션상태"),
                                fieldWithPath("data.missionHistory[].status").description("미션 상태명"),
                                fieldWithPath("data.missionHistory[].dateTime").description("미션 상태 변경 시간"),
                                fieldWithPath("data.buttonTitle").description("바텀시트 버튼 타이틀")
                        )
                        .build())
        ));
    }

    @Test
    @DisplayName("시니어 미션 참가자 상세 조회 동작 테스트_입금_확")
    void 시니어_미션_참가자_상세_조회_동작_테스트_입금_확인() throws Exception {
        // given
        Mission mission = getMockMission();
        Member junior = getMockJunior();

        Member member = getMockSenior();
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(member));

        ProcessStatus currentStatus = ProcessStatus.PAYMENT_CONFIRMATION;

        RegistrationSeniorDetailPayResponse response = new RegistrationSeniorDetailPayResponse();
        response.setMemberId(junior.getMemberId());
        response.setNickname(junior.getNickName());
        response.setGithubId(junior.getGithubId());
        response.setStatus(currentStatus);
        response.setMissionHistory(List.of(
                new MissionHistoryInfo(ProcessStatus.WAITING_FOR_PAYMENT, LocalDateTime.now()),
                new MissionHistoryInfo(ProcessStatus.PAYMENT_CONFIRMATION, LocalDateTime.now().plusDays(1))
        ));
        response.setRealName("홍길동");
        response.setButtonTitle(currentStatus.getSeniorBottomTitle());
        given(registrationService.getSeniorEnrollDetail(any(), any(), any())).willReturn(response);
        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/senior/{juniorId}", mission.getMissionId(), junior.getMemberId())
                        .header(
                                "Authorization",
                                // todo : mock member로부터 토큰 생성해서 넣기
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tc2VuaW9yIiwiaWF0IjoxNjkwNTAyNzI0LCJleHAiOjE3ODUxMTA3MjR9.gKBXkTs-71pdu6wGE3_aP5oSXaAeO8tkN-tYi_mB0es"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.data.memberId").value(junior.getMemberId()))
                .andExpect(jsonPath("$.data.nickname").value(junior.getNickName()))
                .andExpect(jsonPath("$.data.githubId").value(junior.getGithubId()))
                .andExpect(jsonPath("$.data.status").value(currentStatus.name()))
                .andExpect(jsonPath("$.data.missionHistory[0].status").value(ProcessStatus.WAITING_FOR_PAYMENT.name()))
                .andExpect(jsonPath("$.data.realName").value("홍길동"))
                .andExpect(jsonPath("$.data.buttonTitle").value(currentStatus.getSeniorBottomTitle()));


        // document
        actions.andDo(document(
                "get-senior-detail-pay",// 문서의 고유 id
                resource(ResourceSnippetParameters.builder()
                        .responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("responseCode").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.memberId").description("참가자 아이디"),
                                fieldWithPath("data.nickname").description("참가자 닉네임"),
                                fieldWithPath("data.githubId").description("참가자 깃허브 아이디"),
                                fieldWithPath("data.status").description("참가자 미션상태"),
                                fieldWithPath("data.missionHistory[].status").description("미션 상태명"),
                                fieldWithPath("data.missionHistory[].dateTime").description("미션 상태 변경 시간"),
                                fieldWithPath("data.realName").description("입금자 실명"),
                                fieldWithPath("data.buttonTitle").description("바텀시트 버튼 타이틀"))
                        .build()))
        );
    }


    @Test
    @DisplayName("시니어 미션 참가자 상세 조회 동작 테스트_리뷰_진행")
    void 시니어_미션_참가자_상세_조회_동작_테스트_리뷰_진행() throws Exception {
        // given
        Mission mission = getMockMission();
        Member junior = getMockJunior();

        Member member = getMockSenior();
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(member));

        ProcessStatus currentStatus = ProcessStatus.CODE_REVIEW;

        RegistrationSeniorDetailPullRequestResponse response = new RegistrationSeniorDetailPullRequestResponse();
        response.setMemberId(junior.getMemberId());
        response.setNickname(junior.getNickName());
        response.setGithubId(junior.getGithubId());
        response.setStatus(currentStatus);
        response.setMissionHistory(List.of(
                new MissionHistoryInfo(ProcessStatus.WAITING_FOR_PAYMENT, LocalDateTime.now()),
                new MissionHistoryInfo(ProcessStatus.PAYMENT_CONFIRMATION, LocalDateTime.now().plusDays(1)),
                new MissionHistoryInfo(ProcessStatus.MISSION_PROCEEDING, LocalDateTime.now().plusDays(2)),
                new MissionHistoryInfo(ProcessStatus.CODE_REVIEW, LocalDateTime.now().plusDays(3))
        ));
        response.setGithubPullRequestUrl("https://github.com/abc/abc/abc");
        response.setButtonTitle(currentStatus.getSeniorBottomTitle());
        given(registrationService.getSeniorEnrollDetail(any(), any(), any())).willReturn(response);
        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/senior/{juniorId}", mission.getMissionId(), junior.getMemberId())
                        .header(
                                "Authorization",
                                // todo : mock member로부터 토큰 생성해서 넣기
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tc2VuaW9yIiwiaWF0IjoxNjkwNTAyNzI0LCJleHAiOjE3ODUxMTA3MjR9.gKBXkTs-71pdu6wGE3_aP5oSXaAeO8tkN-tYi_mB0es"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.data.memberId").value(junior.getMemberId()))
                .andExpect(jsonPath("$.data.nickname").value(junior.getNickName()))
                .andExpect(jsonPath("$.data.githubId").value(junior.getGithubId()))
                .andExpect(jsonPath("$.data.status").value(currentStatus.name()))
                .andExpect(jsonPath("$.data.missionHistory[0].status").value(ProcessStatus.WAITING_FOR_PAYMENT.name()))
                .andExpect(jsonPath("$.data.buttonTitle").value(currentStatus.getSeniorBottomTitle()));


        // document
        actions.andDo(document(
                "get-senior-detail-pr",// 문서의 고유 id
                resource(ResourceSnippetParameters.builder()
                        .responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("responseCode").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.memberId").description("참가자 아이디"),
                                fieldWithPath("data.nickname").description("참가자 닉네임"),
                                fieldWithPath("data.githubId").description("참가자 깃허브 아이디"),
                                fieldWithPath("data.status").description("참가자 미션상태"),
                                fieldWithPath("data.missionHistory[].status").description("미션 상태명"),
                                fieldWithPath("data.missionHistory[].dateTime").description("미션 상태 변경 시간"),
                                fieldWithPath("data.githubPullRequestUrl").description("깃허브 풀 리퀘스트 URL"),
                                fieldWithPath("data.buttonTitle").description("바텀시트 버튼 타이틀"))
                        .build()))
        );
    }

    @Test
    @DisplayName("시니어 미션 참가자 상세 조회 동작 테스트_후기_보러가기")
    void 시니어_미션_참가자_상세_조회_동작_테스트_후기_보러가기() throws Exception {
        // given
        Mission mission = getMockMission();
        Member junior = getMockJunior();

        Member member = getMockSenior();
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(member));

        ProcessStatus currentStatus = ProcessStatus.FEEDBACK_REVIEWED;

        RegistrationSeniorDetailFeedbackResponse response = new RegistrationSeniorDetailFeedbackResponse();
        response.setMemberId(junior.getMemberId());
        response.setNickname(junior.getNickName());
        response.setGithubId(junior.getGithubId());
        response.setStatus(currentStatus);
        response.setMissionHistory(List.of(
                new MissionHistoryInfo(ProcessStatus.WAITING_FOR_PAYMENT, LocalDateTime.now()),
                new MissionHistoryInfo(ProcessStatus.PAYMENT_CONFIRMATION, LocalDateTime.now().plusDays(1)),
                new MissionHistoryInfo(ProcessStatus.MISSION_PROCEEDING, LocalDateTime.now().plusDays(2)),
                new MissionHistoryInfo(ProcessStatus.CODE_REVIEW, LocalDateTime.now().plusDays(3)),
                new MissionHistoryInfo(ProcessStatus.MISSION_FINISHED, LocalDateTime.now().plusDays(4)),
                new MissionHistoryInfo(ProcessStatus.FEEDBACK_REVIEWED, LocalDateTime.now().plusDays(5))
        ));
        response.setReviewId(1L);
        response.setButtonTitle(currentStatus.getSeniorBottomTitle());
        given(registrationService.getSeniorEnrollDetail(any(), any(), any())).willReturn(response);
        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/senior/{juniorId}", mission.getMissionId(), junior.getMemberId())
                        .header(
                                "Authorization",
                                // todo : mock member로부터 토큰 생성해서 넣기
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tc2VuaW9yIiwiaWF0IjoxNjkwNTAyNzI0LCJleHAiOjE3ODUxMTA3MjR9.gKBXkTs-71pdu6wGE3_aP5oSXaAeO8tkN-tYi_mB0es"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.data.memberId").value(junior.getMemberId()))
                .andExpect(jsonPath("$.data.nickname").value(junior.getNickName()))
                .andExpect(jsonPath("$.data.githubId").value(junior.getGithubId()))
                .andExpect(jsonPath("$.data.status").value(currentStatus.name()))
                .andExpect(jsonPath("$.data.missionHistory[0].status").value(ProcessStatus.WAITING_FOR_PAYMENT.name()))
                .andExpect(jsonPath("$.data.reviewId").value(1L))
                .andExpect(jsonPath("$.data.buttonTitle").value(currentStatus.getSeniorBottomTitle()));


        // document
        actions.andDo(document(
                "get-senior-detail-feedback",// 문서의 고유 id
                resource(ResourceSnippetParameters.builder()
                        .responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("responseCode").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.memberId").description("참가자 아이디"),
                                fieldWithPath("data.nickname").description("참가자 닉네임"),
                                fieldWithPath("data.githubId").description("참가자 깃허브 아이디"),
                                fieldWithPath("data.status").description("참가자 미션상태"),
                                fieldWithPath("data.missionHistory[].status").description("미션 상태명"),
                                fieldWithPath("data.missionHistory[].dateTime").description("미션 상태 변경 시간"),
                                fieldWithPath("data.reviewId").description("작성한 후기 아이디"),
                                fieldWithPath("data.buttonTitle").description("바텀시트 버튼 타이틀"))
                        .build()))
        );
    }


    // 시니어가 아닌 경우
    @Test
    @DisplayName("시니어 미션 참가자 상세 조회 예외 테스트_시니어가 아닌 경우")
    void 시니어_미션_참가자_상세_조회_예외_테스트_NOT_SENIOR_MEMBER() throws Exception {
        // given
        Mission mission = getMockMission();
        Member junior = getMockJunior();

        Member member = getMockSenior();
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(member));

        ResponseCode expectedResponseCode = ResponseCode.NOT_SENIOR_MEMBER;

        given(registrationService.getSeniorEnrollDetail(any(), any(), any()))
                .willThrow(new NotSeniorMember());

        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/senior/{juniorId}", mission.getMissionId(), junior.getMemberId())
                        .header(
                                "Authorization",
                                // todo : mock member로부터 토큰 생성해서 넣기
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tc2VuaW9yIiwiaWF0IjoxNjkwNTAyNzI0LCJleHAiOjE3ODUxMTA3MjR9.gKBXkTs-71pdu6wGE3_aP5oSXaAeO8tkN-tYi_mB0es"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(document(
                        "get-senior-detail-fail-not-senior"// 문서의 고유 id
                )
        );
    }

    // 존재하지 않는 미션
    @Test
    @DisplayName("시니어 미션 참가자 상세 조회 예외 테스트_존재하지 않는 미션")
    void 시니어_미션_참가자_상세_조회_예외_테스트_NOT_EXIST_MISSION() throws Exception {
        // given
        Mission mission = getMockMission();
        Member junior = getMockJunior();

        Member member = getMockSenior();
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(member));

        ResponseCode expectedResponseCode = ResponseCode.NOT_EXIST_MISSION;

        given(registrationService.getSeniorEnrollDetail(any(), any(), any()))
                .willThrow(new NotExistMission());

        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/senior/{juniorId}", mission.getMissionId(), junior.getMemberId())
                        .header(
                                "Authorization",
                                // todo : mock member로부터 토큰 생성해서 넣기
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tc2VuaW9yIiwiaWF0IjoxNjkwNTAyNzI0LCJleHAiOjE3ODUxMTA3MjR9.gKBXkTs-71pdu6wGE3_aP5oSXaAeO8tkN-tYi_mB0es"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(document(
                        "get-senior-detail-fail-not-exist-mission"// 문서의 고유 id
                )
        );
    }

    // 자신의 미션이 아닌 경우
    @Test
    @DisplayName("시니어 미션 참가자 상세 조회 예외 테스트_NOT_MY_MISSION")
    void 시니어_미션_참가자_상세_조회_예외_테스트_NOT_MY_MISSION() throws Exception {
        // given
        Mission mission = getMockMission();
        Member junior = getMockJunior();

        Member member = getMockSenior();
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(member));

        ResponseCode expectedResponseCode = ResponseCode.NOT_MY_MISSION;

        given(registrationService.getSeniorEnrollDetail(any(), any(), any()))
                .willThrow(new NotMyMission());

        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/senior/{juniorId}", mission.getMissionId(), junior.getMemberId())
                        .header(
                                "Authorization",
                                // todo : mock member로부터 토큰 생성해서 넣기
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tc2VuaW9yIiwiaWF0IjoxNjkwNTAyNzI0LCJleHAiOjE3ODUxMTA3MjR9.gKBXkTs-71pdu6wGE3_aP5oSXaAeO8tkN-tYi_mB0es"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));


        // document
        actions.andDo(document(
                        "get-senior-detail-fail-not-my-mission"// 문서의 고유 id
                )
        );
    }

    // 존재하지 않는 참가자
    @Test
    @DisplayName("시니어 미션 참가자 상세 조회 예외 테스트_NOT_EXIST_MEMBER")
    void 시니어_미션_참가자_상세_조회_예외_테스트_NOT_EXIST_MEMBER() throws Exception {
        // given
        Mission mission = getMockMission();
        Member junior = getMockJunior();

        Member member = getMockSenior();
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(member));

        ResponseCode expectedResponseCode = ResponseCode.NOT_EXIST_MEMBER;

        given(registrationService.getSeniorEnrollDetail(any(), any(), any()))
                .willThrow(new NotExistMember());

        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/senior/{juniorId}", mission.getMissionId(), junior.getMemberId())
                        .header(
                                "Authorization",
                                // todo : mock member로부터 토큰 생성해서 넣기
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tc2VuaW9yIiwiaWF0IjoxNjkwNTAyNzI0LCJleHAiOjE3ODUxMTA3MjR9.gKBXkTs-71pdu6wGE3_aP5oSXaAeO8tkN-tYi_mB0es"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));


        // document
        actions.andDo(document(
                        "get-senior-detail-fail-not-exist-member"// 문서의 고유 id
                )
        );
    }

    // 참가자가 주니어가 아닌 경우
    @Test
    @DisplayName("시니어 미션 참가자 상세 조회 예외 테스트_NOT_JUNIOR_MEMBER")
    void 시니어_미션_참가자_상세_조회_예외_테스트_NOT_JUNIOR_MEMBER() throws Exception {
        // given
        Mission mission = getMockMission();
        Member junior = getMockJunior();

        Member member = getMockSenior();
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(member));

        ResponseCode expectedResponseCode = ResponseCode.NOT_JUNIOR_MEMBER;

        given(registrationService.getSeniorEnrollDetail(any(), any(), any()))
                .willThrow(new NotJuniorMember());

        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/senior/{juniorId}", mission.getMissionId(), junior.getMemberId())
                        .header(
                                "Authorization",
                                // todo : mock member로부터 토큰 생성해서 넣기
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tc2VuaW9yIiwiaWF0IjoxNjkwNTAyNzI0LCJleHAiOjE3ODUxMTA3MjR9.gKBXkTs-71pdu6wGE3_aP5oSXaAeO8tkN-tYi_mB0es"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));


        // document
        actions.andDo(document(
                        "get-senior-detail-fail-not-junior"// 문서의 고유 id
                )
        );
    }

    // 해당 미션에 참가한 주니어가 아닌 경우
    @Test
    @DisplayName("시니어 미션 참가자 상세 조회 예외 테스트_NOT_REGISTERED_MISSION")
    void 시니어_미션_참가자_상세_조회_예외_테스트_NOT_REGISTERED_MISSION() throws Exception {
        // given
        Mission mission = getMockMission();
        Member junior = getMockJunior();

        Member member = getMockSenior();
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(member));

        ResponseCode expectedResponseCode = ResponseCode.NOT_REGISTERED_MISSION;

        given(registrationService.getSeniorEnrollDetail(any(), any(), any()))
                .willThrow(new NotRegisteredMission());

        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/senior/{juniorId}", mission.getMissionId(), junior.getMemberId())
                        .header(
                                "Authorization",
                                // todo : mock member로부터 토큰 생성해서 넣기
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tc2VuaW9yIiwiaWF0IjoxNjkwNTAyNzI0LCJleHAiOjE3ODUxMTA3MjR9.gKBXkTs-71pdu6wGE3_aP5oSXaAeO8tkN-tYi_mB0es"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));


        // document
        actions.andDo(document(
                        "get-senior-detail-fail-not-registered-mission"// 문서의 고유 id
                )
        );
    }

    private Member getMockJunior() {
        if (mockJunior == null) {
            mockJunior = Member.builder()
                    .memberId(1L)
                    .nickName("junior")
                    .githubId("junior")
                    .profileImageUrl("junior")
                    .junior(Junior.builder().juniorId(1L).build())
                    .build();
            mockJunior.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        }
        return mockJunior;
    }

    private Member getMockSenior() {
        if (mockSenior == null) {
            mockSenior = Member.builder()
                    .memberId(1L)
                    .nickName("senior")
                    .githubId("senior")
                    .profileImageUrl("senior")
                    .senior(Senior.builder().seniorId(1L).build())
                    .build();
            mockSenior.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        }
        return mockSenior;
    }

    private Mission getMockMission() {
        if (mockMission == null) {
            mockMission = Mission.builder()
                    .missionId(1L)
                    .title("title")
                    .description("description")
                    .build();
        }
        return mockMission;
    }
}
