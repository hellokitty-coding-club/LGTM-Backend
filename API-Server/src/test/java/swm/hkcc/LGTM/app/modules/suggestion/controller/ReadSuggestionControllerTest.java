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
import swm.hkcc.LGTM.app.modules.suggestion.dto.SuggestionDto;
import swm.hkcc.LGTM.app.modules.suggestion.dto.SuggestionListDto;
import swm.hkcc.LGTM.app.modules.suggestion.exception.NotExistSuggestion;
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
class ReadSuggestionControllerTest {
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

        suggestionDtos = List.of(
                SuggestionDto.builder()
                        .suggestionId(1L)
                        .title("title1")
                        .description("description1")
                        .date(localDateTime)
                        .likeNum("1")
                        .isLiked(true)
                        .isMyPost(true)
                        .build(),
                SuggestionDto.builder()
                        .suggestionId(2L)
                        .title("title2")
                        .description("description2")
                        .date(localDateTime)
                        .likeNum("2")
                        .isLiked(false)
                        .isMyPost(false)
                        .build()
        );
    }

    @DisplayName("제안 리스트 조회")
    @Test
    void readSuggestionList() throws Exception {
        // given
        Member junior = getMockJunior();
        String token = getMockToken(junior);
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(junior));
        given(suggestionService.getSuggestionList(any(Member.class))).willReturn(suggestionDtos);

        SuggestionListDto suggestionListDto = SuggestionListDto.from(suggestionDtos);

        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/suggestion")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data.infoTitle").value(suggestionListDto.getInfoTitle()))
                .andExpect(jsonPath("$.data.infoDescription").value(suggestionListDto.getInfoDescription()))
                .andExpect(jsonPath("$.data.suggestions[0].suggestionId").value(suggestionListDto.getSuggestions().get(0).getSuggestionId()))
                .andExpect(jsonPath("$.data.suggestions[0].title").value(suggestionListDto.getSuggestions().get(0).getTitle()))
                .andExpect(jsonPath("$.data.suggestions[0].description").value(suggestionListDto.getSuggestions().get(0).getDescription()))
                .andExpect(jsonPath("$.data.suggestions[0].date").value(suggestionListDto.getSuggestions().get(0).getDate().toString()))
                .andExpect(jsonPath("$.data.suggestions[0].likeNum").value(suggestionListDto.getSuggestions().get(0).getLikeNum()))
                .andExpect(jsonPath("$.data.suggestions[0].isLiked").value(suggestionListDto.getSuggestions().get(0).getIsLiked()))
                .andExpect(jsonPath("$.data.suggestions[0].isMyPost").value(suggestionListDto.getSuggestions().get(0).getIsMyPost()));

        // documentation
        actions.andDo(document(
                "get-suggestion-list",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .summary("[제안] 제안하기 리스트 조회")
                                .tag("제안하기")
                                .description(
                                        CustomMDGenerator.builder()
                                                .h1("[Descriptions]")
                                                .h3("제안하기 리스트를 조회합니다.")
                                                .h1("[Request Headers]")
                                                .table(
                                                        tableHead("Request values", "Data Type", "Description"),
                                                        tableRow("Authorization", "String", "액세스 토큰")
                                                )
                                                .h1("[Response Fields]")
                                                .table(
                                                        tableHead("Request values", "Data Type", "Description"),
                                                        tableRow("success", "Boolean", "성공 여부"),
                                                        tableRow("responseCode", "Number", "응답 코드"),
                                                        tableRow("message", "String", "응답 메시지"),
                                                        tableRow("data.infoTitle", "String", "화면 상단 타이틀"),
                                                        tableRow("data.infoDescription", "String", "화면 상단 메시지"),
                                                        tableRow("data.suggestions[].suggestionId", "Number", "게시글 ID"),
                                                        tableRow("data.suggestions[].title", "String", "게시글 제목"),
                                                        tableRow("data.suggestions[].description", "String", "게시글 내용"),
                                                        tableRow("data.suggestions[].date", "String", "게시글 작성일"),
                                                        tableRow("data.suggestions[].likeNum", "String", "게시글 좋아요 수, 1000개 이상일 경우 999+로 표시"),
                                                        tableRow("data.suggestions[].isLiked", "Boolean", "해당 회원이 게시글 좋아요를 눌렀는지 여부"),
                                                        tableRow("data.suggestions[].isMyPost", "Boolean", "해당 회원이 게시글 작성자인지 여부")
                                                )
                                                .build()
                                )
                                .requestHeaders(headerWithName("Authorization").description("Bearer Token"))
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data.infoTitle").type(JsonFieldType.STRING).description("제안 리스트 상단 타이틀"),
                                        fieldWithPath("data.infoDescription").type(JsonFieldType.STRING).description("제안 리스트 상단 설명"),
                                        fieldWithPath("data.suggestions").type(JsonFieldType.ARRAY).description("제안 리스트"),
                                        fieldWithPath("data.suggestions[].suggestionId").type(JsonFieldType.NUMBER).description("제안 ID"),
                                        fieldWithPath("data.suggestions[].title").type(JsonFieldType.STRING).description("제안 제목"),
                                        fieldWithPath("data.suggestions[].description").type(JsonFieldType.STRING).description("제안 내용"),
                                        fieldWithPath("data.suggestions[].date").type(JsonFieldType.STRING).description("제안 작성일"),
                                        fieldWithPath("data.suggestions[].likeNum").type(JsonFieldType.STRING).description("제안 좋아요 수"),
                                        fieldWithPath("data.suggestions[].isLiked").type(JsonFieldType.BOOLEAN).description("제안 좋아요 여부"),
                                        fieldWithPath("data.suggestions[].isMyPost").type(JsonFieldType.BOOLEAN).description("제안 내가 작성 여부")
                                )
                                .build())));
    }

    @DisplayName("제안 상세 조회")
    @Test
    void getSuggestionTest() throws Exception {
        // given
        Member junior = getMockJunior();
        String token = getMockToken(junior);
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(junior));
        SuggestionDto suggestionDto = suggestionDtos.get(0);
        given(suggestionService.getSuggestion(any(), any())).willReturn(suggestionDto);

        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/suggestion/{suggestionId}", suggestionDto.getSuggestionId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data.suggestionId").value(suggestionDto.getSuggestionId()))
                .andExpect(jsonPath("$.data.title").value(suggestionDto.getTitle()))
                .andExpect(jsonPath("$.data.description").value(suggestionDto.getDescription()))
                .andExpect(jsonPath("$.data.date").value(suggestionDto.getDate().toString()))
                .andExpect(jsonPath("$.data.likeNum").value(suggestionDto.getLikeNum()))
                .andExpect(jsonPath("$.data.isLiked").value(suggestionDto.getIsLiked()))
                .andExpect(jsonPath("$.data.isMyPost").value(suggestionDto.getIsMyPost()));


        // documentation
        actions.andDo(document(
                "get-suggestion-detail",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .summary("[제안] 제안하기 상세 조회")
                                .tag("제안하기")
                                .description(
                                        CustomMDGenerator.builder()
                                                .h1("[Descriptions]")
                                                .h3("제안하기 게시글을 조회합니다.")
                                                .h1("[Request Headers]")
                                                .table(
                                                        tableHead("Request values", "Data Type", "Description"),
                                                        tableRow("Authorization", "String", "액세스 토큰")
                                                )
                                                .h1("[Response Fields]")
                                                .table(
                                                        tableHead("Request values", "Data Type", "Description"),
                                                        tableRow("success", "Boolean", "성공 여부"),
                                                        tableRow("responseCode", "Number", "응답 코드"),
                                                        tableRow("message", "String", "응답 메시지"),
                                                        tableRow("data.suggestionId", "Number", "게시글 ID"),
                                                        tableRow("data.title", "String", "게시글 제목"),
                                                        tableRow("data.description", "String", "게시글 내용"),
                                                        tableRow("data.date", "String", "게시글 작성일"),
                                                        tableRow("data.likeNum", "String", "게시글 좋아요 수, 1000개 이상일 경우 999+로 표시"),
                                                        tableRow("data.isLiked", "Boolean", "해당 회원이 게시글 좋아요를 눌렀는지 여부"),
                                                        tableRow("data.isMyPost", "Boolean", "해당 회원이 게시글 작성자인지 여부")
                                                )
                                                .h1("[Errors]")
                                                .table(
                                                        tableHead("HTTP Status", "Response Code", "Message"),
                                                        tableRow(
                                                                ResponseCode.NOT_EXIST_SUGGESTION.getHttpStatus().toString(),
                                                                ResponseCode.NOT_EXIST_SUGGESTION.getCode().toString(),
                                                                ResponseCode.NOT_EXIST_SUGGESTION.getMessage()
                                                        )
                                                )
                                                .build()
                                )
                                .requestHeaders(headerWithName("Authorization").description("Bearer Token"))
                                .pathParameters(parameterWithName("suggestionId").description("제안 ID"))
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data.suggestionId").type(JsonFieldType.NUMBER).description("제안 ID"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("제안 제목"),
                                        fieldWithPath("data.description").type(JsonFieldType.STRING).description("제안 내용"),
                                        fieldWithPath("data.date").type(JsonFieldType.STRING).description("제안 작성일"),
                                        fieldWithPath("data.likeNum").type(JsonFieldType.STRING).description("제안 좋아요 수"),
                                        fieldWithPath("data.isLiked").type(JsonFieldType.BOOLEAN).description("제안 좋아요 여부"),
                                        fieldWithPath("data.isMyPost").type(JsonFieldType.BOOLEAN).description("제안 내가 작성 여부")
                                )
                                .build())));
    }

    @DisplayName("제안 상세 조회 예외 테스트 - 존재하지 않는 게시글")
    @Test
    void getSuggestionTestFail_NotExist() throws Exception {
        // given
        Member junior = getMockJunior();
        String token = getMockToken(junior);
        given(memberRepository.findOneByGithubId(any())).willReturn(Optional.ofNullable(junior));
        given(suggestionService.getSuggestion(any(), any())).willThrow(new NotExistSuggestion());

        // when
        // then
        ResponseCode EXPECTED_RESPONSE_CODE = ResponseCode.NOT_EXIST_SUGGESTION;
        ResultActions actions = mockMvc.perform(get("/v1/suggestion/{suggestionId}", suggestionDtos.get(0).getSuggestionId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(EXPECTED_RESPONSE_CODE.getCode()))
                .andExpect(jsonPath("$.message").value(EXPECTED_RESPONSE_CODE.getMessage()));

        // documentation
        actions.andDo(document(
                "get-suggestion-detail-fail-not-exist-suggestion",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
        ));
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