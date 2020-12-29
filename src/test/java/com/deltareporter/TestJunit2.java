package com.deltareporter;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertTrue;

public class TestJunit2 {
    String message = "Robert";
    MessageUtil messageUtil = new MessageUtil(message);

    @Test
    public void suite2test1Pass() {
        System.out.println("Inside suite2Test1()");
        assertTrue(true);
    }

    @Ignore
    @Test
    public void suite2test2Ignore() {
        System.out.println("Inside suite2Test2()");
        assertTrue(true);
    }
}
