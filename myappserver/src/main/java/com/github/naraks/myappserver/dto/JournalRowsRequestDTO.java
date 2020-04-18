package com.github.naraks.myappserver.dto;

import com.github.naraks.myappserver.entity.JournalFilterItem;
import lombok.Data;

import java.util.List;

@Data
public class JournalRowsRequestDTO {

    private String journalId;
    private String search;
    private List<JournalFilterItem> filters;
    private Integer page;
    private Integer pageSize;
}
