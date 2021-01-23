package com.deltareporter.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestCaseType extends AbstractType {
  @NotNull private String name;
  @NotNull private String start_datetime;
  @NotNull private Integer test_suite_id;
  @NotNull private Integer test_run_id;
  @NotNull private Integer test_suite_history_id;
  private Integer test_id;
  private Integer test_history_id;
  private boolean needRerun;
  private String test_status;
  private String end_datetime;
  private String trace;
  private String file;
  private String message;
  private String error_type;
  private String parameters;
  private Integer retries;

  public TestCaseType(String name, String datetime, String parameters, Integer test_suite_id, Integer test_run_id, Integer test_suite_history_id) {
    this.name = name;
    this.start_datetime = datetime;
    this.parameters = parameters;
    this.test_suite_id = test_suite_id;
    this.test_run_id = test_run_id;
    this.test_suite_history_id = test_suite_history_id;
  }
}
