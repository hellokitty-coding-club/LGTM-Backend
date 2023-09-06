package swm.hkcc.LGTM.app.modules.member.controller;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.member.service.MemberService;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.util.Collections;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
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
class UpdateDeviceTokenControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private TokenProvider tokenProvider;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        // mock user authentication
        Member member = (Member.builder()
                .memberId(1L)
                .build());
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                customUserDetails, "", customUserDetails.getAuthorities()
                        ));
    }

    @Test
    @DisplayName("디바이스 토큰 업데이트 동작 테스트")
    void updateDeviceToken() throws Exception {

        // given
        Member member = (Member.builder()
                .memberId(1L)
                .githubId("testGithubId")
                .build());
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        String mockToken = getMockToken(member);

        Mockito.when(memberService.updateDeviceToken(Mockito.anyLong(), Mockito.any())).thenReturn(true);
        Mockito.when(memberRepository.findOneByGithubId(Mockito.anyString()))
                .thenReturn(java.util.Optional.ofNullable(member));

        // when

        // then
        ResultActions actions = mockMvc.perform(patch("/v1/member/device-token")
                        .header("Authorization", "Bearer " + mockToken)
                        .queryParam("deviceToken", "testDeviceToken")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data").value(true));

        // document
        actions
                .andDo(document("update-device-token",
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .summary("[멤버] 디바이스 토큰 업데이트")
                                .description(
                                        CustomMDGenerator.builder()
                                                .h1("[Descriptions]")
                                                .h3("디바이스 토큰 업데이트")
                                                .h1("[Request Parameters]")
                                                .table(
                                                        tableHead("Name", "Type", "Description"),
                                                        tableRow("deviceToken", "String", "디바이스 토큰")
                                                )
                                                .br()
                                                .ul("device token이 추출이 안되는 기기일 경우 null값을 허용한다")
                                                .ul("null로 수정이 된 경우에도, 정상 device token이 보내질때와 동일하게 200번 code가 도착한다")
                                                .ul("빈문자열이나 공백이 보내져도 200OK 발생하므로 클라이언트에서 1차 검증 필요")
                                                .br()
                                                .h1("[Errors]")
                                                .table(
                                                        tableHead("HTTP Status", "Response Code", "Message"),
                                                        tableRow(
                                                                ResponseCode.NOT_EXIST_MEMBER.getHttpStatus().toString(),
                                                                ResponseCode.NOT_EXIST_MEMBER.getCode().toString(),
                                                                ResponseCode.NOT_EXIST_MEMBER.getMessage())
                                                )
                                                .build()
                                )
                                .tag("Member")
                                .queryParameters(
                                        parameterWithName("deviceToken").optional().description("디바이스 토큰")
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("access token")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("디바이스 토큰 업데이트 성공 여부")
                                )
                                .build()
                        )));
    }


    @Test
    @DisplayName("디바이스 토큰 업데이트 실패 테스트 - 존재하지 않는 회원")
    void createMissionNotExistMember() throws Exception {
        // given
        Member member = (Member.builder()
                .memberId(1L)
                .githubId("testGithubId")
                .build());
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

        String memberAccessToken = getMockToken(member);

        Mockito.when(memberService.updateDeviceToken(Mockito.anyLong(), Mockito.any()))
                .thenThrow(new NotExistMember());
        Mockito.when(memberRepository.findOneByGithubId(Mockito.anyString()))
                .thenReturn(java.util.Optional.ofNullable(member));

        // when

        // then
        ResponseCode expectedResponseCode = ResponseCode.NOT_EXIST_MEMBER;
        ResultActions actions = mockMvc.perform(patch("/v1/member/device-token")
                        .header("Authorization", "Bearer " + memberAccessToken)
                        .queryParam("deviceToken", "testDeviceToken")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions
                .andDo(document("update-device-token-not-exist-member",
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")

                                ).build())
                ));
    }

    private String getMockToken(Member member) {
        return tokenProvider.createToken(member.getGithubId(), TokenType.ACCESS_TOKEN);
    }
}
