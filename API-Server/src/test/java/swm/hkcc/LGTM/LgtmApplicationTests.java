package swm.hkcc.LGTM;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import swm.hkcc.LGTM.app.global.api.IntroController;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(IntroController.class)
class LgtmApplicationTests {

    private MockMvc mockMvc;

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
        ResultActions perform = mockMvc.perform(get("/api/v1/intro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data.minVersion").value(100))
                .andExpect(jsonPath("$.data.latestVersion").value(100));

        // document
        perform
                .andDo(print())                                          // 요청/응답을 콘솔에 출력
                .andDo(document("get-intro",                    // 문서의 고유 id
                                preprocessRequest(modifyUris()           // 문서의 request 출력 설정
                                                .scheme("https")
                                                .host("www.lgtm.com")
                                                .removePort(),
                                        prettyPrint()),                  // request JSON 정렬하여 출력
                                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                                responseFields(                          // 문서의 응답 필드
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터"),
                                        fieldWithPath("data.minVersion").type(JsonFieldType.NUMBER).description("최소버전"),
                                        fieldWithPath("data.latestVersion").type(JsonFieldType.NUMBER).description("최신버전")
                                )
                        )
                );
    }

}
