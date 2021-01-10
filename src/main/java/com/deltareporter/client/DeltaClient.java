package com.deltareporter.client;

import com.deltareporter.models.*;
import com.deltareporter.util.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeltaClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeltaClient.class);

  private final String serviceURL;


  public DeltaClient(String serviceUrl) {
    this.serviceURL = serviceUrl;
  }

  public boolean isAvailable() {
    try{
      HttpClient.Response response =
          HttpClient.uri(Path.STATUS_PATH, this.serviceURL)
              .onFailure("Unable to send ping")
              .get(String.class);

      return response.getStatus() == 200;
    } catch (Exception e) {
      LOGGER.info("Cannot connect to Delta Reporter:", e);
      return false;
    }
  }

  public synchronized HttpClient.Response<LaunchType> createLaunch(LaunchType launch) {
    return HttpClient.uri(Path.LAUNCH_PATH, this.serviceURL)
        .onFailure("Unable to create launch")
        .post(LaunchType.class, launch);
  }

  public synchronized HttpClient.Response<TestRunType> createTestRun(TestRunType testRun) {
    return HttpClient.uri(Path.TEST_RUNS_PATH, this.serviceURL)
        .onFailure("Unable to create test run")
        .post(TestRunType.class, testRun);
  }

  public synchronized HttpClient.Response<TestRunType> finishTestRun(TestRunType testRun) {
    return HttpClient.uri(Path.TEST_RUNS_PATH, this.serviceURL)
        .onFailure("Unable to finish test run")
        .put(TestRunType.class, testRun);
  }

  public synchronized HttpClient.Response<TestSuiteHistoryType> createTestSuiteHistory(
      TestSuiteHistoryType testHistory) {
    return HttpClient.uri(Path.TEST_SUITE_HISTORY_PATH, this.serviceURL)
        .onFailure("Unable to create test suite history")
        .post(TestSuiteHistoryType.class, testHistory);
  }

  public synchronized HttpClient.Response<TestSuiteHistoryType> finishTestSuiteHistory(
      TestSuiteHistoryType testSuiteHistory) {
    return HttpClient.uri(Path.TEST_SUITE_HISTORY_PATH, this.serviceURL)
        .onFailure("Unable to update test suite history")
        .put(TestSuiteHistoryType.class, testSuiteHistory);
  }

  public synchronized HttpClient.Response<TestCaseType> createTestCase(TestCaseType testCase) {
    return HttpClient.uri(Path.TEST_CASE_HISTORY_PATH, this.serviceURL)
        .onFailure("Unable to create test case")
        .post(TestCaseType.class, testCase);
  }

  public HttpClient.Response<TestCaseType> finishTest(TestCaseType test) {
    return HttpClient.uri(Path.TEST_CASE_HISTORY_PATH, this.serviceURL)
        .onFailure("Unable to finish test")
        .put(TestCaseType.class, test);
  }

  public HttpClient.Response<LaunchType> finishLaunch(LaunchType testLaunch) {
    return HttpClient.uri(Path.LAUNCH_FINISH, this.serviceURL)
        .onFailure("Unable to finish launch")
        .put(LaunchType.class, testLaunch);
  }

  public String getServiceUrl() {
    return this.serviceURL;
  }


}
