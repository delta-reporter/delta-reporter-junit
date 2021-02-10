package com.deltareporter;

import com.deltareporter.listener.DeltaListener;
import org.junit.runner.JUnitCore;

public class TestRunner {
    public static void main(String[] args) {

        JUnitCore core= new JUnitCore();
        core.addListener(new DeltaListener());
        core.run(TestJunit1.class, TestJunit2.class);
    }
}
