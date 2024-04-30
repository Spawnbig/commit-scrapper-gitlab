package com.scrapper.commits.controllers;


import com.scrapper.commits.dtos.RequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ScrapperTestSpring {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Test Spring @Autowired Integration")
    void testScrapper() throws Exception {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setUrl("https://gitlab.com/n.sanhueza05/airsense-temuco/-/commits/9b3073dcdf876a4ac4c64afc97484f293cce6e2b");
        requestDTO.setAllCommits(true);

        mvc.perform(post("/scrap")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"url\": \"https://gitlab.com/n.sanhueza05/airsense-temuco/-/commits/9b3073dcdf876a4ac4c64afc97484f293cce6e2b\",\n" +
                        "\t\"allCommits\": false\n" +
                        "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }
}
