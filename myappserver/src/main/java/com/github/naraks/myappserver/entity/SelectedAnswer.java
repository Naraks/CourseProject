package com.github.naraks.myappserver.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SelectedAnswer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "answer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Answer answer;

    @JoinColumn(name = "session_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Session session;

}
