package swm.hkcc.LGTM.app.modules.suggestion.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import swm.hkcc.LGTM.app.modules.suggestion.dto.CreateSuggestionRequest;
import swm.hkcc.LGTM.app.modules.suggestion.dto.CreateSuggestionResponse;
import swm.hkcc.LGTM.app.modules.suggestion.service.SuggestionService;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.util.Collections;
import java.util.Optional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
class CreateSuggestionControllerTest {
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
    @DisplayName("미션 제안하기 생성 동작 테스트")
    void createSuggestion() throws Exception {
        // given
        Member junior = getMockJunior();
        String token = getMockToken(junior);
        CreateSuggestionRequest createSuggestionRequest = CreateSuggestionRequest.builder()
                .title("title")
                .description("description")
                .build();
        CreateSuggestionResponse createSuggestionResponse = CreateSuggestionResponse.builder()
                .suggestionId(1L)
                .writerId(junior.getMemberId())
                .build();
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(junior));
        given(suggestionService.createSuggestion(Mockito.any(), Mockito.any())).willReturn(createSuggestionResponse);

        // when
        // then
        ResultActions actions = mockMvc.perform(post("/v1/suggestion")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                new ObjectMapper()
                                        .registerModule(new JavaTimeModule())
                                        .writeValueAsString(createSuggestionRequest)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.suggestionId").value(createSuggestionResponse.getSuggestionId()))
                .andExpect(jsonPath("$.data.writerId").value(createSuggestionResponse.getWriterId()));

        // documentation
        actions.andDo(document(
                "post-create-suggestion",
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
                resource(ResourceSnippetParameters.builder()
                        .summary("[제안] 미션 제안하기 생성")
                        .tag("제안하기")
                        .description(
                                CustomMDGenerator.builder()
                                        .h1("[Descriptions]")
                                        .h3("제안하기를 생성합니다.")
                                        .h1("[Request Headers]")
                                        .table(
                                                tableHead("Request values", "Data Type", "Description"),
                                                tableRow("Authorization", "String", "액세스 토큰")
                                        )
                                        .h1("[Request values]")
                                        .table(
                                                tableHead("Request values", "Data Type", "Description"),
                                                tableRow("title", "String", "제목, 최대 길이는 100자이며, null이거나 100자 초과 시 예외가 반환됩니다."),
                                                tableRow("description", "String", "설명, 최대 길이는 1000자이며, null이거나 1000자 초과 시 예외가 반환됩니다.")
                                        )
                                        .h1("[Response values]")
                                        .table(
                                                tableHead("Response values", "Data Type", "Description"),
                                                tableRow("success", "Boolean", "성공 여부"),
                                                tableRow("responseCode", "Number", "응답 코드"),
                                                tableRow("message", "String", "응답 메시지"),
                                                tableRow("data", "Object", "응답 데이터"),
                                                tableRow("data.suggestionId", "Number", "제안 ID"),
                                                tableRow("data.writerId", "Number", "작성자 ID")
                                        )
                                        .h1("[Errors]")
                                        .table(
                                                tableHead("HTTP Status", "Response Code", "Message"),
                                                tableRow(ResponseCode.BAD_REQUEST.getHttpStatus().toString(), ResponseCode.BAD_REQUEST.getCode().toString(), "Request Body 값 오류, 각 항목별로 메시지 다름")
                                        )
                                        .build()
                        )
                        .requestHeaders(headerWithName("Authorization").description("access token"))
                        .requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
                        )
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.suggestionId").type(JsonFieldType.NUMBER).description("제안 ID"),
                                fieldWithPath("data.writerId").type(JsonFieldType.NUMBER).description("작성자 ID")
                        )
                        .build())));
    }

//    @DisplayName("미션 제안하기 생성 실패 테스트 - 주니어가 아닌 회원")
//    @Test
//    void createSuggestionFail_NotJunior() throws Exception {
//        // given
//        Member senior = getMockSenior();
//        String token = getMockToken(senior);
//        CreateSuggestionRequest createSuggestionRequest = CreateSuggestionRequest.builder()
//                .title("title")
//                .description("description")
//                .build();
//        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(senior));
//        given(suggestionService.createSuggestion(Mockito.any(), Mockito.any())).willThrow(new NotJuniorMember());
//
//        // when
//        // then
//        ResponseCode expectedResponseCode = ResponseCode.NOT_JUNIOR_MEMBER;
//        ResultActions actions = mockMvc.perform(post("/v1/suggestion")
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(
//                                new ObjectMapper()
//                                        .registerModule(new JavaTimeModule())
//                                        .writeValueAsString(createSuggestionRequest)
//                        ))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
//                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()))
//                .andExpect(jsonPath("$.data").doesNotExist());
//
//        // documentation
//        actions.andDo(document(
//                "post-create-suggestion-fail-not-senior",
//                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
//                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
//                resource(ResourceSnippetParameters.builder()
//                        .summary("[제안] 미션 제안하기 생성 실패 - 시니어가 아닌 회원")
//                        .tag("제안하기")
//                        .description(
//                                CustomMDGenerator.builder()
//                                        .build()
//                        )
//                        .requestHeaders(headerWithName("Authorization").description("access token"))
//                        .requestFields(
//                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
//                                fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
//                        )
//                        .responseFields(
//                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
//                                fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
//                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
//                        )
//                        .build())));
//    }

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