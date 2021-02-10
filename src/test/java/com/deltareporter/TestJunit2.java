package com.deltareporter;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertTrue;

public class TestJunit2 {

    @Test
    public void suite2test1Pass() {
        System.out.println("Inside suite2Test1()");
        assertTrue(true);
    }

    @Ignore
    @Test
    public void suite2test2Ignore() throws InterruptedException {
        System.out.println("Inside suite2Test2()");
        Thread.sleep(1500);
        assertTrue(true);
    }
}
