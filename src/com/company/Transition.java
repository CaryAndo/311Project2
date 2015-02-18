package com.company;

import java.util.HashMap;

/**
 * Created by cary on 2/17/15.
 */
public class Transition {

    public HashMap<Character, Integer> switcher = new HashMap<Character, Integer>();
    public char[] symbol = new char[1000];
    public int[] nextSpace = new int[1000];
    public int index = 0;

    public Transition() {
        char lowerCase = 'a';
        char upperCase = 'A';
        /*
        * Init all transitions to -1 (a-z A-Z and _ $)
        * */
        for (int i = 0; i < 26; i++) {
            lowerCase++;
            upperCase++;

            switcher.put(lowerCase, -1);
            switcher.put(upperCase, -1);
        }
        switcher.put('_', -1);
        switcher.put('$', -1);

        for (int i = 0; i < nextSpace.length; i++) {
            nextSpace[i] = -1;
            symbol[i] = '!';
        }
    }

    /*
    * Build the internal tables given words
    * */
    public void parse(String input) {
        if (switcher.containsKey(input.charAt(0))) {
            if (switcher.get(input.charAt(0)) == -1) {
                switcher.put(input.charAt(0), index); // Save starting point
            }
            int searchIndex = switcher.get(input.charAt(0));
            if (input.toCharArray().length > 1) {
                for (int i = 1; i < input.toCharArray().length; i++) {
                    if (input.charAt(i) == symbol[searchIndex]) {
                        searchIndex++;
                        continue; // They match, everything okay.
                    } else if (true) {

                    } else if (nextSpace[searchIndex] != -1) {
                        searchIndex = nextSpace[searchIndex]; // If a jump is defined, then jump
                        i--; // Do this iteration over again after the link has been made
                    } else {
                        nextSpace[searchIndex] = index; // Set the link to be the end of our symbol array
                        searchIndex = index; // Set the symbol index to be the end of the array (should be empty)
                        symbol[searchIndex] = input.charAt(i); // Set the empty space to be our character
                        index++; // Increment the end of the array
                        searchIndex++; // Increment the symbol index
                    }
                }
            } else {
                while (symbol[searchIndex] != '@') {
                    if (nextSpace[searchIndex] != -1) {
                        searchIndex = nextSpace[searchIndex]; // Follow the link
                    } else {
                        nextSpace[searchIndex] = index; // Set the link to be the end of our symbol array
                        searchIndex = index; // Set the symbol index to be the end of the array (should be empty)
                        symbol[searchIndex] = '@'; // Set the empty space at the end of symbol to be a terminator
                        index++;
                        searchIndex++;
                    }
                }
            }
        } else {
            System.out.println("Invalid character: " + input.charAt(0));
            return;
        }
    }
}
