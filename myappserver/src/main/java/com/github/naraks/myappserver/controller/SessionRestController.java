package com.github.naraks.myappserver.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.github.naraks.myappserver.dto.question.QuestionItemDTO;
import com.github.naraks.myappserver.dto.session.SessionRequestDTO;
import com.github.naraks.myappserver.entity.Session;
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

    @JsonView({UserDetails.class})
    @GetMapping("questions-new")
    public List<QuestionItemDTO> getQuestions(){
        return sessionService.getQuestions();
    }

    @PostMapping()
    public String create(@RequestBody SessionRequestDTO sessionRequestDTO) {
        Session session = sessionService.save(sessionRequestDTO);
        return session.getPercent().toString();
    }

}


