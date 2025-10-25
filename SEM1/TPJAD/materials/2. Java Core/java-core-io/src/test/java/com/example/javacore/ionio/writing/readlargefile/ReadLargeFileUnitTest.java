package com.example.javacore.ionio.writing.readlargefile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.stream.Stream;


public class ReadLargeFileUnitTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    public static final String FILE_PATH = "src/test/resources/input.txt";

    @Test
    public final void givenUsingCommonsIo_whenIteratingAFileInMemory_thenCorrect() throws IOException {
        final String path = FILE_PATH;

        logMemory();
        FileUtils.readLines(new File(path));
        logMemory();
    }

    @Test
    public final void whenStreamingThroughAFile_thenCorrect() throws IOException {
        final String path = FILE_PATH;

        logMemory();

        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(path);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                final String line = sc.nextLine();
                // System.out.println(line);
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }

        logMemory();
    }

    @Test
    public final void givenUsingApacheIo_whenStreamingThroughAFile_thenCorrect() throws IOException {
        final String path = FILE_PATH;

        logMemory();

        final LineIterator it = FileUtils.lineIterator(new File(path), "UTF-8");
        try {
            while (it.hasNext()) {
                final String line = it.nextLine();
                // do something with line
            }
        } finally {
            LineIterator.closeQuietly(it);
        }

        logMemory();
    }

    @Test
    public void givenUsingBufferedReader_whenIteratingAFile_thenCorrect() throws IOException {
        String fileName = FILE_PATH;

        logMemory();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while (br.readLine() != null) {
                // do something with each line
            }
        }
        logMemory();
    }

    @Test
    public void givenUsingNewBufferedReader_whenIteratingAFile_thenCorrect() throws IOException {
        String fileName = FILE_PATH;

        logMemory();
        try (BufferedReader br = java.nio.file.Files.newBufferedReader(Paths.get(fileName))) {
            while (br.readLine() != null) {
                // do something with each line
            }
        }
        logMemory();
    }

    @Test
    public void givenUsingSeekableByteChannel_whenIteratingAFile_thenCorrect() throws IOException {
        String fileName = FILE_PATH;

        logMemory();
        try (SeekableByteChannel ch = java.nio.file.Files.newByteChannel(Paths.get(fileName), StandardOpenOption.READ)) {
            ByteBuffer bf = ByteBuffer.allocate(1000);
            while (ch.read(bf) > 0) {
                bf.flip();
                // System.out.println(new String(bf.array()));
                bf.clear();
            }
        }
        logMemory();
    }

    @Test
    public void givenUsingStreamApi_whenIteratingAFile_thenCorrect() throws IOException {
        String fileName = FILE_PATH;

        logMemory();
        try (Stream<String> lines = java.nio.file.Files.lines(Paths.get(fileName))) {
            lines.forEach(line -> {
                // do something with each line
            });
        }
        logMemory();
    }

    // utils

    private final void logMemory() {
        logger.info("Max Memory: {} Mb", Runtime.getRuntime()
            .maxMemory() / 1048576);
        logger.info("Total Memory: {} Mb", Runtime.getRuntime()
            .totalMemory() / 1048576);
        logger.info("Free Memory: {} Mb", Runtime.getRuntime()
            .freeMemory() / 1048576);
    }

}
