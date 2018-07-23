package com.corelogic.batch.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corelogic.batch.model.Backlog;
import com.corelogic.batch.service.BacklogService;

@Component
public class BacklogWriter implements ItemWriter<Backlog> {

  private static final Logger LOGGER = LoggerFactory.getLogger(BacklogWriter.class);

  @Autowired
  private BacklogService backlogService;

  private int index = 0;

  @Override
  public void write(List<? extends Backlog> items) throws Exception {
    LOGGER.info("Received the information of {} backlogs", items.size());

    items.forEach(backlog -> {
      if (!isEmpty(backlog)) {
        backlogService.save(backlog);
        LOGGER.info("Saved the information of a backlog: {}", backlog);
      } else {
        LOGGER.info("Omitted empty row " + index++);
      }
    });
  }

  private boolean isEmpty(Backlog backlog) {
    return (backlog.getSummary() == null || backlog.getSummary().isEmpty())
        && (backlog.getEpic() == null || backlog.getEpic().isEmpty())
        && (backlog.getBacklog() == null || backlog.getBacklog().isEmpty())
        && (backlog.getSubTask() == null || backlog.getSubTask().isEmpty())
        && (backlog.getStatus() == null || backlog.getStatus().isEmpty());
  }
}
