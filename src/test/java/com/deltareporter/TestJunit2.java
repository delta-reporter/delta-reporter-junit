package com.deltareporter;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class TestJunit2 {
    String message = "Robert";
    MessageUtil messageUtil = new MessageUtil(message);

    @Test
    public void test4() {
        System.out.println("Inside test4()");
        message = "Hi!" + "Robert";
        assertEquals(message,messageUtil.salutationMessage());
    }

    @Test
    public void test5() {
        System.out.println("Inside test5()");
        message = "Hi!" + "Robert22";
        assertEquals(message,messageUtil.salutationMessage());
    }
}
