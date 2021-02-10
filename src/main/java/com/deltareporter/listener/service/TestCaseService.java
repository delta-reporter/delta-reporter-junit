package com.deltareporter.listener.service;

import com.deltareporter.client.DeltaClient;
import com.deltareporter.models.TestCaseType;
import com.deltareporter.util.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCaseService {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(TestCaseService.class);
  private final DeltaClient deltaClient;

  public TestCaseService(DeltaClient deltaClient) {
    this.deltaClient = deltaClient;
  }

  public TestCaseType register(String name, String datetime, String parameters, Integer test_suite_id, Integer test_run_id, Integer test_suite_history_id) {
    TestCaseType testCase = new TestCaseType(name, datetime, parameters, test_suite_id, test_run_id, test_suite_history_id);
    String testCaseDetails = "name: %s, datetime: %s, parameters: %s, test_suite_id: %d, test_run_id: %d, test_suite_history_id: %d";
    String formattedTestCase = String.format(
        testCaseDetails, name, datetime, parameters, test_suite_id, test_run_id,
        test_suite_history_id);
    LOGGER.info("Test Case details for registration:" + formattedTestCase);
    HttpClient.Response<TestCaseType> response = this.deltaClient.createTestCase(testCase);
    testCase = response.getObject();
    if (testCase == null) {
      throw new RuntimeException(
          "Unable to register test case '"
              + formattedTestCase
              + "' for delta service: "
              + this.deltaClient.getServiceUrl());
    }
    LOGGER.info("Registered test case details:" + formattedTestCase);

    return testCase;
  }

  public TestCaseType finishTest(TestCaseType test) {
    HttpClient.Response<TestCaseType> result = this.deltaClient.finishTest(test);
    if (result.getStatus() != 200 && result.getObject() == null) {
      throw new RuntimeException(
          "Unable to register test "
              + test.getName()
              + " for delta service: "
              + this.deltaClient.getServiceUrl());
    }
    return result.getObject();
  }
}
