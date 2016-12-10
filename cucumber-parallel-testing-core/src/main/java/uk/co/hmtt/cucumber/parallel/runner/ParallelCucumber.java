package uk.co.hmtt.cucumber.parallel.runner;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.junit.FeatureRunner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.hmtt.cucumber.parallel.model.FeatureFileRecorder;
import uk.co.hmtt.cucumber.parallel.system.SynchronisedFile;

import java.io.IOException;

public class ParallelCucumber extends Cucumber {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelCucumber.class);

    private SynchronisedFile synchronisedFile = new SynchronisedFile();

    public ParallelCucumber(Class clazz) throws InitializationError, IOException {
        super(clazz);
    }

    @Override
    protected void runChild(FeatureRunner child, RunNotifier notifier) {
        if (synchronisedFile.hasNotBeenRun(FeatureFileRecorder.class, child.getName())) {
            LOGGER.debug("Feature file not already processed");
            child.run(notifier);
        }
    }

}
