package com.deltareporter;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class TestJunit1 {
    String message = "Robert";
    MessageUtil messageUtil = new MessageUtil(message);

    @Test
    public void test1() {
        System.out.println("Inside test1()");
        assertEquals(message, messageUtil.printMessage());
    }

    @Test
    public void test2() {
        System.out.println("Inside test2()");
        assertEquals(message, "xzz");
    }

    @Ignore
    @Test
    public void test3() {
        System.out.println("Inside test3()");
        assertEquals(message, messageUtil.printMessage());
    }
}
