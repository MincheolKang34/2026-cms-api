package com.malgn.content;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("로그인한 사용자는 콘텐츠를 등록할 수 있다")
    void createContent_success() throws Exception {
        String requestBody = """
                {
                  "title": "테스트 제목",
                  "description": "테스트 설명"
                }
                """;

        mockMvc.perform(post("/api/contents")
                        .with(httpBasic("user1", "user1234"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("테스트 제목"))
                .andExpect(jsonPath("$.data.createdBy").value("user1"));
    }

    @Test
    @DisplayName("페이징 목록 조회가 가능하다")
    void getContents_success() throws Exception {
        mockMvc.perform(get("/api/contents?page=0&size=10")
                        .with(httpBasic("user1", "user1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.page").value(0))
                .andExpect(jsonPath("$.data.size").value(10));
    }

    @Test
    @DisplayName("존재하는 콘텐츠 단건 조회 시 성공한다")
    void getContentById_success() throws Exception {
        mockMvc.perform(get("/api/contents/1")
                        .with(httpBasic("user1", "user1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @DisplayName("작성자는 자신의 콘텐츠를 수정할 수 있다")
    void updateContent_byOwner_success() throws Exception {
        String requestBody = """
                {
                  "title": "수정된 제목",
                  "description": "수정된 설명"
                }
                """;

        mockMvc.perform(put("/api/contents/2")
                        .with(httpBasic("user1", "user1234"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("수정된 제목"))
                .andExpect(jsonPath("$.data.lastModifiedBy").value("user1"));
    }

    @Test
    @DisplayName("관리자는 다른 사용자의 콘텐츠를 수정할 수 있다")
    void updateContent_byAdmin_success() throws Exception {
        String requestBody = """
                {
                  "title": "관리자 수정 제목",
                  "description": "관리자 수정 설명"
                }
                """;

        mockMvc.perform(put("/api/contents/2")
                        .with(httpBasic("admin", "admin1234"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.lastModifiedBy").value("admin"));
    }

    @Test
    @DisplayName("작성자가 아닌 일반 사용자는 다른 사용자의 콘텐츠를 수정할 수 없다")
    void updateContent_forbidden() throws Exception {
        String requestBody = """
                {
                  "title": "권한 없는 수정",
                  "description": "권한 없는 설명"
                }
                """;

        mockMvc.perform(put("/api/contents/1")
                        .with(httpBasic("user1", "user1234"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("관리자는 다른 사용자의 콘텐츠를 삭제할 수 있다")
    void deleteContent_byAdmin_success() throws Exception {
        mockMvc.perform(delete("/api/contents/2")
                        .with(httpBasic("admin", "admin1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("인증 없이 콘텐츠 목록 조회 시 401이 발생한다")
    void getContents_unauthorized() throws Exception {
        mockMvc.perform(get("/api/contents?page=0&size=10"))
                .andExpect(status().isUnauthorized());
    }
}