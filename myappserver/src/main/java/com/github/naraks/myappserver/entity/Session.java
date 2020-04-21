package com.github.naraks.myappserver.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Session {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fio")
    private String fullName;

    @Column
    private Double percent;

}
