/*
  Authors (group members): Jonathan Bailey, Pedro Moura, Jordan Synodis, Michael Richards
  Email addresses of group members: pmoura2020@my.fit.edu, Jbailey2021@my.fit.edu, jsynodis2021@my.fit.edu, mrichards2021@my.fit.edu
  Group name: 12JPMJ

  Course: CSE 2010
  Section: 1

  Description of the overall algorithm: This program takes in the user input and attempts to guess the rest of the word by providing 3
                                        guesses based on the already typed letters
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SmartWord {
    String[] guesses = new String[3];  // 3 guesses from SmartWord
    Trie temp = new Trie(); // trie holds all of the possible words
    StringBuilder currWord = new StringBuilder(""); // current word that's being typed

    // initialize SmartWord with a file of English words
    public SmartWord(String wordFile) throws FileNotFoundException {
      wordFile = "dictionaryWords.txt";
      File file = new File(wordFile);
      Scanner words = new Scanner(file);

      while (words.hasNextLine()) {
        temp.root.insert(words.nextLine()); // inserts each word in the trie
      }
      words.close(); // closes scanner
    }

    // process old messages from oldMessageFile
    public void processOldMessages(String oldMessageFile) throws FileNotFoundException {
      File file = new File(oldMessageFile);
      Scanner messages = new Scanner(file);

      // list of certain puncuation and numbers
      char[] symbolList = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '`', '~', '!', '.', '?', ',', ';', '$', '%', '^', '&',
                          '*', '(', ')', '+', '=', '{', '}', '[', ']', '|', '\\', ':', '"', '<', '>', '/', '\''};

      ArrayList<Character> symbols = new ArrayList<>();
      for (char c : symbolList) {
        symbols.add(c); // puts symbols into an ArrayList
      }

      while (messages.hasNextLine()) {
        String[] words = messages.nextLine().split(" "); // makes array of all the words in each line

        for (String word : words) {
          if (word.length() > 1) { // if the word is 2 or more characters long
            if (symbols.indexOf(word.charAt(0)) != -1) {
              word = word.substring(1); // get rid of the symbol in the beginning if present
            }
            if (symbols.indexOf(word.charAt(word.length() - 1)) != -1 && word.length() > 2) {
              word = word.substring(0, word.length() - 2); // get rid of the symbol in the end if present
            } else if (symbols.indexOf(word.charAt(word.length() - 1)) != -1) {
              word = word.substring(0, 0); // get rid of the symbol in the end if present
            }
  
            if (temp.find(word)) {
              temp.root.insert(word); // if word is already there, insert word into trie
            } else {
              temp.root.addWord(word); // if the word is not already there, add word into trie
            }
          }
        }
      }
      messages.close(); // closes scanner
    }

    // based on a letter typed in by the user, return 3 word guesses in an array
    // letter: letter typed in by the user
    // letterPosition:  position of the letter in the word, starts from 0
    // wordPosition: position of the word in a message, starts from 0
    public String[] guess(char letter,  int letterPosition, int wordPosition) {
      if (letterPosition == 0 && currWord.length() > 0) {
        currWord.replace(0, currWord.length(), ""); // if new word, clear sting builder
        currWord.append(letter); // add current letter to string builder
      } else {
        currWord.append(letter); // add current letter to sting builder
      }

      List<String> top = temp.guess(currWord.toString()); // suggest string builder and store guesses
      for (int i = 0; i < top.size(); i++) {
        guesses[i] = top.get(i); // transfer top guesses into guess array
      }
      return guesses; // return guesses
    }

    // feedback on the 3 guesses from the user
    // isCorrectGuess: true if one of the guesses is correct
    // correctWord: 3 cases:
    // a.  correct word if one of the guesses is correct
    // b.  null if none of the guesses is correct, before the user has typed in 
    //            the last letter
    // c.  correct word if none of the guesses is correct, and the user has 
    //            typed in the last letter
    // That is:
    // Case       isCorrectGuess      correctWord   
    // a.         true                correct word
    // b.         false               null
    // c.         false               correct word
    public void feedback(boolean isCorrectGuess, String correctWord) {
      if (correctWord == null) {
        return; // if the word is blank, return
      }
      temp.root.insert(correctWord); // insert the word into the trie to increase weight
    }
}
