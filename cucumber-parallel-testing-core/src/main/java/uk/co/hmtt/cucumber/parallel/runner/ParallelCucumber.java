package uk.co.hmtt.cucumber.parallel.runner;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.junit.FeatureRunner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.hmtt.cucumber.parallel.model.Recorder;
import uk.co.hmtt.cucumber.parallel.system.SynchronisedFile;

import java.io.IOException;

public class ParallelCucumber extends Cucumber {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelCucumber.class);

    private SynchronisedFile<Recorder> synchronisedFile;

    public ParallelCucumber(Class clazz) throws InitializationError, IOException {
        super(clazz);
        synchronisedFile = new SynchronisedFile<>();
    }

    @Override
    protected void runChild(FeatureRunner child, RunNotifier notifier) {

        LOGGER.debug("Acquiring lock");
        final Recorder read = synchronisedFile.read(Recorder.class);

        if (!read.getFeatures().contains(child.getName())) {
            LOGGER.debug("Feature file not already processed");
            read.getFeatures().add(child.getName());
            synchronisedFile.write(read);
            LOGGER.debug("Updated record and released lock. About to run test");
            child.run(notifier);
        } else {
            LOGGER.debug("Feature file already processed; releasing lock");
            synchronisedFile.release();
        }

    }

}
