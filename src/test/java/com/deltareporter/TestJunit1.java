package com.deltareporter;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestJunit1 {
    String message = "Robert";
    MessageUtil messageUtil = new MessageUtil(message);

    @Test
    public void suite1test1Pass() {
        System.out.println("Inside suite1Test1()");
        assertTrue(true);
    }

    @Test
    public void suite1test2Fail() {
        System.out.println("Inside suite1test2()");
        assertTrue("wtf",false);
    }
}
