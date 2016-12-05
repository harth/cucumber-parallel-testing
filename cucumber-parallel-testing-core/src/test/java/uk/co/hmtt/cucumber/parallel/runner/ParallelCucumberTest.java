package uk.co.hmtt.cucumber.parallel.runner;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.junit.FeatureRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.hmtt.cucumber.parallel.model.Recorder;
import uk.co.hmtt.cucumber.parallel.system.SynchronisedFile;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParallelCucumberTest {

    @Mock
    private FeatureRunner featureRunner;

    @Mock
    private RunNotifier notifier;

    @Mock
    private SynchronisedFile<Recorder> recorder;

    @Test
    public void shouldNotRunTheTestIfItHasBeenRunAlready() throws IOException, InitializationError, IllegalAccessException, NoSuchFieldException {

        final ParallelCucumber parallelCucumber = new ParallelCucumber(Cucumber.class);
        final Field synchronisedFile = ParallelCucumber.class.getDeclaredField("synchronisedFile");
        synchronisedFile.setAccessible(true);
        synchronisedFile.set(parallelCucumber, recorder);

        final Recorder recorder = new Recorder();
        recorder.getFeatures().add("test");

        when(this.recorder.read(any(Class.class))).thenReturn(recorder);

        when(featureRunner.getName()).thenReturn("test");
        parallelCucumber.runChild(featureRunner, notifier);

        verify(featureRunner, never()).run(any(RunNotifier.class));

    }

    @Test
    public void shouldRunTheTestIfItHasNotBeenRunAlready() throws IOException, InitializationError, NoSuchFieldException, IllegalAccessException {

        final ParallelCucumber parallelCucumber = new ParallelCucumber(Cucumber.class);
        final Field synchronisedFile = ParallelCucumber.class.getDeclaredField("synchronisedFile");
        synchronisedFile.setAccessible(true);
        synchronisedFile.set(parallelCucumber, recorder);

        when(this.recorder.read(any(Class.class))).thenReturn(new Recorder());
        parallelCucumber.runChild(featureRunner, notifier);

        when(featureRunner.getName()).thenReturn("new test");

        verify(featureRunner, times(1)).run(any(RunNotifier.class));

    }

}