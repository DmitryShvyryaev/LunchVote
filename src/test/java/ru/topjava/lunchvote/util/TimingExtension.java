package ru.topjava.lunchvote.util;

import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

public class TimingExtension implements BeforeAllCallback, AfterAllCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final Logger log = LoggerFactory.getLogger("result");

    private StopWatch stopWatch;

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        stopWatch = new StopWatch("Timing of execution of " + extensionContext.getRequiredTestClass().getSimpleName());
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        log.info("\n" + stopWatch.prettyPrint() + "\n");
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        String taskName = extensionContext.getDisplayName();
        log.info("\nStart " + taskName);
        stopWatch.start(taskName);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        stopWatch.stop();
    }
}
