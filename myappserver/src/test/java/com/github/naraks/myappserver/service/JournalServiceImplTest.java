package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.entity.Journal;
import com.github.naraks.myappserver.repository.JournalRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JournalServiceImplTest {

    @Autowired
    private JournalService journalService;

    @Autowired
    private JournalRepository journalRepository;

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
}


