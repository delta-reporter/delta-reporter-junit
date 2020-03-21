package com.deltareporter.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaunchType extends AbstractType {
  @NotNull private String name;
  @NotNull private String project;

  public void setName(String name) {
    this.name = name;
  }

  public void setProject(String project) {
    this.project = project;
  }

  public LaunchType() {}

  public String getName() {
    return this.name;
  }

  public String getProject() {
    return this.project;
  }

  public LaunchType(String name, String project) {
    this.name = name;
    this.project = project;
  }
}
