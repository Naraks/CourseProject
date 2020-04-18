package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.JournalRowsRequestDTO;
import com.github.naraks.myappserver.dto.JournalRowsResponseDTO;
import com.github.naraks.myappserver.entity.Journal;

public interface JournalService {

    Journal getJournal(String id);

    JournalRowsResponseDTO getJournalRows(String id, JournalRowsRequestDTO request);
}
