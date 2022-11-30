/*
  Authors (group members):
  Email addresses of group members:
  Group name:
  Course:
  Section:
  Description of the overall algorithm:
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SmartWord
{
    String[] guesses = new String[3];  // 3 guesses from SmartWord

    Trie trie;

    // initialize SmartWord with a file of English words
    public SmartWord(String args){ // changed this from "String wordFile" to take in file as arg
      
    // imports input thorugh files
      //File input = new File(args); 
      Scanner sc = new Scanner(args);

      while(sc.hasNext()){
        String word = sc.nextLine();
        //String [] checkmark = checkspace.split(" ");
        //String word = checkmark[0];
        word.toLowerCase();
        trie = new Trie();
 
        if(isNumeric(word) == false){
          word.replaceAll("\\p{Punct}", "");
          trie.root.insert(word);
        }
      }
    }

   // process old messages from oldMessageFile
   public void processOldMessages(String args) // was String oldMessageFile
   {
     // imports input thorugh files
     //File input = new File(args[0]); 
     Scanner sc = new Scanner(args);

     while(sc.hasNext()){
       String checkspace = sc.nextLine();
       String [] checkmark = checkspace.split(" ");
       //String word = checkmark[0];
       //word.toLowerCase();
       for (String word : checkmark){
         word.toLowerCase();
       
          if(isNumeric(word) == false){
            System.out.println("Entered isNumeric");
            word.replaceAll("\\p{Punct}", "");

            // if the word isnt found in the tree, put it in the tree
            if (!trie.find(word)) {
              trie.root.insert(word);
            } 

            // addweight to to the word given
            trie.addWeight(word);
          }
        }
      }
    }

    // based on a letter typed in by the user, return 3 word guesses in an array
    // letter: letter typed in by the user
    // letterPosition:  position of the letter in the word, starts from 0
    // wordPosition: position of the word in a message, starts from 0
    public String[] guess(char letter,  int letterPosition, int wordPosition)
    {
	
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
    public void feedback(boolean isCorrectGuess, String correctWord)        
    {

    }

    // method used to check if input word is equal to a #
    public static boolean isNumeric(String str) { 
      try {  
        Double.parseDouble(str);  
        return true;
      } catch(NumberFormatException e){  
        return false;  
      }  
    }
    

}
