package com.corelogic.batch.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.corelogic.batch.model.Backlog;

public class BacklogProcessor implements ItemProcessor<Backlog, Backlog> {

  private static final Logger LOGGER = LoggerFactory.getLogger(BacklogProcessor.class);

  @Override
  public Backlog process(Backlog item) throws Exception {
    LOGGER.info("Processing backlog information: {}", item);
    return item;
  }
}
