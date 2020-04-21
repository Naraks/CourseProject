package com.github.naraks.myappserver;

import com.github.naraks.myappserver.entity.Journal;
import com.github.naraks.myappserver.repository.JournalRepository;
import com.github.naraks.myappserver.service.JournalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class MyappserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyappserverApplication.class, args);
	}

	@Autowired
	JournalRepository journalRepository;

	@PostConstruct
	private void initJournals(){
		Journal questionsJournal = new Journal();
		questionsJournal.setId(JournalServiceImpl.QUESTIONS_JOURNAL_ID);
		questionsJournal.setName("Вопросы");
		questionsJournal.setDefaultPageSize(5);
		journalRepository.save(questionsJournal);

		Journal sessionsJournal = new Journal();
		sessionsJournal.setId(JournalServiceImpl.SESSIONS_JOURNAL_ID);
		sessionsJournal.setName("Сессии");
		sessionsJournal.setDefaultPageSize(5);
		journalRepository.save(sessionsJournal);
	}
}
