package com.corelogic.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.corelogic.batch.batch.BacklogProcessor;
import com.corelogic.batch.batch.BacklogWriter;
import com.corelogic.batch.batch.CustomBacklogReader;
import com.corelogic.batch.model.Backlog;

@Configuration
@EnableBatchProcessing
public class ExcelFileToDatabaseJobConfig {

  @Value(value = "file:src/main/resources/data/*")
  public Resource[] resources;

  @Bean
  ItemReader<Backlog> customItemReader() {
    CustomBacklogReader reader = new CustomBacklogReader();
    reader.setLinesToSkip(2);
    return reader;
  }

  @Bean
  MultiResourceItemReader<Backlog> customMultiItemReader() {
    MultiResourceItemReader<Backlog> reader = new MultiResourceItemReader<>();
    reader.setResources(resources);
    reader.setDelegate((ResourceAwareItemReaderItemStream<? extends Backlog>) customItemReader());
    return reader;
  }

  @Bean
  ItemProcessor<Backlog, Backlog> excelStudentProcessor() {
    return new BacklogProcessor();
  }

  @Bean
  ItemWriter<Backlog> excelStudentWriter() {
    return new BacklogWriter();
  }

  @Bean
  Step excelFileToDatabaseStep(MultiResourceItemReader<Backlog> customMultiItemReader,
      ItemProcessor<Backlog, Backlog> excelStudentProcessor,
      ItemWriter<Backlog> excelStudentWriter, StepBuilderFactory stepBuilderFactory) {

    return stepBuilderFactory.get("excelFileToDatabaseStep")
        .<Backlog, Backlog>chunk(1)
        .reader(customMultiItemReader)
        .processor(excelStudentProcessor)
        .writer(excelStudentWriter)
        .build();
  }

  @Bean
  Job excelFileToDatabaseJob(JobBuilderFactory jobBuilderFactory, @Qualifier("excelFileToDatabaseStep") Step excelStudentStep) {
    return jobBuilderFactory.get("excelFileToDatabaseJob")
        .incrementer(new RunIdIncrementer())
        .flow(excelStudentStep)
        .end()
        .build();
  }

}

























