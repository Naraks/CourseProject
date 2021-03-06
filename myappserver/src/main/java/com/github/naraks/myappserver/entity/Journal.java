package com.github.naraks.myappserver.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Journal {

    @Id
    private String id;

    @Column
    private String name;

    @Column
    private Integer defaultPageSize;
}
