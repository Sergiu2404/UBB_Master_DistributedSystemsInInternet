//package com.example.javacore.io.writing.appendtofile;
//
//import org.apache.commons.io.FileUtils;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.jupiter.api.Test;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class AppendToFileManualTest {
//
//    public static final String fileName = "src/main/resources/countries.properties";
//
//    @Before
//    @After
//    public void setup() throws Exception {
//        PrintWriter writer = new PrintWriter(fileName);
//        writer.print("UK\r\n" + "US\r\n" + "Germany\r\n");
//        writer.close();
//    }
//
//    @Test
//    public void whenAppendToFileUsingFiles_thenCorrect() throws IOException {
//        Files.write(Paths.get(fileName), "Spain\r\n".getBytes(), StandardOpenOption.APPEND);
//
//        assertThat(StreamUtils.getStringFromInputStream(new FileInputStream(fileName))).isEqualTo("UK\r\n" + "US\r\n" + "Germany\r\n" + "Spain\r\n");
//    }
//
//    @Test
//    public void whenAppendToFileUsingFileUtils_thenCorrect() throws IOException {
//        File file = new File(fileName);
//        FileUtils.writeStringToFile(file, "Spain\r\n", StandardCharsets.UTF_8, true);
//
//        assertThat(StreamUtils.getStringFromInputStream(new FileInputStream(fileName))).isEqualTo("UK\r\n" + "US\r\n" + "Germany\r\n" + "Spain\r\n");
//    }
//
//    @Test
//    public void whenAppendToFileUsingFileOutputStream_thenCorrect() throws Exception {
//        FileOutputStream fos = new FileOutputStream(fileName, true);
//        fos.write("Spain\r\n".getBytes());
//        fos.close();
//
//        assertThat(StreamUtils.getStringFromInputStream(new FileInputStream(fileName))).isEqualTo("UK\r\n" + "US\r\n" + "Germany\r\n" + "Spain\r\n");
//    }
//
//    @Test
//    public void whenAppendToFileUsingFileWriter_thenCorrect() throws IOException {
//        FileWriter fw = new FileWriter(fileName, true);
//        BufferedWriter bw = new BufferedWriter(fw);
//        bw.write("Spain");
//        bw.newLine();
//        bw.close();
//
//        assertThat(StreamUtils.getStringFromInputStream(new FileInputStream(fileName))).isEqualTo("UK\r\n" + "US\r\n" + "Germany\r\n" + "Spain\r\n");
//    }
//}