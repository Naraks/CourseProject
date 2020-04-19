package com.github.naraks.myappserver.dto.journal;

import com.github.naraks.myappserver.dto.journal.JournalItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JournalRowsResponseDTO {

    private Integer total;
    private List<? extends JournalItemDTO> items;
}
