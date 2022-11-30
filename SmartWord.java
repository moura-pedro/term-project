/*

  Authors (group members):
  Email addresses of group members:
  Group name:

  Course:
  Section:

  Description of the overall algorithm:


*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

public class SmartWord {
    String[] guesses = new String[3];  // 3 guesses from SmartWord
    Trie temp = new Trie();
    StringBuilder currWord = new StringBuilder("");
    List<String> goodWords = new ArrayList<>();
    List<String> badWords = new ArrayList<>();

    // initialize SmartWord with a file of English words
    public SmartWord(String wordFile) throws FileNotFoundException {
      /*
      File file = new File(wordFile);
      Scanner words = new Scanner(file);

      while (words.hasNextLine()) {
        temp.root.insert(words.nextLine());
      }
      words.close();
      */
    }

    // process old messages from oldMessageFile
    public void processOldMessages(String oldMessageFile) throws FileNotFoundException {
      File file = new File(oldMessageFile);
      Scanner messages = new Scanner(file);

      char[] symbolList = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '`', '~', '!', '.', '?', ',', ';', '$', '%', '^', '&', '*', '(', ')', '+', '=', '{', '}', '[', ']', '|', '\\', ':', '"', '<', '>', '/', '\''};
      ArrayList<Character> symbols = new ArrayList<>();
      for (char c : symbolList) {
        symbols.add(c);
      }

      while (messages.hasNextLine()) {
        String[] words = messages.nextLine().split(" ");

        for (String word : words) {
          if (word.length() > 2) {
            if (symbols.indexOf(word.charAt(0)) != -1) {
              word = word.substring(1);
            }
            if (symbols.indexOf(word.charAt(word.length() - 1)) != -1) {
              word = word.substring(0, word.length() - 2);
            }
  
            if (temp.find(word)) {
              temp.root.insert(word);
            } else {
              temp.root.addWord(word);
            }
          }
        }
      }
      messages.close();
    }

    // based on a letter typed in by the user, return 3 word guesses in an array
    // letter: letter typed in by the user
    // letterPosition:  position of the letter in the word, starts from 0
    // wordPosition: position of the word in a message, starts from 0
    public String[] guess(char letter,  int letterPosition, int wordPosition) {
      if (letterPosition == 0 && currWord.length() > 0) {
        currWord.replace(0, currWord.length(), "");
        currWord.append(letter);
      } else {
        currWord.append(letter);
      }

      List<String> top = temp.suggest(currWord.toString());
      if (top.size() == 3) {
        for (int i = 0; i < 3; i++) {
          guesses[i] = top.get(i);
        }
      }
      return guesses;
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
        return;
      } else if (isCorrectGuess) {
        goodWords.add(correctWord);
      } else {
        badWords.add(correctWord);
      }
    }
}
