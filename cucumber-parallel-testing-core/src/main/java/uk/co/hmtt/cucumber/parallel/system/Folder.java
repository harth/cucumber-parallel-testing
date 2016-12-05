package uk.co.hmtt.cucumber.parallel.system;

import java.io.File;

public class Folder {

    public static void create(final String path) {
        final File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
