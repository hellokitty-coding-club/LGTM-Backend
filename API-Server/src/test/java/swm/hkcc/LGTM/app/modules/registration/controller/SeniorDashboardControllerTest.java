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
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotSeniorMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.exception.NotExistMission;
import swm.hkcc.LGTM.app.modules.mission.service.MissionService;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MemberRegisterSimpleInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.RegistrationSeniorResponse;
import swm.hkcc.LGTM.app.modules.registration.exception.NotMyMission;
import swm.hkcc.LGTM.app.modules.registration.service.RegistrationService;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.util.Arrays;
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
class SeniorDashboardControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private TokenProvider tokenProvider;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private MissionService missionService;


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
    @DisplayName("시니어 미션 참가자 조회 동작 테스트")
    void 시니어_미션_참가자_조회() throws Exception {
        // given
        Mission mission = Mission.builder()
                .missionId(1L)
                .build();

        Member member = Member.builder()
                .memberId(1L)
                .githubId("test-token-senior")
                .nickName("test-senior")
                .build();

        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(member));
        given(missionService.getMission(any())).willReturn(mission);
        String memberAccessToken = getMockToken(member);


        // Setup mockResponse with the sample data
        List<TechTag> techTags = Arrays.asList(
                new TechTag(7L, "Android", "https://img.url"),
                new TechTag(12L, "Kotlin", "https://img.url"),
                new TechTag(13L, "iOS", "https://img.url")
        );

        MemberRegisterSimpleInfo memberInfo = new MemberRegisterSimpleInfo(
                114L,
                "1호선 광인",
                "No.1_crazier",
                "https://loremflickr.com/cache/resized/65535_52449252296_9a9872253f_c_460_460_nofilter.jpg",
                ProcessStatus.WAITING_FOR_PAYMENT,
                null
        );
        memberInfo.setPaymentDate(null);
        memberInfo.setMissionFinishedDate(null);

        RegistrationSeniorResponse mockResponse = RegistrationSeniorResponse.builder()
                .missionName("당근마켓 리드가 직접 알려주는 당근마켓 인프라 찍먹하기")
                .techTagList(techTags)
                .memberInfoList(Arrays.asList(memberInfo))
                .build();

        given(registrationService.getSeniorEnrollInfo(member, mission)).willReturn(mockResponse);
        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/senior", mission.getMissionId())
                        .header("Authorization", "Bearer " + memberAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data.missionName").value("당근마켓 리드가 직접 알려주는 당근마켓 인프라 찍먹하기"))
                .andExpect(jsonPath("$.data.techTagList[0].techTagId").value(7))
                .andExpect(jsonPath("$.data.techTagList[0].name").value("Android"));

        // document
        actions.andDo(document(
                "get-senior-dashboard",  // 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
                resource(ResourceSnippetParameters.builder()
                        .summary("[미션 진행] 시니어 미션 참가자 조회")
                        .tag("미션 진행")
                        .description(CustomMDGenerator.builder()
                                .h1("[Descriptions]")
                                .h3("시니어가 가 자신의 미션 참가자 정보를 조회한다.")
                                .h1("[Request Headers]")
                                .table(
                                        tableHead("Request values", "Data Type", "Description"),
                                        //Authorization
                                        tableRow("Authorization", "String", "액세스 토큰")
                                )
                                .h1("[Path Variables]")
                                .table(
                                        tableHead("Request values", "Data Type", "Description"),
                                        tableRow("missionId", "Long", "미션 아이디")
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
                                fieldWithPath("data.techTagList[].iconImageUrl").type(JsonFieldType.STRING).description("기술 태그 이미지 URL"),
                                fieldWithPath("data.memberInfoList").type(JsonFieldType.ARRAY).description("미션 참가자 리스트"),
                                fieldWithPath("data.memberInfoList[].memberId").type(JsonFieldType.NUMBER).description("회원 아이디"),
                                fieldWithPath("data.memberInfoList[].nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                                fieldWithPath("data.memberInfoList[].githubId").type(JsonFieldType.STRING).description("회원 깃허브 아이디"),
                                fieldWithPath("data.memberInfoList[].profileImageUrl").type(JsonFieldType.STRING).description("회원 프로필 이미지 URL"),
                                fieldWithPath("data.memberInfoList[].processStatus").type(JsonFieldType.STRING).description("회원 미션 참가 상태"),
                                fieldWithPath("data.memberInfoList[].paymentDate").type(JsonFieldType.STRING).description("회원 미션 참가 결제일"),
                                fieldWithPath("data.memberInfoList[].missionFinishedDate").type(JsonFieldType.STRING).description("회원 미션 완료일"),
                                fieldWithPath("data.memberInfoList[].githubPrUrl").type(JsonFieldType.STRING).description("회원 미션 PR URL")
                        )
                        .build())));
    }

    @Test
    @DisplayName("시니어 미션 참가자 조회 실패 테스트 - 시니어가 아닌 경우")
    void 시니어_미션_참가자_조회_실패_시니어가_아님() throws Exception {
        // given
        Mission mission = Mission.builder()
                .missionId(1L)
                .build();

        Member member = Member.builder()
                .memberId(1L)
                .githubId("test-token")
                .build();
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(member));
        given(missionService.getMission(any())).willReturn(mission);

        given(registrationService.getSeniorEnrollInfo(member, mission)).willThrow(new NotSeniorMember());

        String memberAccessToken = getMockToken(member);
        // when

        // then
        ResponseCode expectedResponseCode = ResponseCode.NOT_SENIOR_MEMBER;

        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/senior", mission.getMissionId())
                        .header("Authorization", "Bearer " + memberAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(document(
                "get-senior-dashboard-fail-not-senior",  // 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint())));       // response JSON 정렬하여 출력
    }

    @Test
    @DisplayName("시니어 미션 참가자 조회 실패 테스트 - 미션이 없는 경우")
    void 시니어_미션_참가자_조회_실패_미션_없음() throws Exception {
        // given
        Mission mission = Mission.builder()
                .missionId(1L)
                .build();

        Member member = Member.builder()
                .memberId(1L)
                .githubId("test-token")
                .build();
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(member));
        given(missionService.getMission(any())).willReturn(mission);

        String memberAccessToken = getMockToken(member);

        given(registrationService.getSeniorEnrollInfo(member, mission)).willThrow(new NotExistMission());
        // when

        // then
        ResponseCode expectedResponseCode = ResponseCode.NOT_EXIST_MISSION;

        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/senior", mission.getMissionId())
                        .header("Authorization", "Bearer " + memberAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(document(
                "get-senior-dashboard-not-exist-mission",  // 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint())));       // response JSON 정렬하여 출력
    }

    @Test
    @DisplayName("시니어 미션 참가자 조회 실패 테스트 - 자신의 미션이 아닌 경우")
    void 시니어_미션_참가자_조회_실패_자신의_미션이_아님() throws Exception {
        // given
        Mission mission = Mission.builder()
                .missionId(1L)
                .build();

        Member member = Member.builder()
                .memberId(1L)
                .githubId("test-token")
                .build();
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(member));
        given(missionService.getMission(any())).willReturn(mission);

        String memberAccessToken = getMockToken(member);

        given(registrationService.getSeniorEnrollInfo(member, mission)).willThrow(new NotMyMission());
        // when

        // then
        ResponseCode expectedResponseCode = ResponseCode.NOT_MY_MISSION;

        ResultActions actions = mockMvc.perform(get("/v1/mission/{missionId}/senior", mission.getMissionId())
                        .header("Authorization", "Bearer " + memberAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(document(
                "get-senior-dashboard-fail-not-my-mission",  // 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint())));       // response JSON 정렬하여 출력
    }

    private String getMockToken(Member member) {
        return tokenProvider.createToken(member.getGithubId(), TokenType.ACCESS_TOKEN);
    }
}