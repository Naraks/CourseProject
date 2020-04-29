package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.journal.JournalRowsRequestDTO;
import com.github.naraks.myappserver.dto.question.QuestionItemDTO;
import com.github.naraks.myappserver.entity.Answer;
import com.github.naraks.myappserver.entity.JournalFilterItem;
import com.github.naraks.myappserver.entity.Question;
import com.github.naraks.myappserver.repository.AnswerRepository;
import com.github.naraks.myappserver.repository.QuestionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QuestionServiceTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionService questionService;

    @Test
    void save() {
        String questionName = "question_name";
        QuestionItemDTO questionItemDTO = new QuestionItemDTO();
        questionItemDTO.setName(questionName);
        questionItemDTO.setAnswers(new ArrayList<>());
        questionService.save(questionItemDTO);

        assertTrue(questionRepository.findAll()
                .stream()
                .map(Question::getName)
                .collect(Collectors.toList())
                .contains(questionName));
    }

    @Test
    void saveWithoutAnswers() {
        String questionName = "question_name";
        QuestionItemDTO questionItemDTO = new QuestionItemDTO();
        questionItemDTO.setName(questionName);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> questionService.save(questionItemDTO));

        assertEquals("Question must contain answers", exception.getMessage());
    }

    @Test
    void edit() {
        String questionName = "question_name";
        String questionEditedName = "question_edited_name";
        QuestionItemDTO questionItemDTO = new QuestionItemDTO();
        questionItemDTO.setName(questionName);
        questionItemDTO.setAnswers(new ArrayList<>());
        questionItemDTO = questionService.save(questionItemDTO);
        String id = questionItemDTO.getId();

        questionItemDTO.setName(questionEditedName);
        questionService.edit(questionItemDTO);

        assertEquals(questionRepository.findById(Long.parseLong(id)).get().getName(), questionEditedName);
    }

    @Test
    void getPagingAndFilteredQuestions() {
        JournalRowsRequestDTO requestDTO = new JournalRowsRequestDTO();
        JournalFilterItem filterItem = new JournalFilterItem();
        filterItem.setCode(QuestionServiceImpl.QUESTION_ANSWER_COUNT_FILTER);
        filterItem.setValue("2");
        List<JournalFilterItem> filters = new ArrayList<>();
        filters.add(filterItem);
        requestDTO.setSearch("тест");
        requestDTO.setFilters(filters);
        requestDTO.setPageSize(1);
        requestDTO.setPage(2);

        Question question = new Question();
        question.setName("Тестовый вопрос");
        question = questionRepository.save(question);

        Answer answer = new Answer();
        answer.setName("0");
        answer.setIsCorrect(true);
        answer.setQuestion(question);
        answerRepository.save(answer);

        Question question1 = new Question();
        question1.setName("Вопростест1");
        question1 = questionRepository.save(question1);

        Answer answer1 = new Answer();
        answer1.setName("1");
        answer1.setIsCorrect(true);
        answer1.setQuestion(question1);
        answerRepository.save(answer1);

        Answer answer2 = new Answer();
        answer2.setName("2");
        answer2.setIsCorrect(true);
        answer2.setQuestion(question1);
        answerRepository.save(answer2);

        Question question2 = new Question();
        question2.setName("Вопрос");
        question2 = questionRepository.save(question2);

        Answer answer3 = new Answer();
        answer3.setName("1");
        answer3.setIsCorrect(true);
        answer3.setQuestion(question2);
        answerRepository.save(answer3);

        Answer answer4 = new Answer();
        answer4.setName("2");
        answer4.setIsCorrect(true);
        answer4.setQuestion(question2);
        answerRepository.save(answer4);

        Question question3 = new Question();
        question3.setName("Тест-Вопрос");
        question3 = questionRepository.save(question3);

        Answer answer5 = new Answer();
        answer5.setName("1");
        answer5.setIsCorrect(true);
        answer5.setQuestion(question3);
        answerRepository.save(answer5);

        Answer answer6 = new Answer();
        answer6.setName("2");
        answer6.setIsCorrect(true);
        answer6.setQuestion(question3);
        answerRepository.save(answer6);

        assertEquals(1, questionService.getPagingAndFilteredQuestions(requestDTO).size());
        assertEquals("Тест-Вопрос", questionService.getPagingAndFilteredQuestions(requestDTO).get(0).getName());
    }

    @Test
    void countFilteredQuestionsBySearch() {
        JournalRowsRequestDTO requestDTO = new JournalRowsRequestDTO();
        JournalFilterItem filterItem = new JournalFilterItem();
        filterItem.setCode(QuestionServiceImpl.QUESTION_ANSWER_COUNT_FILTER);
        filterItem.setValue("2");
        List<JournalFilterItem> filters = new ArrayList<>();
        filters.add(filterItem);
        requestDTO.setFilters(filters);

        Question question = new Question();
        question.setName("Вопрос");
        question = questionRepository.save(question);

        Answer answer = new Answer();
        answer.setName("0");
        answer.setIsCorrect(true);
        answer.setQuestion(question);
        answerRepository.save(answer);

        Question question1 = new Question();
        question1.setName("Вопрос1");
        question1 = questionRepository.save(question1);

        Answer answer1 = new Answer();
        answer1.setName("1");
        answer1.setIsCorrect(true);
        answer1.setQuestion(question1);
        answerRepository.save(answer1);

        Answer answer2 = new Answer();
        answer2.setName("2");
        answer2.setIsCorrect(true);
        answer2.setQuestion(question1);
        answerRepository.save(answer2);

        assertEquals(1, questionService.countFilteredQuestions(requestDTO));
    }
}