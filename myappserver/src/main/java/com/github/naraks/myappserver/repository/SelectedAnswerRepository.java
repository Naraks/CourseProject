package com.github.naraks.myappserver.repository;

import com.github.naraks.myappserver.entity.SelectedAnswer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SelectedAnswerRepository extends CrudRepository<SelectedAnswer, Long> {
    List<SelectedAnswer> findAll();
}
