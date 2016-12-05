package uk.co.hmtt.cucumber.parallel.system;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class FolderTest {

    public static final String PATH = System.getProperty("user.dir") + "/target/parallel/lock.txt";
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void shouldCreateFolderIfItDoesNotAlreadyExist() throws IOException {

        final File file = new File(PATH);
        assertThat(file.exists(), is(false));

        Folder.create(PATH);

        assertThat(file.exists(), is(true));

    }

    @Test
    public void shouldNotCreateFolderIfItAlreadyExists() {

        final File fileBefore = new File(PATH);
        final boolean directoryCreatedByTest = fileBefore.mkdirs();
        assertThat(directoryCreatedByTest, is(true));

        Folder.create(PATH);

        final File fileAfter = new File(PATH);

        assertThat("Created date should not have changed", fileBefore.lastModified(), is(equalTo(fileAfter.lastModified())));

    }

    @Before
    public void init() {
        deleteFile();
    }

    @AfterClass
    public static void tearDown() {
        deleteFile();
    }

    private static void deleteFile() {
        final File file = new File(PATH);
        file.delete();
    }

}