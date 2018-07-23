package com.corelogic.batch.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Backlog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String summary;
  private String epic;
  private String backlog;
  private String subTask;
  private String status;

}
