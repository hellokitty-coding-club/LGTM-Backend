package swm.hkcc.LGTM.app.modules.mission.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionStatus;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequest;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionResponse;
import swm.hkcc.LGTM.app.modules.mission.service.CreateMissionService;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class CreateMissionControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private CreateMissionService createMissionService;

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
    void createMissinon() throws Exception {
        // given
        LocalDateTime referenceDate = LocalDateTime.of(2000, 1, 1, 1, 1, 1);
        CreateMissionRequest createMissionRequest = CreateMissionRequest.builder()
                .missionRepositoryUrl("https://github.com/abcabc")
                .title("title")
                .missionStatus(MissionStatus.RECRUITING)
                .tagList(List.of("tag1", "tag2"))
                .thumbnailImageUrl("https://abc.com/aa.png")
                .description("content")
                .reomnnandTo("ReomnnandTo")
                .notReomnnandTo("notReomnnandTo")
                .registrationDueDate(referenceDate)
                .assignmentDueDate(referenceDate)
                .reviewCompletationDueDate(referenceDate)
                .price(1000)
                .maxPeopleNumber(10)
                .build();

        String token = "token";
        Mockito.when(createMissionService.createMission(null, createMissionRequest))
                .thenReturn(CreateMissionResponse.builder()
                        .missionId(1L)
                        .writerId(1L)
                        .build());

        // when
        log.info("createMissionRequest: {}", createMissionRequest);

        // then
        mockMvc.perform(post("/v1/mission")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(
                                new ObjectMapper()
                                        .registerModule(new JavaTimeModule())
                                        .writeValueAsString(createMissionRequest)
                        ))
                .andExpect(status().isOk());
        // ...

        // document

    }
}