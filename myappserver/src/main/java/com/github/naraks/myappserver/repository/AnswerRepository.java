package com.github.naraks.myappserver.repository;

import com.github.naraks.myappserver.entity.Answer;
import com.github.naraks.myappserver.entity.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnswerRepository extends CrudRepository<Answer, Long> {
    List<Answer> findByQuestion(Question question);
    void deleteByQuestion(Question question);
}
