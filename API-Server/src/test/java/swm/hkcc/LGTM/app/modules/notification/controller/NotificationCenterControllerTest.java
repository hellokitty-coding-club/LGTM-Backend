package swm.hkcc.LGTM.app.modules.notification.controller;

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
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.notification.dto.NotificationDTO;
import swm.hkcc.LGTM.app.modules.notification.service.NotificationCenterService;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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
public class NotificationCenterControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private TokenProvider tokenProvider;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private NotificationCenterService notificationCenterService;

    private Member mockMember;
    LocalDateTime now = LocalDateTime.now();

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
    @DisplayName("알림 센터 목록 조회")
    public void getNotification() throws Exception {
        // given
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(Optional.ofNullable(getMockMember()));
        List<NotificationDTO> response =
                List.of(
                        NotificationDTO.builder()
                                .notificationId(1L)
                                .title("테스트")
                                .body("테스트")
                                .isRead(true)
                                .createdAt(now)
                                .build(),

                        NotificationDTO.builder()
                                .notificationId(2L)
                                .title("테스트")
                                .body("테스트")
                                .isRead(false)
                                .createdAt(now)
                                .build()
                );
        given(notificationCenterService.getNotification(Mockito.any(Member.class))).willReturn(response);
        // when
        // then
        ResultActions actions = mockMvc.perform(get("/v1/notification")
                        .header("Authorization", "Bearer " + getMockToken(getMockMember()))
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"));

        // documentation
        actions.andDo(document("get-notification",// 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
                resource(
                        ResourceSnippetParameters.builder()
                                .summary("[알림 센터] 알림 센터 목록 조회")
                                .tag("알림 센터")
                                .description(CustomMDGenerator.builder()
                                        .h1("[Descriptions]")
                                        .h3("알림 센터 목록 조회")
                                        .h1("[Request Headers]")
                                        .table(
                                                tableHead("Request values", "Data Type", "Description"),
                                                tableRow("Authorization", "String", "액세스 토큰")
                                        )
                                        .line()
                                        .h1("[Response Fields]")
                                        .table(
                                                tableHead("Request values", "Data Type", "Description"),
                                                tableRow("success", "Boolean", "성공여부"),
                                                tableRow("responseCode", "Number", "응답코드"),
                                                tableRow("message", "String", "메시지"),
                                                tableRow("data", "Array", "알림 센터 목록"),
                                                tableRow("data[].notificationId", "Number", "알림 id"),
                                                tableRow("data[].title", "String", "알림 제목"),
                                                tableRow("data[].body", "String", "알림 내용"),
                                                tableRow("data[].isRead", "Boolean", "읽음 여부"),
                                                tableRow("data[].createdAt", "String", "알림 생성 시간")
                                        )
                                        .line()
                                        .h1("[Errors]")
                                        .table(
                                                tableHead("HTTP Status", "Response Code", "Message"),
                                                tableRow("HTTP Status", "Response Code", "Message")
                                        ).build())
                                .requestHeaders(headerWithName("Authorization").description("access token"))
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("알림 센터 목록"),
                                        fieldWithPath("data[].notificationId").type(JsonFieldType.NUMBER).description("알림 id"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("알림 제목"),
                                        fieldWithPath("data[].body").type(JsonFieldType.STRING).description("알림 내용"),
                                        fieldWithPath("data[].isRead").type(JsonFieldType.BOOLEAN).description("읽음 여부"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("알림 생성 시간")
                                )
                                .build())));
    }

    private Member getMockMember() {
        if (mockMember == null) {
            mockMember = Member.builder()
                    .memberId(1L)
                    .githubId("member")
                    .build();
            mockMember.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        }
        return mockMember;
    }

    private String getMockToken(Member member) {
        return tokenProvider.createToken(member.getGithubId(), TokenType.ACCESS_TOKEN);
    }

}
