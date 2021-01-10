package com.deltareporter.listener.service;

import com.deltareporter.client.DeltaClient;
import com.deltareporter.models.LaunchType;
import com.deltareporter.util.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LaunchService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LaunchService.class);
  private final DeltaClient client;

  public LaunchService(DeltaClient deltaClient) {
    this.client = deltaClient;
  }

  public Integer register(String name, String project) {
    LaunchType launch = new LaunchType(name, project);
    String launchDetails = "Name: %s, Project: %s";
    LOGGER.info(
        "Launch details to create:" + String.format(launchDetails, name, project));

    HttpClient.Response<LaunchType> response = this.client.createLaunch(launch);
    if (response.getStatus() != 200) {
      throw new RuntimeException(
          "Unable to create launch: '"
              + name
              + "' for delta service: "
              + this.client.getServiceUrl());
    }
    Integer launch_id = (int) response.getObject().getId();
    LOGGER.info("Launch created! Launch ID:" + launch_id);

    return launch_id;
  }

  public void finish(Integer launch_id) {
    LaunchType launch = new LaunchType(launch_id);
    String launchDetails = "Launch ID: %s";
    LOGGER.debug(
        "Launch details:"
            + String.format(
            launchDetails, launch_id));

    HttpClient.Response<LaunchType> response = this.client.finishLaunch(launch);
    LOGGER.info("Launch finished! Response: " + response);
  }
}
