package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.journal.JournalRowsRequestDTO;
import com.github.naraks.myappserver.dto.journal.JournalRowsResponseDTO;
import com.github.naraks.myappserver.entity.Journal;

public interface JournalService {

    Journal getJournal(String id);

    JournalRowsResponseDTO getJournalRows(String id, JournalRowsRequestDTO request);
}
