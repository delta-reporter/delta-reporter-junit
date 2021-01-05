package com.deltareporter.client;

import com.deltareporter.models.*;
import com.deltareporter.util.http.HttpClient;
import org.apache.commons.lang3.StringUtils;

public class BasicClient {

  private final String serviceURL;
  private String project = "UNKNOWN";

  public BasicClient(String serviceURL) {
    this.serviceURL = serviceURL;
  }

  public synchronized HttpClient.Response<TestSuiteHistoryType> finishTestSuiteHistory(
      TestSuiteHistoryType testHistory) {
    return HttpClient.uri(Path.TEST_SUITE_HISTORY_PATH, this.serviceURL, new Object[0])
        .onFailure("Unable to update test suite history")
        .put(TestSuiteHistoryType.class, testHistory);
  }

  public synchronized HttpClient.Response<TestRunType> finishTestRun(TestRunType testRun) {
    return HttpClient.uri(Path.TEST_RUNS_PATH, this.serviceURL, new Object[0])
        .onFailure("Unable to finish test run")
        .put(TestRunType.class, testRun);
  }

  public synchronized HttpClient.Response<LaunchType> finishLaunch(LaunchType launch) {
    return HttpClient.uri(Path.LAUNCH_FINISH, this.serviceURL, new Object[0])
        .onFailure("Unable to finish launch")
        .put(LaunchType.class, launch);
  }

  public synchronized HttpClient.Response<TestCaseType> createTestCase(TestCaseType testCase) {
    return HttpClient.uri(Path.TEST_CASE_HISTORY_PATH, this.serviceURL, new Object[0])
        .onFailure("Unable to create test case")
        .post(TestCaseType.class, testCase);
  }

  public String getProject() {
    return this.project;
  }

  public HttpClient.Response<ProjectType> getProjectByName(String name) {
    return HttpClient.uri(Path.PROJECTS_PATH, this.serviceURL, new Object[] {name})
        .onFailure("Unable to get project by name")
        .get(ProjectType.class);
  }

  public BasicClient initProject(String project) {
    if (!StringUtils.isEmpty(project)) {
      HttpClient.Response<ProjectType> rs = getProjectByName(project);
      if (rs.getStatus() == 200) {
        this.project = ((ProjectType) rs.getObject()).getName();
      }
    }
    return this;
  }

  public String getServiceUrl() {
    return this.serviceURL;
  }
}
