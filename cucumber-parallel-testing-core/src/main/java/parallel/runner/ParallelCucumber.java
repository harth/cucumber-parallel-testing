package parallel.runner;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.junit.FeatureRunner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parallel.exceptions.ParallelException;
import parallel.model.RecorderWrapper;
import parallel.model.SynchronisedFile;

import java.io.IOException;
import java.nio.channels.FileChannel;

public class ParallelCucumber extends Cucumber {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelCucumber.class);

    FileChannel fileChannel;

    public ParallelCucumber(Class clazz) throws InitializationError, IOException {
        super(clazz);
    }

    @Override
    protected void runChild(FeatureRunner child, RunNotifier notifier) {

        final SynchronisedFile<RecorderWrapper> synchronisedFile = new SynchronisedFile<>();

        LOGGER.debug("Reading file");

        final RecorderWrapper read = synchronisedFile.read(RecorderWrapper.class);

        LOGGER.debug("Rad object size {}", read.getFeatures().size());

        LOGGER.debug("File read and locked");

        if (!read.getFeatures().contains(child.getName())) {

            LOGGER.debug("Feature file not already processed");

            read.getFeatures().add(child.getName());
            synchronisedFile.write(read);
//            synchronisedFile.release();
            child.run(notifier);
        } else {
            LOGGER.debug("Feature file already processed; releasing lock");
            synchronisedFile.release();
        }


//        try {

//            final SynchronisedFileOld synchronisedFile = new SynchronisedFileOld(RecorderWrapper.class);
////            synchronisedFile.lock();
//            final RecorderWrapper read = synchronisedFile.read();
//            synchronisedFile.write(read);
//
//
//            LOGGER.debug("Acquiring lock");
//            fileChannel.lock();
//            LOGGER.debug("Lock activated");
//            if (!recorder.getFeatures().contains(child.getName())) {
//                recorder.addFeature(child.getName());
//                unlock();
//                LOGGER.debug("Released lock before running test");
//                child.run(notifier);
//            } else {
//                LOGGER.debug("Released lock with no test to run");
//                unlock();
//            }
//        } catch (IOException e) {
//            unlock();
//        }
    }

    private void unlock() {
        try {
            fileChannel.close();
        } catch (IOException e) {
            throw new ParallelException(e);
        }
    }
}
