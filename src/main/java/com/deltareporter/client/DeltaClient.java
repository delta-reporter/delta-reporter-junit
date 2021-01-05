package com.deltareporter.client;

import com.deltareporter.models.*;
import com.deltareporter.util.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeltaClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeltaClient.class);

  private final BasicClient basicClient;
  private final ExtendedClient extendedClient;

  private final String serviceURL;


  public DeltaClient(String serviceUrl) {
    this.basicClient = new BasicClient(serviceUrl);
    this.extendedClient = new ExtendedClient(this.basicClient);
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

  public synchronized HttpClient.Response<TestSuiteHistoryType> createTestSuiteHistory(
      TestSuiteHistoryType testHistory) {
    return HttpClient.uri(Path.TEST_SUITE_HISTORY_PATH, this.serviceURL)
        .onFailure("Unable to create test suite history")
        .post(TestSuiteHistoryType.class, testHistory);
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

  public HttpClient.Response<TestSuiteHistoryType> finishTestSuiteHistory(
      TestSuiteHistoryType testSuiteHistory) {
    return this.basicClient.finishTestSuiteHistory(testSuiteHistory);
  }

  public HttpClient.Response<TestRunType> finishTestRun(TestRunType testRun) {
    return this.basicClient.finishTestRun(testRun);
  }

  public HttpClient.Response<LaunchType> finishLaunch(LaunchType testLaunch) {
    return this.basicClient.finishLaunch(testLaunch);
  }



  public HttpClient.Response<ProjectType> getProjectByName(String name) {
    return this.basicClient.getProjectByName(name);
  }

  public String getProject() {
    return this.basicClient.getProject();
  }

  public BasicClient initProject(String project) {
    return this.basicClient.initProject(project);
  }

  public String getServiceUrl() {
    return this.serviceURL;
  }

  public void finishTestRun(Integer test_run_id, String end_datetime, String test_run_status) {
    this.extendedClient.finishTestRun(test_run_id, end_datetime, test_run_status);
  }

  public void finishLaunch(Integer launch_id) {
    this.extendedClient.finishLaunch(launch_id);
  }

  public void finishTestSuiteHistory(
      Integer test_suite_history_id, String end_datetime, String test_suite_status) {
    this.extendedClient.finishTestSuiteHistory(
        test_suite_history_id, end_datetime, test_suite_status);
  }

  public TestCaseType registerTestCase(
      String name, String datetime, String parameters, Integer test_suite_id,  Integer test_run_id, Integer test_suite_history_id) {
    return this.extendedClient.registerTestCase(name, datetime, parameters, test_suite_id, test_run_id, test_suite_history_id);
  }
}
