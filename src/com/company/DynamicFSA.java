package com.company;

import java.util.HashMap;

/**
 * Dynamic Finite State Automaton
 * @author Cary Anderson
 */
public class DynamicFSA {

    private HashMap<Character, Integer> switcher = new HashMap<Character, Integer>();
    private char[] symbol = new char[1000];
    private int[] nextSpace = new int[1000];
    private int index = 0;

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

    /**
     *  Print all the tables and transitions in a pretty format
     *  @param itemsPerLine The number of table entries to print per line.
     */
    public void print(int itemsPerLine) {
        /*
        * First print out the switch
        * */
        String switchString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_$";
        System.out.print("\nSwitch:  ");
        for (int i = 0; i < switchString.length(); i++) {
            if ((i > 0 && i % itemsPerLine == 0)) {
                System.out.print("\n      ");
                int temp = i - 1;
                while (temp % itemsPerLine != 0)
                    temp--;
                for (int j = temp; j < i; j++) {
                    String leftSpace;
                    String rightSpace;
                    if (switcher.get(switchString.charAt(j)) > 100) {
                        leftSpace = "  ";
                        rightSpace = "";
                    } else if (switcher.get(switchString.charAt(j)) > 10 || switcher.get(switchString.charAt(j)) < 0) {
                        leftSpace = "  ";
                        rightSpace = " ";
                    } else {
                        leftSpace = "  ";
                        rightSpace = "  ";
                    }
                    System.out.print(leftSpace + switcher.get(switchString.charAt(j)) + rightSpace);
                }
                System.out.print("\n\nSwitch: ");
            }

            if (i == switchString.length()-1) {
                System.out.print(switchString.charAt(i) + "    ");

                System.out.print("\n      ");
                int temp = i - 1;
                while (temp % itemsPerLine != 0)
                    temp--;
                for (int j = temp; j <= i; j++) {
                    String leftSpace;
                    String rightSpace;
                    if (switcher.get(switchString.charAt(j)) > 100) {
                        leftSpace = "  ";
                        rightSpace = "";
                    } else if (switcher.get(switchString.charAt(j)) > 10 || switcher.get(switchString.charAt(j)) < 0) {
                        leftSpace = "  ";
                        rightSpace = " ";
                    } else {
                        leftSpace = "  ";
                        rightSpace = "  ";
                    }
                    System.out.print(leftSpace + switcher.get(switchString.charAt(j)) + rightSpace);
                }
            } else {
                System.out.print(switchString.charAt(i) + "    ");
            }
        }
        /*
        * Next print out the symbol and the next arrays
        * */
        System.out.print("\n\nSymbol: ");
        for (int i = 0; i < lastSymbolIndex(); i++) {
            if ((i > 0 && i % itemsPerLine == 0)) {
                System.out.print("\nnext: ");
                int temp = i - 1;
                while (temp % itemsPerLine != 0)
                    temp--;
                for (int j = temp; j < i; j++) {
                    String leftSpace;
                    String rightSpace;
                    if (nextSpace[j] > 100) {
                        leftSpace = "  ";
                        rightSpace = "";
                    } else if (nextSpace[j] > 10 || nextSpace[j] < 0) {
                        leftSpace = "  ";
                        rightSpace = " ";
                    } else {
                        leftSpace = "  ";
                        rightSpace = "  ";
                    }
                    if (nextSpace[j] == -1)
                        System.out.print(leftSpace + "  " + rightSpace);
                    else
                        System.out.print(leftSpace + nextSpace[j] + rightSpace);
                }
                System.out.print("\n\nSymbol: ");
            }

            if (i == lastSymbolIndex()-1) {
                System.out.print(symbol[i] + "    ");

                System.out.print("\nnext:  ");
                int temp = i - 1;
                while (temp % itemsPerLine != 0)
                    temp--;
                for (int j = temp; j <= i; j++) {
                    //System.out.print(nextSpace[j] + "   ");
                    if (nextSpace[j] == -1)
                        System.out.print("     " );
                    else
                        System.out.print(nextSpace[j]+ "   ");
                }
            } else {
                System.out.print(symbol[i] + "    ");
            }
        }

        System.out.println("\n\nFinished! ");
        System.out.println("Empty table spaces remaining: " + (symbol.length - lastSymbolIndex()));
    }

    /*
    * The index where the last symbol is in the symbol array
    * */
    private int lastSymbolIndex() {
        int counter = 0;
        for (char c : symbol) {
            if (c == '!') {
                return counter;
            }
            counter++;
        }
        return symbol.length;
    }

    /**
     * Process line after parsing keywords
     * @param input A line of code.
     * @return The line containing only keywords with their types appended.
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
