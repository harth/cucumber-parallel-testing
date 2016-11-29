package parallel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.nio.channels.FileChannel;

public abstract class SynchronisedObject {

    private FileChannel fileChannel;

    @JsonIgnore
    public FileChannel getFileChannel() {
        return fileChannel;
    }

    public void setFileChannel(final FileChannel fileChannel) {
        this.fileChannel = fileChannel;
    }

}
