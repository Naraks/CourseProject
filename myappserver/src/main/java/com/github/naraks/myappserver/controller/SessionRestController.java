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

    @PostMapping("create")
    public String create(@RequestBody SessionRequestDTO sessionRequestDTO) {
        double percent = sessionService.calculate(sessionRequestDTO);
        Session session = new Session();
        session.setFullName(sessionRequestDTO.getName());
        session.setPercent(percent);
        sessionService.save(session);
        return Double.toString(percent);
    }

    @JsonView({UserDetails.class})
    @GetMapping("questions-new")
    public List<QuestionItemDTO> getQuestions(){
        return sessionService.getQuestions();
    }
}


