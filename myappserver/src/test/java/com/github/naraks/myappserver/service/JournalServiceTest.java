package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.journal.JournalRowsRequestDTO;
import com.github.naraks.myappserver.entity.Journal;
import com.github.naraks.myappserver.repository.JournalRepository;
import com.github.naraks.myappserver.repository.QuestionRepository;
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
        requestDTO.setPage(0);
        requestDTO.setPageSize(5);

        
    }
}


//    @Override
//    public JournalRowsResponseDTO getJournalRows(String id, JournalRowsRequestDTO request) {
//        switch (id) {
//            case JournalServiceImpl.QUESTIONS_JOURNAL_ID:
//                List<QuestionItemDTO> questions = questionService.getPagingAndFilteredQuestions(request);
//                return new JournalRowsResponseDTO(questionService.countFilteredQuestions(request), questions);
//
//            case JournalServiceImpl.SESSIONS_JOURNAL_ID:
//                List<SessionItemDTO> sessions = sessionService.getSessions(request);
//                return new JournalRowsResponseDTO(sessionService.countSessions(request), sessions);
//
//        }
//    }