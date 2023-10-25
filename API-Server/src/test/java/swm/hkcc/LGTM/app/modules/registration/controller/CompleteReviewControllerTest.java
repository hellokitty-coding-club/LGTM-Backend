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
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.exception.NotJuniorMember;
import swm.hkcc.LGTM.app.modules.member.exception.NotSeniorMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.member.service.MemberService;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.exception.NotExistMission;
import swm.hkcc.LGTM.app.modules.mission.service.MissionService;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MissionHistoryInfo;
import swm.hkcc.LGTM.app.modules.registration.exception.NotMyMission;
import swm.hkcc.LGTM.app.modules.registration.exception.NotRegisteredMission;
import swm.hkcc.LGTM.app.modules.registration.service.RegistrationService;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
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
public class CompleteReviewControllerTest {
    private MockMvc mockMvc;


    @Autowired
    private TokenProvider tokenProvider;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MissionService missionService;

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
    @DisplayName("completeReview_동작_테스트")
    void completeReview_동작_테스트() throws Exception {
        // given
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(getMockSenior()));
        given(memberService.getMember(any())).willReturn(getMockJunior());
        given(missionService.getMission(any())).willReturn(getMockMission());
        given(registrationService.completeReview(any(), any(), any())).willReturn(
                MissionHistoryInfo.builder()
                        .status(ProcessStatus.MISSION_FINISHED)
                        .dateTime(LocalDateTime.now().toString())
                        .build()
        );

        // when
        // then
        ResultActions actions = mockMvc.perform(post("/v1/mission/{missionId}/review/{juniorId}", getMockMission().getMissionId(), getMockJunior().getMemberId())
                        .header("Authorization", "Bearer " + getMockToken(getMockSenior()))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"));

        // documentation
        actions.andDo(document(
                "post-senior-complete-review",// 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
                resource(
                        ResourceSnippetParameters.builder()
                                .summary("[미션 진행] 시니어 리뷰완료")
                                .tag("미션 진행")
                                .description(CustomMDGenerator.builder()
                                        .h1("[Descriptions]")
                                        .h3("시니어 리뷰완료")
                                        .h1("[Request Headers]")
                                        .table(
                                                tableHead("Request values", "Data Type", "Description"),
                                                tableRow("Authorization", "String", "액세스 토큰")
                                        )
                                        .h1("[Path Variables]")
                                        .table(
                                                tableHead("Request values", "Data Type", "Description"),
                                                tableRow("missionId", "Long", "미션 아이디"),
                                                tableRow("juniorId", "Long", "주니어 아이디")
                                        )
                                        .line()
                                        .h1("[Response Body]")
                                        .table(
                                                tableHead("Response values", "Data Type", "Description"),
                                                tableRow("data.status", "String", "변경된 미션 진행 상태"),
                                                tableRow("data.dateTime", "String", "변경 시간")
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
                                        ).build())
                                .requestHeaders(headerWithName("Authorization").description("access token"))
                                .pathParameters(
                                        parameterWithName("missionId").type(SimpleType.NUMBER).description("미션 아이디"),
                                        parameterWithName("juniorId").type(SimpleType.NUMBER).description("주니어 아이디")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.status").type(JsonFieldType.STRING).description("미션 진행 상태"),
                                        fieldWithPath("data.dateTime").type(JsonFieldType.STRING).description("미션 진행 시간")
                                )
                                .build())));
    }

    //NOT_SENIOR_MEMBER
    @Test
    @DisplayName("completeReview_예외_NOT_SENIOR_MEMBER")
    void completeReview_예외_NOT_SENIOR_MEMBER() throws Exception {
        // given
        ResponseCode expectedResponseCode = ResponseCode.NOT_SENIOR_MEMBER;

        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockSenior()));
        given(memberService.getMember(any())).willReturn(getMockJunior());
        given(missionService.getMission(any())).willReturn(getMockMission());
        given(registrationService.completeReview(any(), any(), any())).willThrow(new NotSeniorMember());

        // when
        // then
        ResultActions actions = mockMvc.perform(post("/v1/mission/{missionId}/review/{juniorId}", getMockMission().getMissionId(), getMockJunior().getMemberId())
                        .header("Authorization", "Bearer " + getMockToken(getMockSenior()))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(MockMvcRestDocumentationWrapper.document(
                "post-senior-complete-review-fail-not-senior-member"// 문서의 고유 id
        ));
    }

    @Test
    @DisplayName("completeReview_예외_NOT_EXIST_MISSION")
    void completeReview_예외_NOT_EXIST_MISSION() throws Exception {
        // given
        ResponseCode expectedResponseCode = ResponseCode.NOT_EXIST_MISSION;

        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockSenior()));
        given(memberService.getMember(any())).willReturn(getMockJunior());
        given(missionService.getMission(any())).willReturn(getMockMission());
        given(registrationService.completeReview(any(), any(), any())).willThrow(new NotExistMission());

        // when
        // then
        ResultActions actions = mockMvc.perform(post("/v1/mission/{missionId}/review/{juniorId}", getMockMission().getMissionId(), getMockJunior().getMemberId())
                        .header("Authorization", "Bearer " + getMockToken(getMockSenior()))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(MockMvcRestDocumentationWrapper.document(
                "post-senior-complete-review-fail-not-exist-mission"// 문서의 고유 id
        ));
    }

    @Test
    @DisplayName("completeReview_예외_NOT_MY_MISSION")
    void completeReview_예외_NOT_MY_MISSION() throws Exception {
        // given
        ResponseCode expectedResponseCode = ResponseCode.NOT_MY_MISSION;

        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockSenior()));
        given(memberService.getMember(any())).willReturn(getMockJunior());
        given(missionService.getMission(any())).willReturn(getMockMission());
        given(registrationService.completeReview(any(), any(), any())).willThrow(new NotMyMission());

        // when
        // then
        ResultActions actions = mockMvc.perform(post("/v1/mission/{missionId}/review/{juniorId}", getMockMission().getMissionId(), getMockJunior().getMemberId())
                        .header("Authorization", "Bearer " + getMockToken(getMockSenior()))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(MockMvcRestDocumentationWrapper.document(
                "post-senior-complete-review-fail-not-my-mission"// 문서의 고유 id
        ));
    }

    @Test
    @DisplayName("completeReview_예외_NOT_EXIST_MEMBER")
    void completeReview_예외_NOT_EXIST_MEMBER() throws Exception {
        // given
        ResponseCode expectedResponseCode = ResponseCode.NOT_EXIST_MEMBER;

        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockSenior()));
        given(memberService.getMember(any())).willReturn(getMockJunior());
        given(missionService.getMission(any())).willReturn(getMockMission());
        given(registrationService.completeReview(any(), any(), any())).willThrow(new NotExistMember());

        // when
        // then
        ResultActions actions = mockMvc.perform(post("/v1/mission/{missionId}/review/{juniorId}", getMockMission().getMissionId(), getMockJunior().getMemberId())
                        .header("Authorization", "Bearer " + getMockToken(getMockSenior()))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(MockMvcRestDocumentationWrapper.document(
                "post-senior-complete-review-fail-not-exist-member"// 문서의 고유 id
        ));
    }

    @Test
    @DisplayName("completeReview_예외_NOT_JUNIOR_MEMBER")
    void completeReview_예외_NOT_JUNIOR_MEMBER() throws Exception {
        // given
        ResponseCode expectedResponseCode = ResponseCode.NOT_JUNIOR_MEMBER;

        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockSenior()));
        given(memberService.getMember(any())).willReturn(getMockJunior());
        given(missionService.getMission(any())).willReturn(getMockMission());
        given(registrationService.completeReview(any(), any(), any())).willThrow(new NotJuniorMember());

        // when
        // then
        ResultActions actions = mockMvc.perform(post("/v1/mission/{missionId}/review/{juniorId}", getMockMission().getMissionId(), getMockJunior().getMemberId())
                        .header("Authorization", "Bearer " + getMockToken(getMockSenior()))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(MockMvcRestDocumentationWrapper.document(
                "post-senior-complete-review-fail-not-junior-member"// 문서의 고유 id
        ));
    }

    @Test
    @DisplayName("completeReview_예외_NOT_REGISTERED_MISSION")
    void completeReview_예외_NOT_REGISTERED_MISSION() throws Exception {
        // given
        ResponseCode expectedResponseCode = ResponseCode.NOT_REGISTERED_MISSION;

        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockSenior()));
        given(memberService.getMember(any())).willReturn(getMockJunior());
        given(missionService.getMission(any())).willReturn(getMockMission());
        given(registrationService.completeReview(any(), any(), any())).willThrow(new NotRegisteredMission());

        // when
        // then
        ResultActions actions = mockMvc.perform(post("/v1/mission/{missionId}/review/{juniorId}", getMockMission().getMissionId(), getMockJunior().getMemberId())
                        .header("Authorization", "Bearer " + getMockToken(getMockSenior()))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(MockMvcRestDocumentationWrapper.document(
                "post-senior-complete-review-fail-not-registered-mission"// 문서의 고유 id
        ));
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
