package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DynamicFSA primary = new DynamicFSA();

        System.out.println("Begin!\n\nLoading keywords:");

        // Build the transition table with the Java reserved words
        try {
            File first = new File("Proj2_Input1.txt");
            Scanner scan = new Scanner(first);
            while (scan.hasNext()) {
                String[] line = scan.nextLine().split("[\\s.]");
                for (String s : line) {
                    //System.out.println(s);
                    primary.parse(s);
                }
            }
        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }

        System.out.println("\n\nReserve words added, now processing.");

        try {
            File second = new File("Proj2_Input2.txt");
            Scanner scan = new Scanner(second);
            while (scan.hasNext()) {
                String line = scan.nextLine();
                if (line.length() > 1)
                    System.out.println(primary.process(line));
            }
        } catch(FileNotFoundException fne) {
            fne.printStackTrace();
        }

        System.out.println("\nPrinting internal tables: ");

        primary.print(8);
    }
}
