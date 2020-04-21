package com.github.naraks.myappserver.repository;

import com.github.naraks.myappserver.entity.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> getByNameContainingIgnoreCase(String search);
    List<Question> findAll();
}
