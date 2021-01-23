package com.deltareporter.listener;

import com.deltareporter.client.DeltaSingleton;
import com.deltareporter.client.DeltaClient;
import com.deltareporter.listener.service.LaunchService;
import com.deltareporter.listener.service.TestCaseService;
import com.deltareporter.listener.service.TestRunService;
import com.deltareporter.listener.service.TestSuiteHistoryService;
import com.deltareporter.models.TestCaseType;
import com.deltareporter.models.TestSuiteHistoryType;
import com.deltareporter.util.ConfigurationUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeltaListener extends RunListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeltaListener.class);

  private boolean DELTA_ENABLED;
  private boolean GENERATED_LAUNCH;
  private String DELTA_TEST_TYPE;
  private String DELTA_PROJECT;
  private String DELTA_SERVICE_URL;
  private Integer DELTA_LAUNCH_ID;
  private Integer DELTA_TEST_RUN_ID;

  private static ThreadLocal<TestCaseType> threadTest = new ThreadLocal<>();
  private Map<String, TestCaseType> registeredTests = new HashMap<>();
  private Map<String, TestSuiteHistoryType> registeredSuites = new HashMap<>();

  private TestRunService testRunService;
  private TestSuiteHistoryService testSuiteHistoryService;
  private LaunchService launchService;
  private TestCaseService testCaseService;
  private TestSuiteHistoryType suiteHistory;

  @Override
  public void testRunStarted(Description description) {
    boolean initialized = initializeDelta();
    if (!initialized) { return; }

    try {
      this.DELTA_LAUNCH_ID = NumberUtils.createInteger(System.getenv("DELTA_LAUNCH_ID"));

      if (this.DELTA_LAUNCH_ID == null) {
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("E dd-MM-yyyy hh:mm:ss a");
        String name = this.DELTA_TEST_TYPE + " | " + ft.format(date);
        this.DELTA_LAUNCH_ID = this.launchService.register(name, this.DELTA_PROJECT);
        this.GENERATED_LAUNCH = true;
      }

      if (this.DELTA_PROJECT != null) {
        LOGGER.info("Test type for this run: " + this.DELTA_TEST_TYPE);
      }

      String datetime = new Date().toString();
      this.DELTA_TEST_RUN_ID =
          this.testRunService.register(this.DELTA_TEST_TYPE, this.DELTA_LAUNCH_ID, datetime);

    } catch (Throwable e) {
      this.DELTA_ENABLED = false;
      LOGGER.error("Undefined error during test run registration!", e);
    }
  }

  @Override
  public void testRunFinished(Result result){
    if (!this.DELTA_ENABLED) {
      return;
    }
    try {
      String end_datetime = new Date().toString();
      String test_run_result = "Passed";
      if (!result.wasSuccessful())
        test_run_result = "Failed";
      this.testRunService.finish(
          this.DELTA_TEST_RUN_ID, end_datetime, test_run_result);
      if (this.GENERATED_LAUNCH){
        this.launchService.finish(this.DELTA_LAUNCH_ID);
      }
    } catch (Throwable e) {
      LOGGER.error("Undefined error during test run finish!", e);
    }
  }

  @Override
  public void testSuiteStarted(Description description){
    //Do nothing
  }

  @Override
  public void testSuiteFinished(Description description){
    if (!this.DELTA_ENABLED) {
      return;
    }
    try {
      String end_datetime = new Date().toString();
      this.testSuiteHistoryService.finish(
          this.suiteHistory.getTest_suite_history_id(), end_datetime);
    } catch (Throwable e) {
      LOGGER.error("Undefined error during test suite finish!", e);
    }
  }

  @Override
  public void testStarted(Description description) {
    final String className = description.getClassName();
    final String methodName = description.getMethodName();

    if (!this.DELTA_ENABLED) {
      return;
    }

    try {
      if (!this.registeredSuites.containsKey(className)) {
        String datetime = new Date().toString();
        this.suiteHistory = this.testSuiteHistoryService.register(
            className, this.DELTA_TEST_TYPE, datetime, this.DELTA_TEST_RUN_ID, this.DELTA_PROJECT);
        this.registeredSuites.put(className, suiteHistory);
      }

      TestCaseType startedTest = null;
      String testDateTime = new Date().toString();
      TestCaseType testCase = this.testCaseService.register(
          methodName, testDateTime, null, this.suiteHistory.getTest_suite_id(),
          this.DELTA_TEST_RUN_ID, this.suiteHistory.getTest_suite_history_id());

      if (this.registeredTests.containsKey(methodName)) {
        startedTest = this.registeredTests.get(methodName);
        startedTest.setTest_history_id(testCase.getTest_history_id());
        startedTest.setEnd_datetime(null);
        startedTest.setStart_datetime(new Date().toString());
      }

      if (startedTest == null) {
        startedTest = testCase;
      }

      threadTest.set(startedTest);
      this.registeredTests.put(methodName, startedTest);
    } catch (Throwable e) {
//      if (adapter.getSkipExceptionInstance(null).getClass().isAssignableFrom(e.getClass())) {
//        throw e;
//      }
      LOGGER.error("Undefined error during test case/method start!", e);
    }
  }

  @Override
  public void testFinished(Description description) {
    if (!this.DELTA_ENABLED) {
      return;
    }
    try {
      long threadId = Thread.currentThread().getId();
      TestCaseType test = threadTest.get();
      String testName = description.getMethodName();
      if (test == null) {
        throw new RuntimeException(
            "Unable to find TestType result to mark test as finished! name: '"
                + testName
                + "'; threadId: "
                + threadId);
      }

      if (test.getTest_status() != null && (test.getTest_status().equals("Failed") || test.getTest_status().equals("Skipped"))) {
        LOGGER.info("Test already marked as finished");
      } else {
        TestCaseType finishedTest = populateTestResult(description);
        threadTest.remove();
        this.testCaseService.finishTest(finishedTest);
      }

    } catch (Throwable e) {
      LOGGER.error("Undefined error during test case/method finish!", e);
    }
  }

  @Override
  public void testFailure(Failure failure) {
    if (!this.DELTA_ENABLED) {
      return;
    }
    try {
      TestCaseType finishedTest = populateTestResultOnFailure(failure, "Failed");
      this.testCaseService.finishTest(finishedTest);
    } catch (Throwable e) {
      LOGGER.error("Undefined error during test case/method finish!", e);
    }
  }

  @Override
  public void testIgnored(Description description) {
    final String className = description.getClassName();
    final String methodName = description.getMethodName();

    if (!this.DELTA_ENABLED) {
      return;
    }

    try {
      if (!this.registeredSuites.containsKey(className)) {
        String datetime = new Date().toString();
        this.suiteHistory = this.testSuiteHistoryService.register(
            className, this.DELTA_TEST_TYPE, datetime, this.DELTA_TEST_RUN_ID, this.DELTA_PROJECT);
        this.registeredSuites.put(className, suiteHistory);
      }

      TestCaseType startedTest = null;
      String testDateTime = new Date().toString();
      TestCaseType testCase = this.testCaseService.register(
          methodName, testDateTime, null, this.suiteHistory.getTest_suite_id(),
          this.DELTA_TEST_RUN_ID, this.suiteHistory.getTest_suite_history_id());

      if (this.registeredTests.containsKey(methodName)) {
        startedTest = this.registeredTests.get(methodName);
        startedTest.setTest_history_id(testCase.getTest_history_id());
        startedTest.setEnd_datetime(null);
        startedTest.setStart_datetime(new Date().toString());
      }

      if (startedTest == null) {
        startedTest = testCase;
      }

      threadTest.set(startedTest);
      this.registeredTests.put(methodName, startedTest);

      TestCaseType finishedTest = populateTestResultOnSkip(description);
      this.testCaseService.finishTest(finishedTest);
    } catch (Throwable e) {
      LOGGER.error("Undefined error during test case/method finish!", e);
    }
  }

  @Override
  public void testAssumptionFailure(Failure failure) {
    //Do nothing
  }

  private boolean initializeDelta() {
    try {

      this.DELTA_ENABLED = Boolean.parseBoolean(ConfigurationUtil.getConfigurationFile().getProperty("delta_enabled"));
      this.DELTA_TEST_TYPE = ConfigurationUtil.getConfigurationFile().getProperty("delta_test_type");
      this.DELTA_PROJECT = ConfigurationUtil.getConfigurationFile().getProperty("delta_project");
      this.DELTA_SERVICE_URL = ConfigurationUtil.getConfigurationFile().getProperty("delta_service_url");

      if (this.DELTA_ENABLED) {
        DeltaClient dc = DeltaSingleton.INSTANCE.getClient();
        if (dc != null) {
          this.DELTA_ENABLED = dc.isAvailable();
          this.launchService = new LaunchService(dc);
          this.testRunService = new TestRunService(dc);
          this.testSuiteHistoryService = new TestSuiteHistoryService(dc);
          this.testCaseService = new TestCaseService(dc);
        }
        LOGGER.info("Delta Reporter is " + (this.DELTA_ENABLED ? "available" : "unavailable"));
      }

    } catch (NoSuchElementException | IOException e) {
      LOGGER.error("Unable to find config property: ", e);
    }

    return this.DELTA_ENABLED;
  }

  private TestCaseType populateTestResult(Description description) {
    long threadId = Thread.currentThread().getId();
    TestCaseType test = threadTest.get();
    String finishTime = new Date().toString();

    String testName = description.getMethodName();
    LOGGER.info("testName registered with current thread is: " + testName);

    if (test == null) {
      throw new RuntimeException(
          "Unable to find TestType result to mark test as finished! name: '"
              + testName
              + "'; threadId: "
              + threadId);
    }

    test.setTest_status("Passed");
    test.setEnd_datetime(finishTime);
    test.setTrace(null);
    test.setMessage(null);
    threadTest.remove();
    return test;
  }

  private TestCaseType populateTestResultOnSkip(Description description) {
    long threadId = Thread.currentThread().getId();
    TestCaseType test = threadTest.get();
    String finishTime = new Date().toString();

    String testName = description.getMethodName();
    LOGGER.info("testName registered with current thread is: " + testName);

    if (test == null) {
      throw new RuntimeException(
          "Unable to find TestType result to mark test as finished! name: '"
              + testName
              + "'; threadId: "
              + threadId);
    }

    test.setTest_status("Skipped");
    test.setEnd_datetime(finishTime);
    test.setTrace(null);
    test.setMessage(null);
    threadTest.remove();
    return test;
  }

  private TestCaseType populateTestResultOnFailure(Failure failure, String status) {
    long threadId = Thread.currentThread().getId();
    TestCaseType test = threadTest.get();
    String finishTime = new Date().toString();
    StringBuilder sb = new StringBuilder();
    String trace;
    String message;

    if (failure.getException() == null) {
      trace = null;
      message = null;
    } else {
      message = failure.toString();
      for (StackTraceElement elem : failure.getException().getStackTrace()) {
        sb.append("\n").append(elem.toString());
      }
    }
    trace = !StringUtils.isEmpty(sb.toString()) ? sb.toString() : null;
    String testName = failure.getDescription().getMethodName();
    LOGGER.debug("testName registered with current thread is: " + testName);

    if (test == null) {
      throw new RuntimeException(
          "Unable to find TestType result to mark test as finished! name: '"
              + testName
              + "'; threadId: "
              + threadId);
    }

    test.setTest_status(status);
    test.setTrace(trace);
    test.setMessage(message);
    test.setEnd_datetime(finishTime);

    //threadTest.remove();
    return test;
  }

}