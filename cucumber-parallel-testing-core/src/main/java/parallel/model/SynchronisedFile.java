package parallel.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parallel.exceptions.ParallelException;
import parallel.runner.ParallelCucumber;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.*;

import static parallel.Constants.PARALLEL_WORKING_DIR;

public class SynchronisedFile<T> {

    ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronisedFile.class);
    RandomAccessFile rw;

    public T read(Class<T> clz) {

        final String fileAsSting = PARALLEL_WORKING_DIR + clz.getName() + ".lock";

        if (!Files.exists(Paths.get(fileAsSting))) {
            try {
                LOGGER.debug("Creating directory");
                Files.createDirectory(Paths.get(PARALLEL_WORKING_DIR));
            }catch (FileAlreadyExistsException e) {
                LOGGER.debug("failed to create directory");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            rw = new RandomAccessFile(fileAsSting, "rw");
            rw.getChannel().lock();

            if (rw.length() == 0) {
                try {
                    String jsonInString = objectMapper.writeValueAsString(clz.newInstance());
                    rw.writeUTF(jsonInString);
                    return clz.newInstance();
                } catch (InstantiationException  | IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                return objectMapper.readValue(rw.readUTF(), clz);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

        public void write(T obj) {
        try {
            String jsonInString = objectMapper.writeValueAsString(obj);

            LOGGER.debug("Writing value {}", jsonInString);

            rw.setLength(0);

            rw.writeUTF(jsonInString);
            rw.getChannel().close();
        } catch (IOException e) {
            throw new ParallelException(e);
        }
    }

    public void release() {
        try {
            rw.getChannel().close();
        } catch (IOException e) {
            throw new ParallelException(e);
        }
    }

}
