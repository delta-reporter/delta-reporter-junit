package com.deltareporter.listener.service;

import com.deltareporter.client.DeltaClient;
import com.deltareporter.models.TestRunType;
import com.deltareporter.util.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRunService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestRunService.class);
  private final DeltaClient deltaClient;

  public TestRunService(DeltaClient deltaClient) {
    this.deltaClient = deltaClient;
  }

  public Integer register(String test_type, Integer launch_id, String datetime) {
    TestRunType test_run = new TestRunType(test_type, launch_id, datetime);
    String testRunDetails = "Test Type: %s, Launch ID: %s, DateTime: %s";
    LOGGER.info(
        "Test Run details to create:"
            + String.format(testRunDetails, test_type, launch_id, datetime));

    HttpClient.Response<TestRunType> response = this.deltaClient.createTestRun(test_run);
    Integer test_run_id = (int) response.getObject().getId();

    if (test_run_id == null) {
      throw new RuntimeException(
          "Unable to create test run for delta service: " + this.deltaClient.getServiceUrl());
    }
    LOGGER.info("Test Run created! Test Run ID:" + test_run_id);

    return test_run_id;
  }

  public void finish(Integer test_run_id, String end_datetime, String test_run_status) {
    TestRunType test_run = new TestRunType(test_run_id, end_datetime, test_run_status);
    String testRunDetails = "Test Run ID: %s, End DateTime: %s, Test Run Status: %s";
    LOGGER.info(
        "Test Run details:"
            + String.format(
            testRunDetails, test_run_id, end_datetime, test_run_status));

    this.deltaClient.finishTestRun(test_run);
  }
}
