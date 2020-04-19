package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.journal.JournalRowsRequestDTO;
import com.github.naraks.myappserver.dto.journal.JournalRowsResponseDTO;
import com.github.naraks.myappserver.dto.question.QuestionItemDTO;
import com.github.naraks.myappserver.dto.session.SessionItemDTO;
import com.github.naraks.myappserver.entity.Journal;
import com.github.naraks.myappserver.repository.JournalRepository;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JournalServiceImpl implements JournalService {

    public static final String QUESTIONS_JOURNAL_ID = "questions";
    public static final String SESSIONS_JOURNAL_ID = "sessions";

    private final JournalRepository journalRepository;
    private final QuestionService questionService;
    private final SessionService sessionService;

    public JournalServiceImpl(JournalRepository journalRepository, QuestionService questionService, SessionService sessionService) {
        this.journalRepository = journalRepository;
        this.questionService = questionService;
        this.sessionService = sessionService;
    }

    @Override
    public Journal getJournal(String id) {
        return journalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Journal with id %s not found", id)));
    }

    @Override
    public JournalRowsResponseDTO getJournalRows(String id, JournalRowsRequestDTO request) {
        switch (id) {
            case JournalServiceImpl.QUESTIONS_JOURNAL_ID:
                List<QuestionItemDTO> questions = questionService.getQuestions(id, request);
                return new JournalRowsResponseDTO(questionService.countQuestions(request), questions);

            case JournalServiceImpl.SESSIONS_JOURNAL_ID:
                List<SessionItemDTO> sessions = sessionService.getSessions(id, request);
                return new JournalRowsResponseDTO(sessionService.countSessions(request), sessions);

            default:
                throw new RuntimeException(String.format("Journal with id %s not exist", id));
        }
    }
}
