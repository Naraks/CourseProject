package com.github.naraks.myappserver.dto.session;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SessionRequestDTO {

    private String name;
    private List<AnsweredQuestionDTO> questionsList;
}
