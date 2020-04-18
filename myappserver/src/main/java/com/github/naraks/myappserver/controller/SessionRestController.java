package com.github.naraks.myappserver.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.github.naraks.myappserver.dto.QuestionItemDTO;
import com.github.naraks.myappserver.dto.SessionItemDTO;
import com.github.naraks.myappserver.dto.SessionQuestionsDTO;
import com.github.naraks.myappserver.service.SessionService;
import com.github.naraks.myappserver.transfer.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/session")
public class SessionRestController {

    private final SessionService sessionService;

    public SessionRestController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

//    @PostMapping("create")
//    public SessionItemDTO create(@RequestBody SessionItemDTO sessionItemDTO) {
//        return sessionService.save(sessionItemDTO);
//    }

    @JsonView({UserDetails.class})
    @GetMapping("questions-new")
    public List<QuestionItemDTO> getQuestions(){
        return sessionService.getQuestions();
    }
}


