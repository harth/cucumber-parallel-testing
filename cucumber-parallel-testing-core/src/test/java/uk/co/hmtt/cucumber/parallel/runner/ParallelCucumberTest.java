package uk.co.hmtt.cucumber.parallel.runner;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.junit.FeatureRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.hmtt.cucumber.parallel.Constants;
import uk.co.hmtt.cucumber.parallel.model.Recorder;
import uk.co.hmtt.cucumber.parallel.system.SynchronisedFile;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class ParallelCucumberTest {

    @Mock
    private FeatureRunner featureRunner;

    @Mock
    private RunNotifier notifier;

    @Mock
    private SynchronisedFile<Recorder> recorder;

    private ParallelCucumber parallelCucumber;

    @Test @Ignore
    public void shouldNotRunTheTestIfItHasBeenRunAlready() throws IOException, InitializationError, IllegalAccessException, NoSuchFieldException {

        final Recorder recorder = new Recorder();
        recorder.getFeatures().add("test");

        when(this.recorder.read(any(Class.class))).thenReturn(recorder);
        when(featureRunner.getName()).thenReturn("test");

        parallelCucumber.runChild(featureRunner, notifier);

        verify(featureRunner, never()).run(any(RunNotifier.class));

    }

    @Test @Ignore
    public void shouldRunTheTestIfItHasNotBeenRunAlready() throws IOException, InitializationError, NoSuchFieldException, IllegalAccessException {

        when(this.recorder.read(any(Class.class))).thenReturn(new Recorder());
        when(featureRunner.getName()).thenReturn("new test");

        parallelCucumber.runChild(featureRunner, notifier);

        verify(featureRunner, times(1)).run(any(RunNotifier.class));

    }

    @Before
    public void init() throws IOException, InitializationError {
        new File(Constants.PARALLEL_WORKING_DIR + Recorder.class.getName() + ".lock").delete();
        parallelCucumber = new ParallelCucumber(Cucumber.class);
        initMocks(parallelCucumber);
    }

}