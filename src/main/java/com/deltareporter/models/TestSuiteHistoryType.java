package com.deltareporter.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestSuiteHistoryType extends AbstractType {
  @NotNull private String name;
  @NotNull private String test_type;
  @NotNull private String start_datetime;
  @NotNull private Integer test_run_id;
  private Integer test_suite_history_id;
  private Integer test_suite_id;
  private String end_datetime;
  private String project;

  public TestSuiteHistoryType(
      String name, String test_type, String start_datetime, Integer test_run_id, String project) {
    this.name = name;
    this.test_type = test_type;
    this.start_datetime = start_datetime;
    this.test_run_id = test_run_id;
    this.project = project;
  }

  public TestSuiteHistoryType(
      Integer test_suite_history_id, String end_datetime) {
    this.test_suite_history_id = test_suite_history_id;
    this.end_datetime = end_datetime;
  }
}
