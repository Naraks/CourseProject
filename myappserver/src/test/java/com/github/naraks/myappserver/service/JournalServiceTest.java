package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.journal.JournalRowsRequestDTO;
import com.github.naraks.myappserver.entity.Journal;
import com.github.naraks.myappserver.entity.Question;
import com.github.naraks.myappserver.entity.Session;
import com.github.naraks.myappserver.repository.JournalRepository;
import com.github.naraks.myappserver.repository.QuestionRepository;
import com.github.naraks.myappserver.repository.SessionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JournalServiceTest {

    @Autowired
    private JournalService journalService;

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeAll
    private void init(){
        Journal questionsJournal = new Journal();
        questionsJournal.setId(JournalServiceImpl.QUESTIONS_JOURNAL_ID);
        questionsJournal.setName("Вопросы");
        questionsJournal.setDefaultPageSize(5);
        journalRepository.save(questionsJournal);
    }

    @Test
    void getJournalTest() {
        assertEquals("Вопросы", journalService.getJournal(JournalServiceImpl.QUESTIONS_JOURNAL_ID).getName());
    }

    @Test
    void getNullJournalTest() {
        String id = "Unknown";

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> journalService.getJournal(id));

        assertEquals(exception.getMessage(), String.format("Journal with id %s not found", id));
    }

    @Test
    void getJournalNullRowsTest(){
        String id = "Unknown";

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> journalService.getJournal(id));

        assertEquals(exception.getMessage(), String.format("Journal with id %s not found", id));
    }

    @Test
    void getQuestionJournalRowsTest(){
        JournalRowsRequestDTO requestDTO = new JournalRowsRequestDTO();
        requestDTO.setJournalId(JournalServiceImpl.QUESTIONS_JOURNAL_ID);
        requestDTO.setSearch("");
        requestDTO.setFilters(new ArrayList<>());
        requestDTO.setPage(1);
        requestDTO.setPageSize(2);

        Question question1 = new Question();
        question1.setName("q1");
        questionRepository.save(question1);

        Question question2 = new Question();
        question2.setName("q2");
        questionRepository.save(question2);

        Question question3 = new Question();
        question3.setName("q3");
        questionRepository.save(question3);

        assertEquals(2, journalService.getJournalRows(JournalServiceImpl.QUESTIONS_JOURNAL_ID, requestDTO).getItems().size());
    }

    @Test
    void getSessionJournalRowsTest(){
        JournalRowsRequestDTO requestDTO = new JournalRowsRequestDTO();
        requestDTO.setJournalId(JournalServiceImpl.SESSIONS_JOURNAL_ID);
        requestDTO.setSearch("");
        requestDTO.setFilters(new ArrayList<>());
        requestDTO.setPage(1);
        requestDTO.setPageSize(2);

        Session session = new Session();
        session.setFullName("1");
        session.setPercent(50d);
        sessionRepository.save(session);

        Session session1 = new Session();
        session1.setFullName("2");
        session1.setPercent(50d);
        sessionRepository.save(session1);

        Session session2 = new Session();
        session2.setFullName("3");
        session2.setPercent(50d);
        sessionRepository.save(session2);

        assertEquals(2, journalService.getJournalRows(JournalServiceImpl.SESSIONS_JOURNAL_ID, requestDTO).getItems().size());
    }
}
