package swm.hkcc.LGTM.app.modules.admin.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;
import swm.hkcc.LGTM.app.modules.admin.service.AdminService;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class AdminDeleteMemberTest {

    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

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
    @DisplayName("유저 삭제")
    void deleteMember() throws Exception {
        // given

        // when
        Mockito.when(adminService.deleteMember(1L)).thenReturn(true);

        // then
        ResultActions actions = mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/admin/member/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"));

        // document
        actions
                .andDo(document("admin-delete-member",      // 문서의 고유 id
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .description("유저 삭제")
                                .summary("유저 삭제")
                                .tag("admin")
                                .pathParameters(
                                        parameterWithName("id").type(SimpleType.NUMBER).description("유저 id")
                                )
                                .responseFields(
                                        fieldWithPath("data").type(SimpleType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("success").type(SimpleType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(SimpleType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(SimpleType.STRING).description("응답 메시지")
                                )
                                .build()
                        )
                ));
    }


    @Test
    @DisplayName("유저 삭제 예외 테스트 - 존재하지 않는 사용자")
    void deleteMemberNotFound() throws Exception {
        // given

        // when
        Mockito.when(adminService.deleteMember(1L))
                .thenThrow(new GeneralException("해당 유저가 없습니다. id=1", new IllegalArgumentException("해당 유저가 없습니다. id=1")));

        // then
        ResponseCode expectedResponseCode = ResponseCode.INTERNAL_ERROR;
        ResultActions actions = mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/admin/member/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value("해당 유저가 없습니다. id=1"));

        // document
        actions
                .andDo(document("admin-delete-member-not-found"));    // 문서의 고유 id
    }


    @Test
    @DisplayName("유저 삭제 예외 테스트 - 삭제할 수 없는 사용자")
    void deleteMemberNotValid() throws Exception {
        // given

        // when
        Mockito.when(adminService.deleteMember(1L))
                .thenThrow(new GeneralException("유저의 활동 기록이 있어 삭제할 수 없는 유저입니다."));

        // then
        ResponseCode expectedResponseCode = ResponseCode.INTERNAL_ERROR;
        ResultActions actions = mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/admin/member/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value("유저의 활동 기록이 있어 삭제할 수 없는 유저입니다."));

        // document
        actions
                .andDo(document("admin-delete-member-invalid"));    // 문서의 고유 id
    }
}