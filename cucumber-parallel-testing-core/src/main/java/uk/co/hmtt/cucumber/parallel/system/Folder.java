package uk.co.hmtt.cucumber.parallel.system;

import uk.co.hmtt.cucumber.parallel.exceptions.ParallelException;

import java.io.File;

import static java.lang.String.format;

public class Folder {

    public static void create(final String path) {
        final File file = new File(path);
        if (!file.exists()) {
            if ( ! file.mkdirs() ) {
                throw new ParallelException(format("Failed to create directories for path %s", path));
            }
        }
    }

}
