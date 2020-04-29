package com.github.naraks.myappserver.controller;

import com.github.naraks.myappserver.dto.question.QuestionItemDTO;
import com.github.naraks.myappserver.service.QuestionService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/question")
public class QuestionRestController {

    private final QuestionService questionService;

    public QuestionRestController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("create")
    public QuestionItemDTO create(@RequestBody QuestionItemDTO questionItemDTO) {
        return questionService.save(questionItemDTO);
    }

    @PutMapping("edit")
    public QuestionItemDTO edit(@RequestBody QuestionItemDTO questionItemDTO) {
        return questionService.edit(questionItemDTO);
    }

}

