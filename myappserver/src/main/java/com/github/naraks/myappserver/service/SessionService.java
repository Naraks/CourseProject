package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.JournalRowsRequestDTO;
import com.github.naraks.myappserver.dto.QuestionItemDTO;
import com.github.naraks.myappserver.dto.SessionItemDTO;

import java.util.List;

public interface SessionService {

    List<SessionItemDTO> getSessions(String journalId, JournalRowsRequestDTO request);

    Integer countSessions(JournalRowsRequestDTO request);

    List<QuestionItemDTO> getQuestions();
}
