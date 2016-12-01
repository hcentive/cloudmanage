package com.hcentive.cloudmanage.junit;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JunitTestExecutionListener extends RunListener{

    private static final Logger logger = LoggerFactory
            .getLogger(JunitTestExecutionListener.class.getName());

    public void testFailure(Failure failure) throws java.lang.Exception{
        logger.error("Test case failed : "+ failure.getMessage() + " in test class : " + failure.getClass());
    }
}
