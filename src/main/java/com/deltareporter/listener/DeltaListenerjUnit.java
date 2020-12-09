package com.deltareporter.listener;

import com.deltareporter.listener.adapter.impl.SuiteAdapterImpl;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
 
public class DeltaListenerjUnit extends RunListener
{

    private final TestLifecycleAware listener = new DeltaEventRegistrar();
    /**
     * Called before any tests have been run.
     * */
    public void testRunStarted(Description description) throws java.lang.Exception
    {
      System.out.println("#### TEST RUN STARTED ####");
      System.out.println(description);
        System.out.println("Number of tests to execute : " + description.testCount());
      SuiteAdapterImpl suiteAdapterImpl = new SuiteAdapterImpl(description);
      this.listener.onTestRunStarted(suiteAdapterImpl);
    }
 
    /**
     *  Called when all tests have finished
     * */
    public void testRunFinished(Result result) throws java.lang.Exception
    {
      System.out.println("#### TEST RUN FINISHED ####");
      System.out.println(result);
        System.out.println("Number of tests executed : " + result.getRunCount());
    }
 
    /**
     *  Called when an atomic test is about to be started.
     * */
    public void testStarted(Description description) throws java.lang.Exception
    {
      System.out.println("#### TEST STARTED ####");
      // System.out.println("Starting execution of test case : "+ description.getD;
      System.out.println(description);
        System.out.println("Starting execution of test case : "+ description.getMethodName());
    }
 
    /**
     *  Called when an atomic test has finished, whether the test succeeds or fails.
     * */
    public void testFinished(Description description) throws java.lang.Exception
    {
      System.out.println("#### TEST FINISHED ####");
      System.out.println(description);
        System.out.println("Finished execution of test case : "+ description.getMethodName());
    }
 
    /**
     *  Called when an atomic test fails.
     * */
    public void testFailure(Failure failure) throws java.lang.Exception
    {
      System.out.println("#### TEST FAILURE ####");
      System.out.println(failure);
        System.out.println("Execution of test case failed : "+ failure.getMessage());
    }
 
    /**
     *  Called when a test will not be run, generally because a test method is annotated with Ignore.
     * */
    public void testIgnored(Description description) throws java.lang.Exception
    {
      System.out.println("#### TEST IGNORED ####");
      System.out.println(description);
        System.out.println("Execution of test case ignored : "+ description.getMethodName());
    }
}