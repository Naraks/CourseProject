package com.github.naraks.myappserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.naraks.myappserver.dto.question.QuestionItemDTO;
import com.github.naraks.myappserver.dto.session.AnsweredQuestionDTO;
import com.github.naraks.myappserver.dto.session.SessionRequestDTO;
import com.github.naraks.myappserver.service.QuestionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
class SessionRestControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    QuestionService questionService;

    MockMvc mockMvc;

    @BeforeAll
    public void setup(){
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .dispatchOptions(true)
                .build();

        QuestionItemDTO question = new QuestionItemDTO();
        question.setName("Test question №1");
        question.setAnswers(new ArrayList<>());
        questionService.save(question);
    }

    @Test
    void getQuestions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/session/questions-new")
                .accept(MediaType.ALL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test question №1")));
    }

    @Test
    void create() throws Exception {
        String url = "api/session";

        SessionRequestDTO sessionRequestDTO = new SessionRequestDTO("Ivan Ivanov", new ArrayList<AnsweredQuestionDTO>());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(sessionRequestDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}