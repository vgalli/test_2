package com.vgalli;

import java.io.*;

public class SortFile {

    public static final long MAX_LINES = 6250000000l;

    private boolean useInMemoryMap = false;


    public void sort(String fileName) throws IOException {

        int numLines = 0;

        try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(fileName))) {
            while ((lineNumberReader.readLine()) != null) ;
            numLines = lineNumberReader.getLineNumber();
        }

        sortFile(fileName, numLines, MAX_LINES);
    }


    public void sortFile(String fileName, int numLines, long maxLines) throws IOException {
        System.out.println(numLines + " found in source file.");

        cleanUpOutput();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

        String line = "";

        System.out.println("Start assigning values to bucket and sorting....");
        while ((line = bufferedReader.readLine()) != null) {
            int value = Integer.parseInt(line);
            doSort(value);
        }

        bufferedReader.close();

            mergeFiles();
    }

    private void mergeFiles() throws IOException {
     System.out.println("Merging Files");
        File dir = new File("output");
        File[] files = dir.listFiles();

        BufferedWriter out = new BufferedWriter(new FileWriter(files[0], true));

        for (int i = 1; i < files.length; i++) {
            BufferedReader in = new BufferedReader(new FileReader(files[i]));
            System.out.println("Merging " + files[i].getName() + " to " + files[0].getName());
            String str;
            while ((str = in.readLine()) != null) {
                out.write(str + "\n");
            }
            in.close();
            files[i].delete();
        }

        out.close();

        System.out.println("Merge complete");
    }

    private void cleanUpOutput() throws IOException {
        File dir = new File("output");
        File[] files = dir.listFiles();
        for (File file : files) {
            file.delete();
        }
    }


    public void doSort(int value) throws IOException {

        int range = 0;

        for (int i = 1; i <= 9; i++) {
            if (value >= Integer.MAX_VALUE / 9 * i && value <= Integer.MAX_VALUE / 9 * (i + 1)) {
                range = i;
            }
        }

        int[] bucket = findBucket(range);

        bucket[bucket.length - 1] = value;
        MergeSort mergeSort = new MergeSort();
        mergeSort.sort(bucket, 0, bucket.length - 1);

            writeFile(bucket, range);

    }

    private int[] findBucket(int range) throws IOException {
        File file = new File("output/bucket_" + range + ".txt");
        int[] bucket = new int[1];

        if (file.exists()) {

            int numLines = 0;

            try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file))) {
                while ((lineNumberReader.readLine()) != null) ;
                numLines = lineNumberReader.getLineNumber();
            }

            bucket = new int[numLines + 1];

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = "";

            int i = 0;

            while ((line = bufferedReader.readLine()) != null) {
                bucket[i++] = Integer.parseInt(line);
            }

            bufferedReader.close();
        }
        return bucket;
    }


    /**
     * Write bucket to file
     *
     * @param bucket Bucket
     * @param range  File id
     */
    public void writeFile(int[] bucket, int range) throws IOException {
        File file = new File("output/bucket_" + range + ".txt");
        if (!file.exists()) {
            System.out.println("Creating bucket " + file.getName());
        }
        FileWriter fileWriter = new FileWriter(file);

        for (int value : bucket) {
            fileWriter.write(value + "\n");
        }

        fileWriter.close();
    }
}
