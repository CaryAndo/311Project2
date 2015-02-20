package com.company;

import java.util.HashMap;

/**
 * Dynamic Finite State Automaton
 * @author Cary Anderson
 */
public class DynamicFSA {

    public HashMap<Character, Integer> switcher = new HashMap<Character, Integer>();
    public char[] symbol = new char[1000];
    public int[] nextSpace = new int[1000];
    public int index = 0;

    public DynamicFSA() {
        char lowerCase = 'a';
        char upperCase = 'A';
        /*
        * Init all transitions to -1 (a-z A-Z and _ $)
        * */
        for (int i = 0; i < 26; i++) {
            switcher.put(lowerCase, -1);
            switcher.put(upperCase, -1);

            lowerCase++;
            upperCase++;
        }
        switcher.put('_', -1);
        switcher.put('$', -1);

        for (int i = 0; i < nextSpace.length; i++) {
            nextSpace[i] = -1;
            symbol[i] = '!';
        }
    }

    /*
    * Print the two arrays nicely for debug
    * */
    public void printTables(int length) {
        //convertToAt();
        if (length > 999)
            length = 1000;
        System.out.println("\nsymbol: ");
        for (int i = 0; i < length; i++) {
            System.out.print("  " + symbol[i]);
        }
        System.out.println("\nnext: ");
        for (int i = 0; i < length; i++) {
            if (nextSpace[i] < 0 || nextSpace[i] > 9)
                System.out.print(" " + nextSpace[i]);
            else
                System.out.print(" +" + nextSpace[i]);
        }
        //convertToStar();
    }

    /**
     * Process line after parsing keywords
     * @param input A line of code.
     * @return The parsed line to print.
     * TODO Handle end of line.
     */
    public String process(String input) {
        if (input == null) {
            System.out.println("Error, passed null");
            return "";
        }

        String ret = "";
        boolean start = true; // Keeps track if we're processing a new word.
        int searchIndex = 0; // Keeps track of our current position
        String currentWord = "";

        for (int i = 0; i < input.length(); i++) {
            if (start) {
                currentWord = "";
                if (!"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_$".contains("" + input.charAt(i))) {
                    continue; // Ignore if its not valid
                }

                if (switcher.get(input.charAt(i)) == -1) {
                    switcher.put(input.charAt(i), index);
                }

                searchIndex = switcher.get(input.charAt(i)); // Set our next input based on switcher.
                currentWord += input.charAt(i);

                start = false;
            } else {
                /*
                * If we're at the end of a word, then figure out what kind it is
                * */
                if (!"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_$".contains("" + input.charAt(i))) {
                    while (symbol[searchIndex] != '*' &&
                            symbol[searchIndex] != '@' &&
                            symbol[searchIndex] != '!' &&
                            symbol[searchIndex] != '?')
                    {
                        if (nextSpace[searchIndex] != -1) {
                            searchIndex = nextSpace[searchIndex];
                        } else {
                            nextSpace[searchIndex] = index;
                            searchIndex = index;
                        }
                    }
                    switch (symbol[searchIndex]) {
                        case '!':
                            symbol[searchIndex] = '?';
                            index++;
                            searchIndex++;
                            currentWord += '?';
                            break;
                        case '@':
                            currentWord += '@';
                            break;
                        case '?':
                            currentWord += '@';
                            break;
                        case '*':
                            currentWord += '*';
                            break;
                    }
                    ret += (currentWord + ' ');

                    start = true; // Only set start to true if we encounter an invalid character
                    //continue;
                } else {
                    if (symbol[searchIndex] == '!') {
                        symbol[searchIndex] = input.charAt(i); // If the symbol is empty then write to it
                        index++;
                        searchIndex++;
                        currentWord += input.charAt(i);
                    } else if (symbol[searchIndex] == input.charAt(i)) {
                        searchIndex++;
                        currentWord += input.charAt(i);
                    } else if (nextSpace[searchIndex] != -1) {
                        searchIndex = nextSpace[searchIndex]; // jump if defined
                        i--; // Process again after jumping
                    } else {
                        nextSpace[searchIndex] = index; // Set the link to be the end of our symbol array
                        searchIndex = index; // Set the symbol index to be the end of the array (should be empty)
                        symbol[searchIndex] = input.charAt(i); // Set the empty space to be our character
                        index++; // Increment the end of the array
                        searchIndex++; // Increment the symbol index
                        currentWord += input.charAt(i);
                    }
                }
            }
        }
        /*
        * After we reach the end of the line, if we were not finished with a word figure out the terminal character.
        * */
        if (!start) {
            while (symbol[searchIndex] != '*' &&
                    symbol[searchIndex] != '@' &&
                    symbol[searchIndex] != '!' &&
                    symbol[searchIndex] != '?')
            {
                if (nextSpace[searchIndex] != -1) {
                    searchIndex = nextSpace[searchIndex];
                } else {
                    nextSpace[searchIndex] = index;
                    searchIndex = index;
                }
            }
            switch (symbol[searchIndex]) {
                case '!':
                    symbol[searchIndex] = '?';
                    index++;
                    //searchIndex++;
                    currentWord += '?';
                    break;
                case '@':
                    currentWord += '@';
                    break;
                case '?':
                    currentWord += '@';
                    break;
                case '*':
                    currentWord += '*';
                    break;
            }
            ret += (currentWord + ' ');
        }

        return ret;
    }

    /*
    * Convert all known java reserved words into being stored with * notation
    * */
    private void convertToStar() {
        for (int i = 0; i < symbol.length; i++) {
            if (symbol[i] == '@')
                symbol[i] = '*'; // Set to be stored with * so we know they are keywords.
        }
    }

    /*
    * Convert all star keywords to @ symbols
    * */
    private void convertToAt() {
        for (int i = 0; i < symbol.length; i++) {
            if (symbol[i] == '*')
                symbol[i] = '@'; //
        }
    }

    /**
     * Build the internal tables given words
     * @param input A keyword to add to the table
    */
    public void parse(String input) {
        convertToAt(); // So that we can keep parsing nicely
        if (input == null) {
            System.out.println("Error, passed null");
            return;
        }

        // Screen for bad characters
        for (char c : input.toCharArray()) {
            if (!"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_$".contains(""+c)) {
                System.out.println("ERROR, string contains an invalid character: " + c);
                return;
            }
        }

        input = input + "@";

        if (switcher.containsKey(input.charAt(0))) {
            if (switcher.get(input.charAt(0)) == -1) {
                switcher.put(input.charAt(0), index); // Save starting point
            }

            int searchIndex = switcher.get(input.charAt(0));
            /*
            * If the string is not just a single character
            * */
            if (input.toCharArray().length > 1) {
                for (int i = 1; i < input.toCharArray().length; i++) {
                    if (symbol[searchIndex] == '!') {
                        symbol[searchIndex] = input.charAt(i); // If the symbol is empty then write to it
                        index++;
                        searchIndex++;
                    } else if (input.charAt(i) == symbol[searchIndex]) {
                        searchIndex++; // If the symbols match, all is well continue on
                    } else if (nextSpace[searchIndex] != -1) {
                        searchIndex = nextSpace[searchIndex]; // If a jump is defined, then jump
                        i--; // Do this iteration over again after we have jumped to the link
                    } else {
                        nextSpace[searchIndex] = index; // Set the link to be the end of our symbol array
                        searchIndex = index; // Set the symbol index to be the end of the array (should be empty)
                        symbol[searchIndex] = input.charAt(i); // Set the empty space to be our character
                        index++; // Increment the end of the array
                        searchIndex++; // Increment the symbol index
                    }
                }
            } else {
                /*
                * Else search for the end of the single letter identifier
                * TODO Examine this logic later
                * */
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
        }
        convertToStar(); // So we are ready to either parse more or start accepting other words.
    }
}
