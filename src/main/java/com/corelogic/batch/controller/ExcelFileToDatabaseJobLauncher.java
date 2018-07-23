package com.corelogic.batch.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExcelFileToDatabaseJobLauncher {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExcelFileToDatabaseJobLauncher.class);

  private final Job job;
  private final JobLauncher jobLauncher;

  @Autowired
  ExcelFileToDatabaseJobLauncher(@Qualifier("excelFileToDatabaseJob") Job job, JobLauncher jobLauncher) {
    this.job = job;
    this.jobLauncher = jobLauncher;
  }

  @RequestMapping(value = "/excel")
  public BatchStatus launchXmlFileToDatabaseJob() throws JobParametersInvalidException,
                                                          JobExecutionAlreadyRunningException,
                                                          JobRestartException,
                                                          JobInstanceAlreadyCompleteException {
    LOGGER.info("Starting excelFileToDatabase job");
    JobExecution jobExecution = jobLauncher.run(job, newExecution());
    LOGGER.info("Stopping excelFileToDatabase job");

    return jobExecution.getStatus();
  }

  private JobParameters newExecution() {
    Map<String, JobParameter> parameters = new HashMap<>();

    JobParameter parameter = new JobParameter(new Date());
    parameters.put("currentTime", parameter);

    return new JobParameters(parameters);
  }

}
