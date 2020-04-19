package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.journal.JournalRowsRequestDTO;
import com.github.naraks.myappserver.dto.question.QuestionItemDTO;
import com.github.naraks.myappserver.dto.session.SessionItemDTO;
import com.github.naraks.myappserver.dto.session.SessionRequestDTO;
import com.github.naraks.myappserver.entity.Session;

import java.util.List;

public interface SessionService {

    List<SessionItemDTO> getSessions(String journalId, JournalRowsRequestDTO request);

    Integer countSessions(JournalRowsRequestDTO request);

    List<QuestionItemDTO> getQuestions();

    void save(Session session);

    Double calculate(SessionRequestDTO sessionRequestDTO);
}
