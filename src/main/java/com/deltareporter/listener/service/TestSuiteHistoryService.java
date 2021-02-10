package com.deltareporter.listener.service;

import com.deltareporter.client.DeltaClient;
import com.deltareporter.models.TestSuiteHistoryType;
import com.deltareporter.util.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSuiteHistoryService {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(TestSuiteHistoryService.class);

  private final DeltaClient deltaClient;

  public TestSuiteHistoryService(DeltaClient deltaClient) {
    this.deltaClient = deltaClient;
  }

  public TestSuiteHistoryType register(
      String name, String test_type, String start_datetime, Integer test_run_id, String project) {
    TestSuiteHistoryType test_suite_history =
        new TestSuiteHistoryType(name, test_type, start_datetime, test_run_id, project);
    String suiteHistoryDetails = "Name: %s, Test Type: %s, DateTime: %s, TestRunId: %s, Project: %s";
    LOGGER.info(
        "Test Suite History details to create:"
            + String.format(
            suiteHistoryDetails, name, test_type, start_datetime, test_run_id, project));

    HttpClient.Response<TestSuiteHistoryType> response =
        this.deltaClient.createTestSuiteHistory(test_suite_history);
    test_suite_history = response.getObject();

    if (test_suite_history == null) {
      throw new RuntimeException(
          "Unable to create test suite history for delta service: " + this.deltaClient.getServiceUrl());
    }
    LOGGER.info(
        "Test Suite History created! Test Suite History ID:"
            + test_suite_history.getTest_suite_history_id());

    return test_suite_history;
  }

  public void finish(Integer test_suite_history_id, String end_datetime) {
    TestSuiteHistoryType test_suite_history =
        new TestSuiteHistoryType(test_suite_history_id, end_datetime);
    String suiteHistoryDetails = "ID: %d, End Datetime: %s";
    LOGGER.debug(
        "Test Suite History details to update:"
            + String.format(
            suiteHistoryDetails,
            test_suite_history_id, end_datetime));

    this.deltaClient.finishTestSuiteHistory(test_suite_history);
  }
}
