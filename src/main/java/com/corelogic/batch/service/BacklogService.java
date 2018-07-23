package com.corelogic.batch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corelogic.batch.model.Backlog;
import com.corelogic.batch.repository.BacklogRepository;

@Service
public class BacklogService {

  @Autowired
  private BacklogRepository backlogRepository;

  public Backlog save(Backlog backlog) {
    return backlogRepository.save(backlog);
  }

}
