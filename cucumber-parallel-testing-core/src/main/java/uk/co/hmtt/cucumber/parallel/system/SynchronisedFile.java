package uk.co.hmtt.cucumber.parallel.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.co.hmtt.cucumber.parallel.Constants;
import uk.co.hmtt.cucumber.parallel.exceptions.ParallelException;
import uk.co.hmtt.cucumber.parallel.model.FeatureFileRecorder;

import java.io.IOException;
import java.io.RandomAccessFile;

public class SynchronisedFile {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private RandomAccessFile rw;

    public boolean hasNotBeenRun(Class<FeatureFileRecorder> clz, String featureName) {

        Folder.create(Constants.PARALLEL_WORKING_DIR);
        try {
            lock(clz);
            return updateLockedFile(clz, featureName);
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            throw new ParallelException(e);
        }

    }

    private void lock(Class<FeatureFileRecorder> clz) throws IOException {
        if (rw == null) {
            rw = new RandomAccessFile(Constants.PARALLEL_WORKING_DIR + clz.getClass().getName() + ".lock", "rw");
            rw.getChannel().lock();
        }
    }

    private boolean updateLockedFile(Class<FeatureFileRecorder> clz, String name) throws IOException, IllegalAccessException, InstantiationException {

        boolean updated = false;
        if (rw.length() == 0) {
            createNewSynchronizedObject();
            updated = true;
        } else {
            final FeatureFileRecorder featureFileRecorder = objectMapper.readValue(rw.readUTF(), clz);
            if (!featureFileRecorder.getFeatureFiles().contains(name)) {
                featureFileRecorder.getFeatureFiles().add(name);
                write(featureFileRecorder);
                updated = true;
            }
        }
        release();
        return updated;

    }

    private Object createNewSynchronizedObject() throws IllegalAccessException, InstantiationException, IOException {
        final FeatureFileRecorder featureFileRecorder = new FeatureFileRecorder();
        rw.writeUTF(objectMapper.writeValueAsString(featureFileRecorder));
        return featureFileRecorder;
    }

    private void write(FeatureFileRecorder obj) throws IOException {
        rw.setLength(0);
        rw.writeUTF(objectMapper.writeValueAsString(obj));
    }

    private void release() throws IOException {
        rw.getChannel().close();
        rw = null;
    }

}
