package com.github.naraks.myappserver.dto.journal;

import com.github.naraks.myappserver.entity.Journal;
import lombok.Data;

@Data
public class JournalEntityDTO {

    private String id;
    private String name;
    private Integer defaultPageSize;

    public JournalEntityDTO(Journal journal) {
        this.id = journal.getId();
        this.name = journal.getName();
        this.defaultPageSize = journal.getDefaultPageSize();
    }
}
