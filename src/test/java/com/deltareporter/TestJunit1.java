package com.deltareporter;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class TestJunit1 {

    @Test
    public void suite1test1Pass() throws InterruptedException {
        System.out.println("Inside suite1Test1()");
        Thread.sleep(2000);
        assertTrue(true);
    }

    @Test
    public void suite1test2Fail() {
        System.out.println("Inside suite1test2()");
        assertTrue("wtf",false);
    }
}
