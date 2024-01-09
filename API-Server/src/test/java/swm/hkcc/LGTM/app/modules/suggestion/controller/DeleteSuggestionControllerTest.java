package swm.hkcc.LGTM.app.modules.suggestion.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
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
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.suggestion.exception.NotExistSuggestion;
import swm.hkcc.LGTM.app.modules.suggestion.exception.NotMySuggestion;
import swm.hkcc.LGTM.app.modules.suggestion.service.SuggestionService;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.util.Collections;
import java.util.Optional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static swm.hkcc.LGTM.utils.CustomMDGenerator.tableHead;
import static swm.hkcc.LGTM.utils.CustomMDGenerator.tableRow;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class DeleteSuggestionControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private TokenProvider tokenProvider;
    @MockBean
    private SuggestionService suggestionService;
    @MockBean
    private MemberRepository memberRepository;
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
    @DisplayName("제안 삭제 동작 테스트")
    void deleteSuggestion() throws Exception {
        // given
        Member junior = getMockJunior();
        String token = getMockToken(junior);
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(junior));

        // when
        // then
        ResultActions actions = mockMvc.perform(delete("/v1/suggestion/{suggestionId}", 1L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data.suggestionId").value(1L))
                .andExpect(jsonPath("$.data.success").value(true));

        // documentation
        actions.andDo(document(
                "delete-suggestion",
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
                resource(ResourceSnippetParameters.builder()
                        .summary("[제안] 미션 제안하기 삭제")
                        .tag("제안하기")
                        .description(CustomMDGenerator.builder()
                                .h1("[Descriptions]")
                                .table(
                                        tableHead("Request values", "Data Type", "Description"),
                                        tableRow("Authorization", "String", "액세스 토큰")
                                )
                                .h1("[Path Variables]")
                                .table(
                                        tableHead("Request values", "Data Type", "Description"),
                                        tableRow("suggestionId", "Number", "게시글 ID")
                                )
                                .h1("[Response Fields]")
                                .table(
                                        tableHead("Response values", "Data Type", "Description"),
                                        tableRow("success", "Boolean", "성공 여부"),
                                        tableRow("responseCode", "Number", "응답 코드"),
                                        tableRow("message", "String", "응답 메시지"),
                                        tableRow("data.suggestionId", "Number", "제안 ID"),
                                        tableRow("data.success", "Boolean", "성공 여부")
                                )
                                .h1("[Errors]")
                                .table(
                                        tableHead("Error Code", "Error Message", "Description"),
                                        tableRow(
                                                ResponseCode.NOT_EXIST_SUGGESTION.getHttpStatus().toString(),
                                                ResponseCode.NOT_EXIST_SUGGESTION.getCode().toString(),
                                                ResponseCode.NOT_EXIST_SUGGESTION.getMessage()
                                        ),
                                        tableRow(
                                                ResponseCode.NOT_MY_SUGGESTION.getHttpStatus().toString(),
                                                ResponseCode.NOT_MY_SUGGESTION.getCode().toString(),
                                                ResponseCode.NOT_MY_SUGGESTION.getMessage()
                                        )
                                )

                                .build())
                        .requestHeaders(headerWithName("Authorization").description("access token"))
                        .pathParameters(parameterWithName("suggestionId").description("제안 ID"))
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.suggestionId").type(JsonFieldType.NUMBER).description("제안 ID"),
                                fieldWithPath("data.success").type(JsonFieldType.BOOLEAN).description("성공 여부")
                        )
                        .build())));
    }

    @Test
    @DisplayName("제안 삭제 실패 테스트 - 존재하지 않는 게시글")
    void deleteSuggestionFailBecauseNotExistSuggestion() throws Exception {
        // given
        Member junior = getMockJunior();
        String token = getMockToken(junior);
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(junior));
        given(suggestionService.deleteSuggestion(any(), any())).willThrow(new NotExistSuggestion());


        // when
        // then
        ResponseCode responseCode = ResponseCode.NOT_EXIST_SUGGESTION;
        ResultActions actions = mockMvc.perform(delete("/v1/suggestion/{suggestionId}", 1L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(responseCode.getCode()))
                .andExpect(jsonPath("$.message").value(responseCode.getMessage()));
        // documentation
        actions.andDo(document("delete-suggestion-fail-because-not-exist-suggestion"));
    }

    @Test
    @DisplayName("제안 삭제 실패 테스트 - 권한이 없는 게시글")
    void deleteSuggestionFailBecauseNotMySuggestion() throws Exception {
        // given
        Member junior = getMockJunior();
        String token = getMockToken(junior);
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(junior));
        given(suggestionService.deleteSuggestion(any(), any())).willThrow(new NotMySuggestion());


        // when
        // then
        ResponseCode responseCode = ResponseCode.NOT_MY_SUGGESTION;
        ResultActions actions = mockMvc.perform(delete("/v1/suggestion/{suggestionId}", 1L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(responseCode.getCode()))
                .andExpect(jsonPath("$.message").value(responseCode.getMessage()));

        // documentation
        actions.andDo(document("delete-suggestion-fail-because-not-exist-suggestion"));
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