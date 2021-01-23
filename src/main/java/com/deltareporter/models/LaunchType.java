package com.deltareporter.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaunchType extends AbstractType {
  @NotNull private String name;
  @NotNull private String project;
  private Integer launch_id;

  public LaunchType(String name, String project) {
    this.name = name;
    this.project = project;
  }

  public LaunchType(Integer launch_id) {
    this.launch_id = launch_id;
  }
}
