package com.github.naraks.myappserver.dto.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnsweredQuestionDTO {

    private String id;
    private List<SessionQuestionAnswerDTO> answersList;

}
