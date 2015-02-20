package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ArrayList<Integer> lineSpace = new ArrayList<Integer>();
        DynamicFSA test = new DynamicFSA();
        /*test.parse("tony");
        test.parse("tonytime");
        test.parse("tonytable");
        test.parse("tonytaple");
        test.parse("t");
        test.parse("a");
        test.parse("app");

        test.printTables(50);
*/
        // Build the transition table with the Java reserved words
        try {
            File first = new File("Proj2_Input1.txt");
            Scanner scan = new Scanner(first);
            while (scan.hasNext()) {
                String[] line = scan.nextLine().split("[\\s.]");
                for (String s : line) {
                    //System.out.println(s);
                    test.parse(s);
                }
            }
            //lineSpace.add(lineCount); // Remember how many words were on this line
        } catch(FileNotFoundException fne) {
            fne.printStackTrace();
        }
        test.printTables(200);

        System.out.println("\n\nTesting process:");

        try {
            File second = new File("Proj2_Input2.txt");
            Scanner scan = new Scanner(second);
            int lineCount = 0; // Number of words on this line
            while (scan.hasNext()) {
                String line = scan.nextLine();
                if (line.length() > 1)
                    System.out.println(test.process(line));
            }
            lineSpace.add(lineCount); // Remember how many words were on this line
        } catch(FileNotFoundException fne) {
            fne.printStackTrace();
        }
        test.process("zed");

        System.out.println("END,\n Index: " + test.index);

        test.print();

        System.out.println("" + test.switcher.get('z'));
    }
}
