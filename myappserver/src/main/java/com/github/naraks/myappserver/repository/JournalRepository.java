package com.github.naraks.myappserver.repository;

import com.github.naraks.myappserver.entity.Journal;
import org.springframework.data.repository.CrudRepository;

public interface JournalRepository extends CrudRepository<Journal, String> {
}
