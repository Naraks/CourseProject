package com.github.naraks.myappserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SessionQuestionsDTO {

    List<SessionItemDTO> questions;

}
