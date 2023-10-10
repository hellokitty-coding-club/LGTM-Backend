package swm.hkcc.LGTM.app.modules.mission.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.service.DeleteMissionService;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.util.Collections;
import java.util.Optional;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
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
class DeleteMissionControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private TokenProvider tokenProvider;
    @MockBean
    private DeleteMissionService deleteMissionService;
    @MockBean
    private MemberRepository memberRepository;
    private Member mockJunior;
    private Member mockSenior;


    @Test
    @DisplayName("미션 삭제 동작 테스트")
    void 미션_삭제_동작_테스트() throws Exception {
        // given
        Mission mission = getMockMission();
        given(memberRepository.findOneByGithubId(anyString())).willReturn(Optional.ofNullable(getMockSenior()));

        // when
        // then
        ResultActions actions = mockMvc.perform(delete("/v1/mission/{missionId}", mission.getMissionId())
                        .header("Authorization", "Bearer " + getMockToken(getMockSenior()))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"));

        // document
        actions.andDo(document(
                "delete-mission",// 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
                resource(
                        ResourceSnippetParameters.builder()
                                .summary("[미션] 미션 삭제")
                                .tag("미션")
                                .description(CustomMDGenerator.builder()
                                        .h1("미션 삭제")
                                        .h3("미션을 삭제합니다.")
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
                                                tableRow("writerId", "Long", "작성자 아이디"),
                                                tableRow("missionId", "Long", "삭제한 미션 아이디")
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
                                                        ResponseCode.IMPOSSIBLE_TO_DELETE.getHttpStatus().toString(),
                                                        ResponseCode.IMPOSSIBLE_TO_DELETE.getCode().toString(),
                                                        ResponseCode.IMPOSSIBLE_TO_DELETE.getMessage()
                                                )

                                        )
                                        .build())
                                .requestHeaders(headerWithName("Authorization").description("access token"))
                                .pathParameters(
                                        parameterWithName("missionId").type(SimpleType.NUMBER).description("미션 아이디")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.writerId").type(JsonFieldType.NUMBER).description("작성자 아이디"),
                                        fieldWithPath("data.missionId").type(JsonFieldType.NUMBER).description("미션 아이디")
                                )
                                .build())));
    }

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
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