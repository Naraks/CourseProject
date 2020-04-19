package com.github.naraks.myappserver.controller;

import com.github.naraks.myappserver.dto.journal.JournalEntityDTO;
import com.github.naraks.myappserver.dto.journal.JournalRowsRequestDTO;
import com.github.naraks.myappserver.dto.journal.JournalRowsResponseDTO;
import com.github.naraks.myappserver.service.JournalService;
import com.github.naraks.myappserver.service.QuestionService;
import com.github.naraks.myappserver.service.SessionService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/journal")
public class JournalRestController {

    private final JournalService journalService;

    public JournalRestController(JournalService journalService, QuestionService questionService, SessionService sessionService) {
        this.journalService = journalService;
    }

    @GetMapping("{id}")
    public JournalEntityDTO getJournalEntity(@PathVariable String id){
        return new JournalEntityDTO(journalService.getJournal(id));
    }

    @PutMapping("{id}/rows")
    public JournalRowsResponseDTO getJournalRows(@PathVariable String id, @RequestBody JournalRowsRequestDTO request){
        return journalService.getJournalRows(id, request);
    }

}
