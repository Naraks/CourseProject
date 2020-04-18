package com.github.naraks.myappserver.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class JournalFilterItem {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String type;

    @Column
    private String code;

    private String value;
}
