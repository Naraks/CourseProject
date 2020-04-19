package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.journal.JournalRowsRequestDTO;
import com.github.naraks.myappserver.dto.question.QuestionItemDTO;

import java.util.List;

public interface QuestionService {

    QuestionItemDTO save(QuestionItemDTO questionItemDTO);

    QuestionItemDTO edit(QuestionItemDTO questionItemDTO);

    List<QuestionItemDTO> getQuestions(String journalId, JournalRowsRequestDTO request);

    Integer countQuestions(JournalRowsRequestDTO request);
}
