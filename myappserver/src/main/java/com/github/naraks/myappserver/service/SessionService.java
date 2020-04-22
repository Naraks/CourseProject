package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.journal.JournalRowsRequestDTO;
import com.github.naraks.myappserver.dto.question.QuestionItemDTO;
import com.github.naraks.myappserver.dto.session.SessionItemDTO;
import com.github.naraks.myappserver.dto.session.SessionRequestDTO;
import com.github.naraks.myappserver.entity.Session;

import java.util.List;

public interface SessionService {

    List<SessionItemDTO> getSessions(JournalRowsRequestDTO request);

    Integer countSessions(JournalRowsRequestDTO request);

    List<QuestionItemDTO> getQuestionsForSession();

    Session save(SessionRequestDTO sessionRequestDTO);

    Double calculateTotalPercent(SessionRequestDTO sessionRequestDTO);
}
