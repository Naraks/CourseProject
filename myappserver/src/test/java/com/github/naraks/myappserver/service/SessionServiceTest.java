package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.journal.JournalRowsRequestDTO;
import com.github.naraks.myappserver.dto.question.QuestionItemDTO;
import com.github.naraks.myappserver.dto.session.AnsweredQuestionDTO;
import com.github.naraks.myappserver.dto.session.SessionItemDTO;
import com.github.naraks.myappserver.dto.session.SessionQuestionAnswerDTO;
import com.github.naraks.myappserver.dto.session.SessionRequestDTO;
import com.github.naraks.myappserver.entity.Answer;
import com.github.naraks.myappserver.entity.Question;
import com.github.naraks.myappserver.entity.Session;
import com.github.naraks.myappserver.repository.AnswerRepository;
import com.github.naraks.myappserver.repository.QuestionRepository;
import com.github.naraks.myappserver.repository.SelectedAnswerRepository;
import com.github.naraks.myappserver.repository.SessionRepository;
import org.junit.jupiter.api.AfterEach;
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
class SessionServiceTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SelectedAnswerRepository selectedAnswerRepository;

    @Autowired
    private SessionService sessionService;

    @AfterEach
    private void clean(){
        selectedAnswerRepository.deleteAll();
        sessionRepository.deleteAll();
        answerRepository.deleteAll();
        questionRepository.deleteAll();
    }

    @Test
    void getSessions() {
        JournalRowsRequestDTO journalRowsRequestDTO = new JournalRowsRequestDTO();
        journalRowsRequestDTO.setPageSize(1);
        journalRowsRequestDTO.setPage(2);
        journalRowsRequestDTO.setSearch("test");
        journalRowsRequestDTO.setFilters(new ArrayList<>());

        Session session = new Session();
        session.setFullName("Ivan test");
        session.setPercent(50d);
        sessionRepository.save(session);

        Session session1 = new Session();
        session1.setFullName("Petr");
        session1.setPercent(50d);
        sessionRepository.save(session1);

        Session session2 = new Session();
        session2.setFullName("1test1");
        session2.setPercent(50d);
        sessionRepository.save(session2);

        assertTrue(sessionService.getSessions(journalRowsRequestDTO)
                .stream()
                .map(SessionItemDTO::getName)
                .collect(Collectors.toList())
                .contains("1test1"));
    }

    @Test
    void countSessions() {
        JournalRowsRequestDTO journalRowsRequestDTO = new JournalRowsRequestDTO();
        journalRowsRequestDTO.setPageSize(1);
        journalRowsRequestDTO.setPage(2);
        journalRowsRequestDTO.setSearch("test");
        journalRowsRequestDTO.setFilters(new ArrayList<>());

        Session session = new Session();
        session.setFullName("Ivan test");
        session.setPercent(50d);
        sessionRepository.save(session);

        Session session1 = new Session();
        session1.setFullName("Petr");
        session1.setPercent(50d);
        sessionRepository.save(session1);

        Session session2 = new Session();
        session2.setFullName("1test1");
        session2.setPercent(50d);
        sessionRepository.save(session2);

        assertEquals(1, sessionService.getSessions(journalRowsRequestDTO).size());
    }

    @Test
    void getQuestions() {
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

        Question question2 = new Question();
        question2.setName("Вопрос");
        question2 = questionRepository.save(question2);

        Answer answer3 = new Answer();
        answer3.setName("1");
        answer3.setIsCorrect(true);
        answer3.setQuestion(question2);
        answerRepository.save(answer3);

        Question question3 = new Question();
        question3.setName("Тест-Вопрос");
        question3 = questionRepository.save(question3);

        Answer answer5 = new Answer();
        answer5.setName("1");
        answer5.setIsCorrect(true);
        answer5.setQuestion(question3);
        answerRepository.save(answer5);

        assertEquals(4, sessionService.getQuestionsForSession().size());
        assertTrue(sessionService.getQuestionsForSession()
                .stream()
                .map(QuestionItemDTO::getName)
                .collect(Collectors.toList())
                .contains("Тест-Вопрос"));
    }

    @Test
    void save() {
        Question question = new Question();
        question.setName("Question");
        question = questionRepository.save(question);

        Answer answer = new Answer();
        answer.setIsCorrect(true);
        answer.setName("answer");
        answer.setQuestion(question);
        answer = answerRepository.save(answer);

        SessionRequestDTO sessionRequestDTO = new SessionRequestDTO();
        sessionRequestDTO.setName("session_name");

        SessionQuestionAnswerDTO sessionQuestionAnswerDTO = new SessionQuestionAnswerDTO();
        sessionQuestionAnswerDTO.setId(answer.getId().toString());
        sessionQuestionAnswerDTO.setIsSelected(true);
        List<SessionQuestionAnswerDTO> answers = new ArrayList<>();
        answers.add(sessionQuestionAnswerDTO);

        AnsweredQuestionDTO answeredQuestionDTO = new AnsweredQuestionDTO();
        answeredQuestionDTO.setAnswersList(answers);
        answeredQuestionDTO.setId(question.getId().toString());
        List<AnsweredQuestionDTO> questions = new ArrayList<>();
        questions.add(answeredQuestionDTO);

        sessionRequestDTO.setQuestionsList(questions);

        sessionService.save(sessionRequestDTO);

        assertEquals("session_name", sessionRepository.findAll().get(0).getFullName());
    }

    @Test
    void calculateTotalPercentCase1() {
        Question question = new Question();
        question.setName("Case 1");
        question = questionRepository.save(question);

        Answer answer = new Answer();
        answer.setName("true");
        answer.setIsCorrect(true);
        answer.setQuestion(question);
        answerRepository.save(answer);

        Answer answer1 = new Answer();
        answer1.setName("false");
        answer1.setIsCorrect(false);
        answer1.setQuestion(question);
        answerRepository.save(answer1);

        SessionRequestDTO sessionRequestDTO = new SessionRequestDTO();
        sessionRequestDTO.setName("session");

        SessionQuestionAnswerDTO sessionQuestionAnswerDTO = new SessionQuestionAnswerDTO();
        sessionQuestionAnswerDTO.setId(answer.getId().toString());
        sessionQuestionAnswerDTO.setIsSelected(true);
        List<SessionQuestionAnswerDTO> answers = new ArrayList<>();
        answers.add(sessionQuestionAnswerDTO);

        AnsweredQuestionDTO answeredQuestionDTO = new AnsweredQuestionDTO();
        answeredQuestionDTO.setAnswersList(answers);
        answeredQuestionDTO.setId(question.getId().toString());
        List<AnsweredQuestionDTO> questions = new ArrayList<>();
        questions.add(answeredQuestionDTO);

        sessionRequestDTO.setQuestionsList(questions);

        assertEquals(100d, sessionService.calculateTotalPercent(sessionRequestDTO));
    }

    @Test
    void calculateTotalPercentCase2() {
        Question question = new Question();
        question.setName("Case 2");
        question = questionRepository.save(question);

        Answer answer = new Answer();
        answer.setName("true");
        answer.setIsCorrect(true);
        answer.setQuestion(question);
        answerRepository.save(answer);

        Answer answer1 = new Answer();
        answer1.setName("false");
        answer1.setIsCorrect(false);
        answer1.setQuestion(question);
        answerRepository.save(answer1);

        SessionRequestDTO sessionRequestDTO = new SessionRequestDTO();
        sessionRequestDTO.setName("session");

        SessionQuestionAnswerDTO sessionQuestionAnswerDTO = new SessionQuestionAnswerDTO();
        sessionQuestionAnswerDTO.setId(answer1.getId().toString());
        sessionQuestionAnswerDTO.setIsSelected(true);
        List<SessionQuestionAnswerDTO> answers = new ArrayList<>();
        answers.add(sessionQuestionAnswerDTO);

        AnsweredQuestionDTO answeredQuestionDTO = new AnsweredQuestionDTO();
        answeredQuestionDTO.setAnswersList(answers);
        answeredQuestionDTO.setId(question.getId().toString());
        List<AnsweredQuestionDTO> questions = new ArrayList<>();
        questions.add(answeredQuestionDTO);

        sessionRequestDTO.setQuestionsList(questions);

        assertEquals(0d, sessionService.calculateTotalPercent(sessionRequestDTO));
    }

    @Test
    void calculateTotalPercentCase3() {
        Question question = new Question();
        question.setName("Case 3");
        question = questionRepository.save(question);

        Answer answer = new Answer();
        answer.setName("true");
        answer.setIsCorrect(true);
        answer.setQuestion(question);
        answerRepository.save(answer);

        Answer answer1 = new Answer();
        answer1.setName("false");
        answer1.setIsCorrect(false);
        answer1.setQuestion(question);
        answerRepository.save(answer1);

        SessionRequestDTO sessionRequestDTO = new SessionRequestDTO();
        sessionRequestDTO.setName("session");

        SessionQuestionAnswerDTO sessionQuestionAnswerDTO = new SessionQuestionAnswerDTO();
        sessionQuestionAnswerDTO.setId(answer1.getId().toString());
        sessionQuestionAnswerDTO.setIsSelected(true);
        SessionQuestionAnswerDTO sessionQuestionAnswerDTO1 = new SessionQuestionAnswerDTO();
        sessionQuestionAnswerDTO1.setId(answer.getId().toString());
        sessionQuestionAnswerDTO1.setIsSelected(true);
        List<SessionQuestionAnswerDTO> answers = new ArrayList<>();
        answers.add(sessionQuestionAnswerDTO);
        answers.add(sessionQuestionAnswerDTO1);

        AnsweredQuestionDTO answeredQuestionDTO = new AnsweredQuestionDTO();
        answeredQuestionDTO.setAnswersList(answers);
        answeredQuestionDTO.setId(question.getId().toString());
        List<AnsweredQuestionDTO> questions = new ArrayList<>();
        questions.add(answeredQuestionDTO);

        sessionRequestDTO.setQuestionsList(questions);

        assertEquals(0d, sessionService.calculateTotalPercent(sessionRequestDTO));
    }

    @Test
    void calculateTotalPercentCase4() {
        Question question = new Question();
        question.setName("Case 4");
        question = questionRepository.save(question);

        Answer answer = new Answer();
        answer.setName("true");
        answer.setIsCorrect(true);
        answer.setQuestion(question);
        answerRepository.save(answer);

        Answer answer1 = new Answer();
        answer1.setName("false");
        answer1.setIsCorrect(false);
        answer1.setQuestion(question);
        answerRepository.save(answer1);

        Answer answer2 = new Answer();
        answer2.setName("true");
        answer2.setIsCorrect(true);
        answer2.setQuestion(question);
        answerRepository.save(answer2);

        Answer answer3 = new Answer();
        answer3.setName("false");
        answer3.setIsCorrect(false);
        answer3.setQuestion(question);
        answerRepository.save(answer3);

        SessionRequestDTO sessionRequestDTO = new SessionRequestDTO();
        sessionRequestDTO.setName("session");

        SessionQuestionAnswerDTO sessionQuestionAnswerDTO = new SessionQuestionAnswerDTO();
        sessionQuestionAnswerDTO.setId(answer.getId().toString());
        sessionQuestionAnswerDTO.setIsSelected(true);

        SessionQuestionAnswerDTO sessionQuestionAnswerDTO1 = new SessionQuestionAnswerDTO();
        sessionQuestionAnswerDTO1.setId(answer1.getId().toString());
        sessionQuestionAnswerDTO1.setIsSelected(true);

        SessionQuestionAnswerDTO sessionQuestionAnswerDTO2 = new SessionQuestionAnswerDTO();
        sessionQuestionAnswerDTO2.setId(answer2.getId().toString());
        sessionQuestionAnswerDTO2.setIsSelected(true);

        SessionQuestionAnswerDTO sessionQuestionAnswerDTO3 = new SessionQuestionAnswerDTO();
        sessionQuestionAnswerDTO3.setId(answer3.getId().toString());
        sessionQuestionAnswerDTO3.setIsSelected(false);

        List<SessionQuestionAnswerDTO> answers = new ArrayList<>();
        answers.add(sessionQuestionAnswerDTO);
        answers.add(sessionQuestionAnswerDTO1);
        answers.add(sessionQuestionAnswerDTO2);
        answers.add(sessionQuestionAnswerDTO3);

        AnsweredQuestionDTO answeredQuestionDTO = new AnsweredQuestionDTO();
        answeredQuestionDTO.setAnswersList(answers);
        answeredQuestionDTO.setId(question.getId().toString());
        List<AnsweredQuestionDTO> questions = new ArrayList<>();
        questions.add(answeredQuestionDTO);

        sessionRequestDTO.setQuestionsList(questions);

        assertEquals(50d, sessionService.calculateTotalPercent(sessionRequestDTO));
    }
}

