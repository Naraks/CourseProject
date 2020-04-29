package com.github.naraks.myappserver.repository;

import com.github.naraks.myappserver.entity.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SessionRepository extends
        CrudRepository<Session, Long>,
        PagingAndSortingRepository<Session, Long> {
    List<Session> getByFullNameContainingIgnoreCase(String search, Pageable pageable);
    List<Session> getByFullNameContainingIgnoreCase(String search);
    List<Session> findAll();
}
