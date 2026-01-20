package com.example.javacore.ionio.reading;


import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FileReaderUnitTest {

    protected static final String TEXT_FILENAME = "src/test/resources/sampleTextFile.txt";
    
    @Test
    public void whenParsingExistingTextFile_thenGetArrayList() throws IOException {
        List<String> lines = FileReaderExample.generateArrayListFromFile(TEXT_FILENAME);
        assertTrue(lines.size() == 2, "File does not has 2 lines");
    }
}
