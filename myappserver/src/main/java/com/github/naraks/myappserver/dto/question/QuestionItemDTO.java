package com.github.naraks.myappserver.dto.question;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.naraks.myappserver.dto.journal.JournalItemDTO;
import com.github.naraks.myappserver.entity.Answer;
import com.github.naraks.myappserver.entity.Question;
import com.github.naraks.myappserver.transfer.UserDetails;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class QuestionItemDTO extends JournalItemDTO {

    @JsonView({UserDetails.class})
    private String id;

    @JsonView({UserDetails.class})
    private String name;

    @JsonView({UserDetails.class})
    private List<AnswerItemDTO> answers;

    public QuestionItemDTO(Question question, List<Answer> answers) {
        this.id = question.getId().toString();
        this.name = question.getName();
        this.answers = answers
                .stream()
                .map(AnswerItemDTO::new)
                .collect(Collectors.toList());
    }
}

