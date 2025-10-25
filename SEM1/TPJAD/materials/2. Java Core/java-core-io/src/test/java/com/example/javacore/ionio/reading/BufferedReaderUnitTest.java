package com.example.javacore.ionio.reading;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class BufferedReaderUnitTest {

    protected static final String TEXT_FILENAME = "src/test/resources/sampleTextFile.txt";

    @Test
    public void whenParsingExistingTextFile_thenGetArrayList() throws IOException {
        List<String> lines = BufferedReaderExample.generateArrayListFromFile(TEXT_FILENAME);
        assertTrue(lines.size() == 2, "File does not has 2 lines");
    }
}
