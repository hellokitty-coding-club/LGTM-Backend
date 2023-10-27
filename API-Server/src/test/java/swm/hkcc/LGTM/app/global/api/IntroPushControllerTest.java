package swm.hkcc.LGTM.app.global.api;


import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import swm.hkcc.LGTM.app.modules.notification.service.NotificationServiceImpl;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static swm.hkcc.LGTM.utils.CustomMDGenerator.tableHead;
import static swm.hkcc.LGTM.utils.CustomMDGenerator.tableRow;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(IntroController.class)
public class IntroPushControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private NotificationServiceImpl notificationServiceImpl;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void contextLoads() throws Exception {
        // given
        // when
        // then
        ResultActions perform = mockMvc.perform(post("/v1/intro/push")
                        .param("targetMemberId", "1")
                        .contentType("application/json")
                        .content("{\n" +
                                "  \"data\": {\n" +
                                "    \"title\": \"푸시 테스트\",\n" +
                                "    \"body\": \"푸시 테스트\"\n" +
                                "  }\n" +
                                "}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"));

        // document
        perform
                .andDo(print())                                         // 요청/응답을 콘솔에 출력
                .andDo(document("get-intro-push-test",                    // 문서의 고유 id
                        preprocessRequest(prettyPrint()),               // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),              // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .tag("Admin")
                                .summary("푸시 테스트 API")
                                .description(CustomMDGenerator.builder()
                                                .h1("[Descriptions]")
                                                .h3("푸시 알림을 위한 테스트 api")
                                                .h1("[Request values]")
                                                .table(
                                                        tableHead("Request values", "Data Type", "Description"),
                                                        tableRow("isBroadcast", "String", "(Option) 전체 멤버에게 푸시를 보낼지 여부\n둘다 값이 있을 경우, broadcast가 실행된다."),
                                                        tableRow("targetMemberId", "Long", "(Option) 전송 멤버 id")
                                                )
                                                .line()
                                                .h1("[Errors]")
                                                .table(
                                                        tableHead("HTTP Status", "Response Code", "Message"),
                                                        tableRow("400", "4000", "targetMemberId 또는 isBroadcast 둘 중 하나는 필수입니다."),
                                                        tableRow("400", "10100", "존재하지 않는 회원입니다.")
                                                )
                                        .build())
                                .queryParameters(
                                        parameterWithName("isBroadcast").type(SimpleType.BOOLEAN).optional().description("전체 방송 여부"),
                                        parameterWithName("targetMemberId").type(SimpleType.NUMBER).optional().description("전송 멤버 id")
                                )
                                .requestFields(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("푸시 알림 데이터"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("푸시 알림 제목"),
                                        fieldWithPath("data.body").type(JsonFieldType.STRING).description("푸시 알림 내용")
                                )
                                .responseFields(                          // 문서의 응답 필드
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("전달받은 멤버 id. braodcast일 경우 0")
                                )
                                .build())
                ));
    }
}
