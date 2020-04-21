package com.github.naraks.myappserver.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class JournalFilterItem {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String type;

    @Column
    private String code;

    private String value;
}
