package swm.hkcc.LGTM.app.modules.admin.controller;


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
import swm.hkcc.LGTM.app.modules.admin.service.AdminService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
public class AdminGetTokenTest {
    private MockMvc mockMvc;
    @Autowired
    private TokenProvider tokenProvider;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private AdminService adminService;
    private Member mockJunior;
    private Member mockSenior;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("유저 토큰 발급")
    void getToken() throws Exception {
        // given
        Member junior = getMockJunior();
        String token = getMockToken(junior);
        given(memberRepository.findOneByGithubId(anyString())).willReturn(Optional.ofNullable(junior));
        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(junior));
        given(adminService.getMemberToken(any())).willReturn(token);

        // when
        // then
        ResultActions actions = mockMvc.perform(get("/admin/token/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(token));

        actions.andDo(document("admin-get-token",
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
                resource(
                        ResourceSnippetParameters.builder()
                                .description("유저 토큰 발급")
                                .summary("유저 토큰 발급")
                                .tag("Admin")
                                .pathParameters(parameterWithName("id").type(SimpleType.NUMBER).description("토큰을 발급받을 유저의 id"))
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.STRING).description("발급된 토큰")
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
