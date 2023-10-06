package swm.hkcc.LGTM.app.modules.registration.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.modules.auth.constants.TokenType;
import swm.hkcc.LGTM.app.modules.auth.utils.jwt.TokenProvider;
import swm.hkcc.LGTM.app.modules.member.domain.Authority;
import swm.hkcc.LGTM.app.modules.member.domain.Junior;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.member.exception.NotJuniorMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.exception.NotExistMission;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MissionHistoryInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.*;
import swm.hkcc.LGTM.app.modules.registration.exception.NotRegisteredMission;
import swm.hkcc.LGTM.app.modules.registration.exception.NotRegisteredMissionInternal;
import swm.hkcc.LGTM.app.modules.registration.service.RegistrationService;
import swm.hkcc.LGTM.app.modules.review.exception.NotExistReviewInternal;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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
public class JuniorDashboardControllerTest {
    private MockMvc mockMvc;


    @Autowired
    private TokenProvider tokenProvider;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private CustomUserDetails customUserDetails;

    private Member mockJunior;
    private Member mockSenior;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("주니어_미션_대시보드 동작 테스트_예금정보")
    void 주니어_미션_대시보드_예금정보() throws Exception {
        // given
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockJunior()));

        List<MissionHistoryInfo> missionHistory = List.of(
                MissionHistoryInfo.builder().status(ProcessStatus.WAITING_FOR_PAYMENT).dateTime(LocalDateTime.now().toString()).build()
        );
        ProcessStatus status = ProcessStatus.WAITING_FOR_PAYMENT;

        RegistrationJuniorAccountResponse mockResponse = RegistrationJuniorAccountResponse.builder()
                .missionName("미션 이름")
                .techTagList(getMockTechTags())
                .processStatus(status)
                .missionHistory(missionHistory)
                .buttonTitle(status.getJuniorBottomTitle())
                .accountInfo(JuniorAdditionalAccountInfo.builder()
                        .accountNumber("123-123-123")
                        .bankName("국민은행")
                        .price(10000)
                        .sendTo("김주니어")
                        .build())
                .build();
        given(registrationService.getJuniorEnrollInfo(any(), any())).willReturn(mockResponse);


        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/junior", getMockMission().getMissionId())
                        .header("Authorization", "Bearer " + getMockToken(getMockJunior()))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data.missionName").value("미션 이름"))
                .andExpect(jsonPath("$.data.techTagList[0].techTagId").value(7))
                .andExpect(jsonPath("$.data.techTagList[0].name").value("Android"));

        // document
        actions.andDo(document(
                "get-junior-dashboard-account",// 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
                resource(
                        ResourceSnippetParameters.builder()
                                .summary("[미션 진행] 주니어 미션 진행상황 조회")
                                .tag("미션 진행")
                                .description(CustomMDGenerator.builder()
                                        .h1("[Descriptions]")
                                        .h3("주니어가 자신의 미션 진행 정보를 조회한다.")
                                        .h1("[Request Headers]")
                                        .table(
                                                tableHead("Request values", "Data Type", "Description"),
                                                tableRow("Authorization", "String", "액세스 토큰")
                                        )
                                        .h1("[Path Variables]")
                                        .table(
                                                tableHead("Request values", "Data Type", "Description"),
                                                tableRow("missionId", "Long", "미션 아이디")
                                        )
                                        .line()
                                        .h1("[Response Body]")
                                        .table(
                                                tableHead("Response values", "Data Type", "Description"),
                                                tableRow("data.missionName", "String", "미션 이름"),
                                                tableRow("data.techTagList", "List<TechTag>", "미션 기술 태그 리스트"),
                                                tableRow("data.techTagList[].techTagId", "Long", "기술 태그 아이디"),
                                                tableRow("data.techTagList[].name", "String", "기술 태그 이름"),
                                                tableRow("data.techTagList[].iconImageUrl", "String", "기술 태그 아이콘 이미지 url"),
                                                tableRow("data.processStatus", "String", "미션 진행 상태"),
                                                tableRow("data.missionHistory", "List<MissionHistoryInfo>", "미션 진행 이력"),
                                                tableRow("data.missionHistory[].status", "String", "미션 진행 상태명"),
                                                tableRow("data.missionHistory[].dateTime", "String", "해당 상태로 변경된 시간 timestamp"),
                                                tableRow("data.buttonTitle", "String", "버튼 텍스트"),
                                                tableRow("---------------", "추가정보", "---------------"),
                                                tableRow("data.accountInfo", "Object", "계좌 정보 <br>WAITING_FOR_PAYMENT, PAYMENT_CONFIRMATION 상태에서만 나타난다."),
                                                tableRow("data.accountInfo.accountNumber", "String", "계좌 번호 <br>WAITING_FOR_PAYMENT, PAYMENT_CONFIRMATION 상태에서만 나타난다."),
                                                tableRow("data.accountInfo.bankName", "String", "은행 이름 <br>WAITING_FOR_PAYMENT, PAYMENT_CONFIRMATION 상태에서만 나타난다."),
                                                tableRow("data.accountInfo.price", "Long", "송금해야 할 금액 <br>WAITING_FOR_PAYMENT, PAYMENT_CONFIRMATION 상태에서만 나타난다."),
                                                tableRow("data.accountInfo.sendTo", "String", "예금주 이름 <br>WAITING_FOR_PAYMENT, PAYMENT_CONFIRMATION 상태에서만 나타난다."),
                                                tableRow("data.reviewId", "Long", "리뷰 아이디 <br>CODE_REVIEW, MISSION_FINISHED 상태에서만 나타난다."),
                                                tableRow("data.pullReqeustUrl", "String", "풀 리퀘스트 url <br>FEEDBACK_REVIEWED 상태에서만 나타난다.")
                                        )
                                        .line()
                                        .h1("[Errors]")
                                        .table(
                                                tableHead("HTTP Status", "Response Code", "Message"),
                                                tableRow(
                                                        ResponseCode.NOT_JUNIOR_MEMBER.getHttpStatus().toString(),
                                                        ResponseCode.NOT_JUNIOR_MEMBER.getCode().toString(),
                                                        ResponseCode.NOT_JUNIOR_MEMBER.getMessage()
                                                ),
                                                tableRow(
                                                        ResponseCode.NOT_EXIST_MISSION.getHttpStatus().toString(),
                                                        ResponseCode.NOT_EXIST_MISSION.getCode().toString(),
                                                        ResponseCode.NOT_EXIST_MISSION.getMessage()
                                                ),
                                                tableRow(
                                                        ResponseCode.NOT_REGISTERED_MISSION.getHttpStatus().toString(),
                                                        ResponseCode.NOT_REGISTERED_MISSION.getCode().toString(),
                                                        ResponseCode.NOT_REGISTERED_MISSION.getMessage()
                                                ),
                                                tableRow(
                                                        ResponseCode.NOT_REGISTERED_MISSION_INTERNAL.getHttpStatus().toString(),
                                                        ResponseCode.NOT_REGISTERED_MISSION_INTERNAL.getCode().toString(),
                                                        ResponseCode.NOT_REGISTERED_MISSION_INTERNAL.getMessage()
                                                ),
                                                tableRow(
                                                        ResponseCode.NOT_EXIST_REVIEW_INTERNAL.getHttpStatus().toString(),
                                                        ResponseCode.NOT_EXIST_REVIEW_INTERNAL.getCode().toString(),
                                                        ResponseCode.NOT_EXIST_REVIEW_INTERNAL.getMessage()
                                                )
                                        )
                                        .build())

                                .requestHeaders(headerWithName("Authorization").description("access token"))
                                .pathParameters(parameterWithName("missionId").type(SimpleType.NUMBER).description("미션 아이디"))
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.missionName").type(JsonFieldType.STRING).description("미션 이름"),
                                        fieldWithPath("data.techTagList").type(JsonFieldType.ARRAY).description("미션 기술 태그 리스트"),
                                        fieldWithPath("data.techTagList[].techTagId").type(JsonFieldType.NUMBER).description("기술 태그 아이디"),
                                        fieldWithPath("data.techTagList[].name").type(JsonFieldType.STRING).description("기술 태그 이름"),
                                        fieldWithPath("data.techTagList[].iconImageUrl").type(JsonFieldType.STRING).description("기술 태그 아이콘 이미지 url"),
                                        fieldWithPath("data.processStatus").type(JsonFieldType.STRING).description("미션 진행 상태"),
                                        fieldWithPath("data.missionHistory").type(JsonFieldType.ARRAY).description("미션 진행 이력"),
                                        fieldWithPath("data.missionHistory[].status").type(JsonFieldType.STRING).description("미션 진행 상태"),
                                        fieldWithPath("data.missionHistory[].dateTime").type(JsonFieldType.STRING).description("미션 진행 시간"),
                                        fieldWithPath("data.buttonTitle").type(JsonFieldType.STRING).description("버튼 타이틀"),
                                        fieldWithPath("data.accountInfo").type(JsonFieldType.OBJECT).description("계좌 정보"),
                                        fieldWithPath("data.accountInfo.accountNumber").type(JsonFieldType.STRING).description("계좌 번호"),
                                        fieldWithPath("data.accountInfo.bankName").type(JsonFieldType.STRING).description("은행 이름"),
                                        fieldWithPath("data.accountInfo.price").type(JsonFieldType.NUMBER).description("금액"),
                                        fieldWithPath("data.accountInfo.sendTo").type(JsonFieldType.STRING).description("예금주 이름")
                                )
                                .build())));
    }


    @Test
    @DisplayName("주니어_미션_대시보드 동작 테스트_pr정보")
    void 주니어_미션_대시보드_pr정보() throws Exception {
        // given
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockJunior()));

        List<MissionHistoryInfo> missionHistory = List.of(
                MissionHistoryInfo.builder().status(ProcessStatus.WAITING_FOR_PAYMENT).dateTime(LocalDateTime.now().toString()).build(),
                MissionHistoryInfo.builder().status(ProcessStatus.PAYMENT_CONFIRMATION).dateTime(LocalDateTime.now().plusDays(1).toString()).build(),
                MissionHistoryInfo.builder().status(ProcessStatus.MISSION_PROCEEDING).dateTime(LocalDateTime.now().plusDays(2).toString()).build(),
                MissionHistoryInfo.builder().status(ProcessStatus.CODE_REVIEW).dateTime(LocalDateTime.now().plusDays(3).toString()).build()
        );
        ProcessStatus status = ProcessStatus.CODE_REVIEW;

        RegistrationJuniorPullRequestResponse mockResponse = RegistrationJuniorPullRequestResponse.builder()
                .missionName("미션 이름")
                .techTagList(getMockTechTags())
                .processStatus(status)
                .missionHistory(missionHistory)
                .buttonTitle(status.getJuniorBottomTitle())
                .pullRequestUrl("http://github.com/xxx/xxx")
                .build();
        given(registrationService.getJuniorEnrollInfo(any(), any())).willReturn(mockResponse);


        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/junior", getMockMission().getMissionId())
                        .header("Authorization", "Bearer " + getMockToken(getMockJunior()))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data.missionName").value("미션 이름"))
                .andExpect(jsonPath("$.data.techTagList[0].techTagId").value(7))
                .andExpect(jsonPath("$.data.techTagList[0].name").value("Android"));

        // document
        actions.andDo(document(
                "get-junior-dashboard-prurl",// 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
                resource(
                        ResourceSnippetParameters.builder()
                                .requestHeaders(headerWithName("Authorization").description("access token"))
                                .pathParameters(parameterWithName("missionId").type(SimpleType.NUMBER).description("미션 아이디"))
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.missionName").type(JsonFieldType.STRING).description("미션 이름"),
                                        fieldWithPath("data.techTagList").type(JsonFieldType.ARRAY).description("미션 기술 태그 리스트"),
                                        fieldWithPath("data.techTagList[].techTagId").type(JsonFieldType.NUMBER).description("기술 태그 아이디"),
                                        fieldWithPath("data.techTagList[].name").type(JsonFieldType.STRING).description("기술 태그 이름"),
                                        fieldWithPath("data.techTagList[].iconImageUrl").type(JsonFieldType.STRING).description("기술 태그 아이콘 이미지 url"),
                                        fieldWithPath("data.processStatus").type(JsonFieldType.STRING).description("미션 진행 상태"),
                                        fieldWithPath("data.missionHistory").type(JsonFieldType.ARRAY).description("미션 진행 이력"),
                                        fieldWithPath("data.missionHistory[].status").type(JsonFieldType.STRING).description("미션 진행 상태"),
                                        fieldWithPath("data.missionHistory[].dateTime").type(JsonFieldType.STRING).description("미션 진행 시간"),
                                        fieldWithPath("data.buttonTitle").type(JsonFieldType.STRING).description("버튼 타이틀"),
                                        fieldWithPath("data.pullRequestUrl").type(JsonFieldType.STRING).description("풀 리퀘스트 url")
                                )
                                .build())));
    }

    @Test
    @DisplayName("주니어_미션_대시보드 동작 테스트_리뷰정보")
    void 주니어_미션_대시보드_리뷰정보() throws Exception {
        // given
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockJunior()));

        List<MissionHistoryInfo> missionHistory = List.of(
                MissionHistoryInfo.builder().status(ProcessStatus.WAITING_FOR_PAYMENT).dateTime(LocalDateTime.now().toString()).build(),
                MissionHistoryInfo.builder().status(ProcessStatus.PAYMENT_CONFIRMATION).dateTime(LocalDateTime.now().plusDays(1).toString()).build(),
                MissionHistoryInfo.builder().status(ProcessStatus.MISSION_PROCEEDING).dateTime(LocalDateTime.now().plusDays(2).toString()).build(),
                MissionHistoryInfo.builder().status(ProcessStatus.CODE_REVIEW).dateTime(LocalDateTime.now().plusDays(3).toString()).build(),
                MissionHistoryInfo.builder().status(ProcessStatus.MISSION_FINISHED).dateTime(LocalDateTime.now().plusDays(4).toString()).build(),
                MissionHistoryInfo.builder().status(ProcessStatus.FEEDBACK_REVIEWED).dateTime(LocalDateTime.now().plusDays(5).toString()).build()
        );
        ProcessStatus status = ProcessStatus.FEEDBACK_REVIEWED;

        RegistrationJuniorFeedbackResponse mockResponse = RegistrationJuniorFeedbackResponse.builder()
                .missionName("미션 이름")
                .techTagList(getMockTechTags())
                .processStatus(status)
                .missionHistory(missionHistory)
                .buttonTitle(status.getJuniorBottomTitle())
                .reviewId(1L)
                .build();
        given(registrationService.getJuniorEnrollInfo(any(), any())).willReturn(mockResponse);


        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/junior", getMockMission().getMissionId())
                        .header("Authorization", "Bearer " + getMockToken(getMockJunior()))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data.missionName").value("미션 이름"))
                .andExpect(jsonPath("$.data.techTagList[0].techTagId").value(7))
                .andExpect(jsonPath("$.data.techTagList[0].name").value("Android"));

        // document
        actions.andDo(document(
                "get-junior-dashboard-reviewId",// 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
                resource(
                        ResourceSnippetParameters.builder()
                                .requestHeaders(headerWithName("Authorization").description("access token"))
                                .pathParameters(parameterWithName("missionId").type(SimpleType.NUMBER).description("미션 아이디"))
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.missionName").type(JsonFieldType.STRING).description("미션 이름"),
                                        fieldWithPath("data.techTagList").type(JsonFieldType.ARRAY).description("미션 기술 태그 리스트"),
                                        fieldWithPath("data.techTagList[].techTagId").type(JsonFieldType.NUMBER).description("기술 태그 아이디"),
                                        fieldWithPath("data.techTagList[].name").type(JsonFieldType.STRING).description("기술 태그 이름"),
                                        fieldWithPath("data.techTagList[].iconImageUrl").type(JsonFieldType.STRING).description("기술 태그 아이콘 이미지 url"),
                                        fieldWithPath("data.processStatus").type(JsonFieldType.STRING).description("미션 진행 상태"),
                                        fieldWithPath("data.missionHistory").type(JsonFieldType.ARRAY).description("미션 진행 이력"),
                                        fieldWithPath("data.missionHistory[].status").type(JsonFieldType.STRING).description("미션 진행 상태"),
                                        fieldWithPath("data.missionHistory[].dateTime").type(JsonFieldType.STRING).description("미션 진행 시간"),
                                        fieldWithPath("data.buttonTitle").type(JsonFieldType.STRING).description("버튼 타이틀"),
                                        fieldWithPath("data.reviewId").type(JsonFieldType.NUMBER).description("리뷰 아이디")
                                )
                                .build())));
    }

    @Test
    @DisplayName("주니어_미션_대시보드_예외_테스트_주니어가_아닌_유저")
    void 주니어_미션_대시보드_예외_테스트_NOT_JUNIOR_MEMBER() throws Exception {
        // given
        ResponseCode expectedResponseCode = ResponseCode.NOT_JUNIOR_MEMBER;

        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockSenior()));
        given(registrationService.getJuniorEnrollInfo(any(), any())).willThrow(new NotJuniorMember());
        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/junior", getMockMission().getMissionId())
                        .header("Authorization", "Bearer " + getMockToken(getMockSenior()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(MockMvcRestDocumentationWrapper.document(
                        "get-junior-dashboard-fail-not-junior-member"// 문서의 고유 id
                )
        );
    }

    @Test
    @DisplayName("주니어_미션_대시보드_예외_테스트_존재하지 않는 미션")
    void 주니어_미션_대시보드_예외_테스트_NOT_EXIST_MISSION() throws Exception {
        // given
        ResponseCode expectedResponseCode = ResponseCode.NOT_EXIST_MISSION;

        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockJunior()));
        given(registrationService.getJuniorEnrollInfo(any(), any())).willThrow(new NotExistMission());

        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/junior", getMockMission().getMissionId())
                        .header("Authorization", "Bearer " + getMockToken(getMockJunior()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(MockMvcRestDocumentationWrapper.document(
                        "get-junior-dashboard-fail-not-exist-mission"// 문서의 고유 id
                )
        );
    }

    @Test
    @DisplayName("주니어_미션_대시보드_예외_테스트_미션_등록하지_않은_유저")
    void 주니어_미션_대시보드_예외_테스트_NOT_REGISTERED_MISSION() throws Exception {
        // given
        ResponseCode expectedResponseCode = ResponseCode.NOT_REGISTERED_MISSION;

        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockJunior()));
        given(registrationService.getJuniorEnrollInfo(any(), any())).willThrow(new NotRegisteredMission());

        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/junior", getMockMission().getMissionId())
                        .header("Authorization", "Bearer " + getMockToken(getMockJunior()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(MockMvcRestDocumentationWrapper.document(
                        "get-junior-dashboard-fail-not-registered-mission"// 문서의 고유 id
                )
        );
    }

    @Test
    @DisplayName("주니어_미션_대시보드_예외_테스트_(500 에러) 미션_등록하지_않은_유저")
    void 주니어_미션_대시보드_예외_테스트_NOT_REGISTERED_MISSION_INTERNAL() throws Exception {
        // given
        ResponseCode expectedResponseCode = ResponseCode.NOT_REGISTERED_MISSION_INTERNAL;

        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockJunior()));
        given(registrationService.getJuniorEnrollInfo(any(), any())).willThrow(new NotRegisteredMissionInternal());

        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/junior", getMockMission().getMissionId())
                        .header("Authorization", "Bearer " + getMockToken(getMockJunior()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(MockMvcRestDocumentationWrapper.document(
                        "get-junior-dashboard-fail-not-registered-mission-internal"// 문서의 고유 id
                )
        );
    }

    @Test
    @DisplayName("주니어_미션_대시보드_예외_테스트_(500 에러) 리뷰가 없음")
    void 주니어_미션_대시보드_예외_테스트_NOT_EXIST_REVIEW_INTERNAL() throws Exception {
        // given
        ResponseCode expectedResponseCode = ResponseCode.NOT_EXIST_REVIEW_INTERNAL;

        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockJunior()));
        given(registrationService.getJuniorEnrollInfo(any(), any())).willThrow(new NotExistReviewInternal());

        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/junior", getMockMission().getMissionId())
                        .header("Authorization", "Bearer " + getMockToken(getMockJunior()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(MockMvcRestDocumentationWrapper.document(
                        "get-junior-dashboard-fail-not-exist-review-internal"// 문서의 고유 id
                )
        );
    }

    private List<TechTag> getMockTechTags() {
        return Arrays.asList(
                new TechTag(7L, "Android", "http://imgimg.com"),
                new TechTag(12L, "Kotlin", "http://imgimg.com"),
                new TechTag(13L, "iOS", "http://imgimg.com")
        );
    }

    private Mission getMockMission() {
        Mission mission = Mission.builder()
                .missionId(1L)
                .build();
        return mission;
    }

    private Member getMockJunior() {
        if (mockJunior == null) {
            mockJunior = Member.builder()
                    .memberId(1L)
                    .githubId("junior")
                    .nickName("junior")
                    .junior(Junior.builder().build())
                    .build();
            mockJunior.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        }
        return mockJunior;
    }

    private Member getMockSenior() {
        if (mockSenior == null) {
            mockSenior = Member.builder()
                    .memberId(2L)
                    .githubId("senior")
                    .nickName("senior")
                    .build();
            mockSenior.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        }
        return mockSenior;
    }

    private String getMockToken(Member member) {
        return tokenProvider.createToken(member.getGithubId(), TokenType.ACCESS_TOKEN);
    }
}
