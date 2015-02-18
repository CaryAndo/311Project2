package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static int max_transition = 1000; // Size of arrays

    public static void main(String[] args) {
        ArrayList<Integer> lineSpace = new ArrayList<Integer>();
        try {
            File first = new File("Proj2_Input1.txt");
            Scanner scan = new Scanner(first);
            int lineCount = 0; // Number of words on this line
            while (scan.hasNext()) {
                String[] line = scan.nextLine().split("\\s");
                for (String s : line) {
                    System.out.println(s);
                }
                lineCount++;
            }
            lineSpace.add(lineCount); // Remember how many words were on this line
        } catch(FileNotFoundException fne) {
            fne.printStackTrace();
        }

        System.out.println("Hey World");
    }
}