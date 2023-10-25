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
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.member.exception.NotJuniorMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.member.service.MemberService;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.exception.NotExistMission;
import swm.hkcc.LGTM.app.modules.mission.service.MissionService;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MemberRegisterSimpleInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.RegistrationSeniorResponse;
import swm.hkcc.LGTM.app.modules.registration.exception.AlreadyRegisteredMission;
import swm.hkcc.LGTM.app.modules.registration.exception.FullRegisterMembers;
import swm.hkcc.LGTM.app.modules.registration.exception.MissRegisterDeadline;
import swm.hkcc.LGTM.app.modules.registration.service.RegistrationService;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
class RegistrationMissionControllerTest {
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
    @DisplayName("미션 등록 동작 테스트")
    void 미션_등록() throws Exception {
        // given
        Mission mission = Mission.builder()
                .missionId(1L)
                .build();

        Member member = Member.builder()
                .memberId(1L)
                .githubId("test-token-junior")
                .nickName("test-junior")
                .build();
        String memberAccessToken = getMockToken(member);

        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(member));


        given(registrationService.registerJunior(member, mission)).willReturn(1L);
        // when
        // then
        ResultActions actions = mockMvc.perform(post("/v1/mission/{missionId}", mission.getMissionId())
                        .header("Authorization", "Bearer " + memberAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data").value(true));

        // document
        actions.andDo(document(
                "register-junior",  // 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
                resource(ResourceSnippetParameters.builder()
                        .summary("[미션] 주니어 미션 참가")
                        .tag("미션")
                        .description(CustomMDGenerator.builder()
                                .h1("[Descriptions]")
                                .h3("주니어가 미션에 참가한다.")
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
                                                ResponseCode.ALREADY_REGISTERED_MISSION.getHttpStatus().toString(),
                                                ResponseCode.ALREADY_REGISTERED_MISSION.getCode().toString(),
                                                ResponseCode.ALREADY_REGISTERED_MISSION.getMessage()
                                        ),
                                        tableRow(
                                                ResponseCode.MISS_REGISTER_DEADLINE.getHttpStatus().toString(),
                                                ResponseCode.MISS_REGISTER_DEADLINE.getCode().toString(),
                                                ResponseCode.MISS_REGISTER_DEADLINE.getMessage()
                                        ),
                                        tableRow(
                                                ResponseCode.FULL_REGISTER_MEMBERS.getHttpStatus().toString(),
                                                ResponseCode.FULL_REGISTER_MEMBERS.getCode().toString(),
                                                ResponseCode.FULL_REGISTER_MEMBERS.getMessage()
                                        )
                                )
                                .build())
                        .requestHeaders(headerWithName("Authorization").description("access token"))
                        .pathParameters(parameterWithName("missionId").type(SimpleType.NUMBER).description("미션 아이디"))
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("응답 데이터")
                        )
                        .build())));
    }

    @Test
    @DisplayName("미션 등록 실패 테스트 - 주니어 아님")
    void 미션_등록_실패_주니어_아님() throws Exception {
        // given
        Mission mission = Mission.builder()
                .missionId(1L)
                .build();

        Member member = Member.builder()
                .memberId(1L)
                .githubId("test-token-junior")
                .nickName("test-junior")
                .build();
        String memberAccessToken = getMockToken(member);

        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(member));
        given(missionService.getMission(any())).willReturn(mission);


        given(registrationService.registerJunior(member, mission)).willThrow(new NotJuniorMember());

        // when
        // then
        ResponseCode expectedResponseCode = ResponseCode.NOT_JUNIOR_MEMBER;
        ResultActions actions = mockMvc.perform(post("/v1/mission/{missionId}", mission.getMissionId())
                        .header("Authorization", "Bearer " + memberAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedResponseCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(document(
                "register-junior-not-junior",  // 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint())));      // response JSON 정렬하여 출력
    }

    @Test
    @DisplayName("미션 등록 실패 테스트 - 미션 없음")
    void 미션_등록_실패_미션_없음() throws Exception {
        // given
        Mission mission = Mission.builder()
                .missionId(1L)
                .build();

        Member member = Member.builder()
                .memberId(1L)
                .githubId("test-token-junior")
                .nickName("test-junior")
                .build();
        String memberAccessToken = getMockToken(member);

        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(member));
        given(missionService.getMission(any())).willReturn(mission);

        given(registrationService.registerJunior(member, mission)).willThrow(new NotExistMission());

        // when
        // then
        ResponseCode expectedResponseCode = ResponseCode.NOT_EXIST_MISSION;
        ResultActions actions = mockMvc.perform(post("/v1/mission/{missionId}", mission.getMissionId())
                        .header("Authorization", "Bearer " + memberAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedResponseCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(document(
                "register-junior-not-exist-mission",  // 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint())));      // response JSON 정렬하여 출력
    }

    @Test
    @DisplayName("미션 등록 실패 테스트 - 이미등록한 미션")
    void 미션_등록_실패_ALREADY_REGISTERED_MISSION() throws Exception {
        // given
        Mission mission = Mission.builder()
                .missionId(1L)
                .build();

        Member member = Member.builder()
                .memberId(1L)
                .githubId("test-token-junior")
                .nickName("test-junior")
                .build();
        String memberAccessToken = getMockToken(member);

        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(member));
        given(missionService.getMission(any())).willReturn(mission);

        given(registrationService.registerJunior(member, mission)).willThrow(new AlreadyRegisteredMission());

        // when
        // then
        ResponseCode expectedResponseCode = ResponseCode.ALREADY_REGISTERED_MISSION;
        ResultActions actions = mockMvc.perform(post("/v1/mission/{missionId}", mission.getMissionId())
                        .header("Authorization", "Bearer " + memberAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedResponseCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(document(
                "register-junior-already-registered-mission",  // 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint())));      // response JSON 정렬하여 출력
    }

    @Test
    @DisplayName("미션 등록 실패 테스트 - 마감기한 지남")
    void 미션_등록_실패_MISS_REGISTER_DEADLINE() throws Exception {

        // given
        Mission mission = Mission.builder()
                .missionId(1L)
                .build();

        Member member = Member.builder()
                .memberId(1L)
                .githubId("test-token-junior")
                .nickName("test-junior")
                .build();
        String memberAccessToken = getMockToken(member);

        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(member));
        given(missionService.getMission(any())).willReturn(mission);

        given(registrationService.registerJunior(member, mission)).willThrow(new MissRegisterDeadline());

        // when
        // then
        ResponseCode expectedResponseCode = ResponseCode.MISS_REGISTER_DEADLINE;
        ResultActions actions = mockMvc.perform(post("/v1/mission/{missionId}", mission.getMissionId())
                        .header("Authorization", "Bearer " + memberAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedResponseCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(document(
                "register-junior-miss-register-deadline",  // 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint())));      // response JSON 정렬하여 출력
    }

    @Test
    @DisplayName("미션 등록 실패 테스트 - 미션 참가 인원 초과")
    void 미션_등록_실패_FULL_REGISTER_MEMBERS() throws Exception {

        // given
        Mission mission = Mission.builder()
                .missionId(1L)
                .build();

        Member member = Member.builder()
                .memberId(1L)
                .githubId("test-token-junior")
                .nickName("test-junior")
                .build();
        String memberAccessToken = getMockToken(member);

        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(member));
        given(missionService.getMission(any())).willReturn(mission);

        given(registrationService.registerJunior(member, mission)).willThrow(new FullRegisterMembers());

        // when
        // then
        ResponseCode expectedResponseCode = ResponseCode.FULL_REGISTER_MEMBERS;
        ResultActions actions = mockMvc.perform(post("/v1/mission/{missionId}", mission.getMissionId())
                        .header("Authorization", "Bearer " + memberAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedResponseCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions.andDo(document(
                "register-junior-full-register-members",  // 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint())));      // response JSON 정렬하여 출력
    }

    private String getMockToken(Member member) {
        return tokenProvider.createToken(member.getGithubId(), TokenType.ACCESS_TOKEN);
    }
}