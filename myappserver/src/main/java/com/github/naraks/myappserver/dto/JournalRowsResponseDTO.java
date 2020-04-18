package com.github.naraks.myappserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JournalRowsResponseDTO {

    private Integer total;
    private List<? extends JournalItemDTO> items;
}
