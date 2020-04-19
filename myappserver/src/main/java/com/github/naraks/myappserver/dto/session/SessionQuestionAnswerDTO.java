package com.github.naraks.myappserver.dto.session;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionQuestionAnswerDTO {

    private String id;
    private Boolean isSelected;
}
