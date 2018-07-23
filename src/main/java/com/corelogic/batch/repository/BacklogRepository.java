package com.corelogic.batch.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.corelogic.batch.model.Backlog;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long> {

}
