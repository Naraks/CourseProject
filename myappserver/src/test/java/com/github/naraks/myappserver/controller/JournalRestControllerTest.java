package com.github.naraks.myappserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.naraks.myappserver.dto.journal.JournalRowsRequestDTO;
import com.github.naraks.myappserver.dto.question.QuestionItemDTO;
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

import static org.hamcrest.Matchers.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
class JournalRestControllerTest {

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

        QuestionItemDTO question1 = new QuestionItemDTO();
        question1.setName("Test question №2");
        question1.setAnswers(new ArrayList<>());
        questionService.save(question1);
    }

    @Test
    void getJournalEntityTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/journal/questions")
                .accept(MediaType.ALL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("name", is("Вопросы")));
    }

    @Test
    void getJournalRowsTest() throws Exception {
        String url = "/api/journal/questions/rows";
        JournalRowsRequestDTO requestDTO = new JournalRowsRequestDTO();
        requestDTO.setJournalId("questions");
        requestDTO.setSearch("");
        requestDTO.setFilters(new ArrayList<>());
        requestDTO.setPage(1);
        requestDTO.setPageSize(5);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(requestDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .put(url)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("total", is(2)))
                .andExpect(jsonPath("items").isArray())
                .andExpect(jsonPath("items", hasSize(2)));
    }
}

