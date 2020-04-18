package com.github.naraks.myappserver.repository;

import com.github.naraks.myappserver.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface QuestionRepository extends
        CrudRepository<Question, Long>,
        PagingAndSortingRepository<Question, Long> {
    List<Question> getByNameContainingIgnoreCase(String search, Pageable pageable);
    List<Question> getByNameContainingIgnoreCase(String search);
    List<Question> findAll();
}
