package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.question.QuestionItemDTO;
import com.github.naraks.myappserver.entity.Question;
import com.github.naraks.myappserver.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QuestionServiceTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionService questionService;

    @Test
    void save() {
        String questionName = "question_name";
        QuestionItemDTO question = new QuestionItemDTO();
        question.setName(questionName);
        questionService.save(question);

    }

//    public QuestionItemDTO save(QuestionItemDTO dto) {
//        Question question = new Question();
//        question.setName(dto.getName());
//        questionRepository.save(question);
//
//        createAnswers(dto, question);
//
//        return new QuestionItemDTO(question, answerRepository.findByQuestion(question));

    @Test
    void edit() {
    }

    @Test
    void getPagingAndFilteredQuestions() {
    }

    @Test
    void countFilteredQuestions() {
    }
}