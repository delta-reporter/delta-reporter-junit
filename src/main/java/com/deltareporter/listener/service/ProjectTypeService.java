package com.deltareporter.listener.service;

import com.deltareporter.client.DeltaClient;

public class ProjectTypeService {
  private final DeltaClient deltaClient;

  public ProjectTypeService(DeltaClient deltaClient) {
    this.deltaClient = deltaClient;
  }

  public void initProject(String projectName) {
    this.deltaClient.initProject(projectName);
  }
}
