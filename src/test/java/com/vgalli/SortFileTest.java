package com.vgalli;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SortFileTest {

    private final static int NUM_LINES = 10000;

    @BeforeClass
    public static void buildTestFile() throws IOException {
        FileWriter fileWriter = new FileWriter("src/test/resources/data.txt");
        Random random = new Random();
        for (int i = 0; i < NUM_LINES; i++) {
            try {
                fileWriter.write(random.nextInt(Integer.MAX_VALUE - 1) + "\n");
            } catch (IOException e) {
                try {
                    fileWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        try {
            fileWriter.close();
        } catch (IOException e) {
            //Ignore
        }
    }

    @Test
    public void sortFile() {
        SortFile sortFile = new SortFile();
        try {
            sortFile.sort("src/test/resources/data.txt" );
        } catch (IOException e) {
            fail(e.getMessage());
        }

        int numLines = 0;

        try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader("src/test/resources/data.txt"))) {
            while ((lineNumberReader.readLine()) != null) ;
            numLines = lineNumberReader.getLineNumber();
            assertEquals("File number mismatch", NUM_LINES, numLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
