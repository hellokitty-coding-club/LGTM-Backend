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
import swm.hkcc.LGTM.app.modules.auth.constants.TokenType;
import swm.hkcc.LGTM.app.modules.auth.utils.jwt.TokenProvider;
import swm.hkcc.LGTM.app.modules.member.domain.Authority;
import swm.hkcc.LGTM.app.modules.member.domain.Junior;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MissionHistoryInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.RegistrationJuniorResponse;
import swm.hkcc.LGTM.app.modules.registration.service.RegistrationService;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("주니어_미션_대시보드 동작 테스트")
    void 주니어_미션_대시보드() throws Exception {
        // given
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockJunior()));

        List<MissionHistoryInfo> missionHistory = List.of(
                MissionHistoryInfo.builder().status(ProcessStatus.WAITING_FOR_PAYMENT).dateTime(LocalDateTime.now().toString()).build()
        );
        ProcessStatus status = ProcessStatus.WAITING_FOR_PAYMENT;

        RegistrationJuniorResponse mockResponse = RegistrationJuniorResponse.builder()
                .missionName("미션 이름")
                .techTagList(getMockTechTags())
                .processStatus(status)
                .missionHistory(missionHistory)
                .buttonTitle(status.getJuniorBottomTitle())
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
                "get-junior-dashboard",// 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
                resource(
                        ResourceSnippetParameters.builder()
                                .summary("[미션 진행] 주니어 미션 진행상황 조회")
                                .tag("미션 진행")
                                .description(CustomMDGenerator.builder()
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
                                        fieldWithPath("data.processStatus").type(JsonFieldType.STRING).description("미션 진행 상태"),
                                        fieldWithPath("data.missionHistory").type(JsonFieldType.ARRAY).description("미션 진행 이력"),
                                        fieldWithPath("data.missionHistory[].status").type(JsonFieldType.STRING).description("미션 진행 상태"),
                                        fieldWithPath("data.missionHistory[].dateTime").type(JsonFieldType.STRING).description("미션 진행 시간"),
                                        fieldWithPath("data.buttonTitle").type(JsonFieldType.STRING).description("버튼 타이틀")
                                )
                                .build())));
    }



    private List<TechTag> getMockTechTags() {
        return Arrays.asList(
                new TechTag(7L, "Android"),
                new TechTag(12L, "Kotlin"),
                new TechTag(13L, "iOS")
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
