package com.github.naraks.myappserver.dto.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionQuestionAnswerDTO {

    private String id;
    private Boolean isSelected;
}
