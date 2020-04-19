package com.github.naraks.myappserver.dto.question;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.naraks.myappserver.entity.Answer;
import com.github.naraks.myappserver.transfer.UserDetails;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerItemDTO {

    @JsonView({UserDetails.class})
    private String id;

    @JsonView({UserDetails.class})
    private String answerText;

    private Boolean isCorrect;

    public AnswerItemDTO(Answer answer){
        this.id = answer.getId().toString();
        this.answerText = answer.getName();
        this.isCorrect = answer.getIsCorrect();
    }
}
