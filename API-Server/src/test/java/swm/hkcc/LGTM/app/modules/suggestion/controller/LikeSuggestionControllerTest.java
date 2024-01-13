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
import swm.hkcc.LGTM.app.modules.suggestion.dto.LikeSuggestionResponse;
import swm.hkcc.LGTM.app.modules.suggestion.dto.SuggestionDto;
import swm.hkcc.LGTM.app.modules.suggestion.service.SuggestionService;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
class LikeSuggestionControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private TokenProvider tokenProvider;
    @MockBean
    private SuggestionService suggestionService;
    @MockBean
    private MemberRepository memberRepository;
    private Member mockJunior;
    private Member mockSenior;
    private List<SuggestionDto> suggestionDtos;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
        LocalDateTime localDateTime = LocalDateTime.now();
    }

    @Test
    @DisplayName("좋아요 동작 테스트")
    void likeSuggestion() throws Exception {
        // given
        Member junior = getMockJunior();
        String token = getMockToken(junior);
        LikeSuggestionResponse likeSuggestionResponse = LikeSuggestionResponse.from(1200, true);
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(junior));
        given(suggestionService.likeSuggestion(any(), any())).willReturn(likeSuggestionResponse);
        // when
        // then
        ResultActions resultActions = mockMvc.perform(post("/v1/suggestion/{suggestionId}/like", 1L)
                        .header("Authorization", "Bearer " + getMockToken(getMockJunior()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data.isLiked").value(true))
                .andExpect(jsonPath("$.data.likeNum").value("999+"));
        // document
        resultActions.andDo(document(
                "post-like-suggestion",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .summary("[제안] 좋아요 누르기")
                                .tag("제안하기")
                                .description( CustomMDGenerator.builder()
                                        .h1("[Request Headers]")
                                        .table(
                                                tableHead("Request values", "Data Type", "Description"),
                                                tableRow("Authorization", "String", "액세스 토큰")
                                        )
                                        .line()
                                        .h1("[Path Variables]")
                                        .table(
                                                tableHead("Request values", "Data Type", "Description"),
                                                tableRow("suggestionId", "Number", "게시글 ID")
                                        )
                                        .line()
                                        .h1("[Response Fields]")
                                        .table(
                                                tableHead("Request values", "Data Type", "Description"),
                                                tableRow("success", "Boolean", "성공 여부"),
                                                tableRow("responseCode", "Number", "응답 코드"),
                                                tableRow("message", "String", "응답 메시지"),
                                                tableRow("data.isLiked", "Boolean", "해당 회원이 게시글 좋아요를 눌렀는지 여부"),
                                                tableRow("data.likeNum", "String", "게시글 좋아요 수, 1000개 이상일 경우 999+로 표시")
                                        )
                                        .line()
                                        .h1("[Errors]")
                                        .table(
                                                tableHead("HTTP Status", "Response Code", "Message"),
                                                tableRow(
                                                        ResponseCode.NOT_EXIST_SUGGESTION.getHttpStatus().toString(),
                                                        ResponseCode.NOT_EXIST_SUGGESTION.getCode().toString(),
                                                        ResponseCode.NOT_EXIST_SUGGESTION.getMessage()
                                                )
                                        )
                                        .line()
                                        .build())
                                .requestHeaders(
                                        headerWithName("Authorization").description("Bearer Token")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data.isLiked").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
                                        fieldWithPath("data.likeNum").type(JsonFieldType.STRING).description("좋아요 수")
                                )
                                .build())));
    }

    @Test
    @DisplayName("좋아요 취소 동작 테스트")
    void dislikeSuggestion() throws Exception {
        // given
        Member junior = getMockJunior();
        String token = getMockToken(junior);
        LikeSuggestionResponse likeSuggestionResponse = LikeSuggestionResponse.from(1200, false);
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(junior));
        given(suggestionService.cancelLikeSuggestion(any(), any())).willReturn(likeSuggestionResponse);
        // when
        // then
        ResultActions resultActions = mockMvc.perform(delete("/v1/suggestion/{suggestionId}/like", 1L)
                        .header("Authorization", "Bearer " + getMockToken(getMockJunior()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data.isLiked").value(false))
                .andExpect(jsonPath("$.data.likeNum").value("999+"));
        // document
        resultActions.andDo(document(
                "post-cancel-like-suggestion",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .summary("[제안] 좋아요 취소하기")
                                .tag("제안하기")
                                .description( CustomMDGenerator.builder()
                                        .h1("[Request Headers]")
                                        .table(
                                                tableHead("Request values", "Data Type", "Description"),
                                                tableRow("Authorization", "String", "액세스 토큰")
                                        )
                                        .line()
                                        .h1("[Path Variables]")
                                        .table(
                                                tableHead("Request values", "Data Type", "Description"),
                                                tableRow("suggestionId", "Number", "게시글 ID")
                                        )
                                        .line()
                                        .h1("[Response Fields]")
                                        .table(
                                                tableHead("Request values", "Data Type", "Description"),
                                                tableRow("success", "Boolean", "성공 여부"),
                                                tableRow("responseCode", "Number", "응답 코드"),
                                                tableRow("message", "String", "응답 메시지"),
                                                tableRow("data.isLiked", "Boolean", "해당 회원이 게시글 좋아요를 눌렀는지 여부"),
                                                tableRow("data.likeNum", "String", "게시글 좋아요 수, 1000개 이상일 경우 999+로 표시")
                                        )
                                        .line()
                                        .h1("[Errors]")
                                        .table(
                                                tableHead("HTTP Status", "Response Code", "Message"),
                                                tableRow(
                                                        ResponseCode.NOT_EXIST_SUGGESTION.getHttpStatus().toString(),
                                                        ResponseCode.NOT_EXIST_SUGGESTION.getCode().toString(),
                                                        ResponseCode.NOT_EXIST_SUGGESTION.getMessage()
                                                )
                                        )
                                        .line()
                                        .build())
                                .requestHeaders(
                                        headerWithName("Authorization").description("Bearer Token")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data.isLiked").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
                                        fieldWithPath("data.likeNum").type(JsonFieldType.STRING).description("좋아요 수")
                                )
                                .build())));
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