import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class Trie {
 
   public class Node {
      Map<Character, Node> children;
      char valU; // character of word value
      int weight; // how many times used in old txt 

      // Node constructor 
      public Node(char c) {
         this.valU = c;
         children = new HashMap<>();
         this.weight = 0;
      }

      // 2nd constructor 
      public Node() {
         children = new HashMap<>();
         this.weight = 0;
      }

      // inserts word to the trie 
      public void insert(String word) {
         if (word == null || word.isEmpty()) // if there is no word return
            return;
         // else get first character from the word 
         char first = word.charAt(0);
         Node child = children.get(first);
         if (child == null) {
            child = new Node(first);
            children.put(first, child);
         }
         // checks if length is greater than 1 
         if (word.length() > 1) {
            // if so then recurs through 
            child.insert(word.substring(1));
         } else {
            // if not this is the end of the word and ends method
            child.weight += 1;
         }
      }

      /**
       * This method adds word to trie
       * @param word the current word to be added 
      */
      public void addWord(String word) {
         if (word == null || word.isEmpty())
            return;
         char first = word.charAt(0);
         Node child = children.get(first);
         if (child == null) {
            child = new Node(first);
            children.put(first, child);
         }
         // checks length greater than 1 if so recurses through moving through each char 
         if (word.length() > 1) {
            child.insert(word.substring(1));
         } else {
            child.weight += 2;
         }
      }
   }

   Node root;

   // constructor "Trie"
   public Trie() {
      root = new Node();
   }

   /**
    * This finds the words in the trie
    * @param prefix     The current letter of word you are trying to find
    * @param exact      used for searching exact prefix
    * @return
    */
   public boolean find(String prefix, boolean exact) {
      Node lastNode = root;
      for (char c : prefix.toCharArray()) {
         lastNode = lastNode.children.get(c);
         if (lastNode == null) { return false; }
      }
      return (!exact || lastNode.weight > 0);
   }

   public boolean find(String prefix) {
      return find(prefix, false);
   }

   /**
    * This method helps the guess method to suggest top 3 guesses 
    * @param root   root of trie
    * @param list   list of suggested words of current prefix
    * @param curr   current guess
    * @param top3   top 3 guesses
    */
   public void guessAssist(Node root, List<String> list, StringBuffer curr, List<Node> top3) {
      // keeps track of top 3 guess 
      if (root.weight > 0 && top3.size() < 3) {
         list.add(curr.toString());
         top3.add(root);
      // updates top 3 guesses
      } else if (root.weight > 0) {
         int[] dif = new int[3];
         for (int i = 0; i < 3; i++) {
            dif[i] = root.weight - top3.get(i).weight;
         }
         // look for better guess
         int index = -1;
         for (int i = 0; i < 3; i++) {
            if (dif[i] > 0 && index == -1) {
               index = i;
            } else if (dif[i] > 0 && dif[index] < dif[i]) {
               index = i;
            }
         }
         // checks if end of the word
         if (index != -1) {
            list.set(index, curr.toString());
            top3.set(index, root);
         }
      }

      // check if curr node children is empty 
      if (root.children == null || root.children.isEmpty())
         return;

      // recursion
      for (Node child : root.children.values()) {
         guessAssist(child, list, curr.append(child.valU), top3);
         curr.setLength(curr.length() - 1);
      }
   }

   // gets the best three guesses 
   public List<String> guess(String prefix) {
      List<String> list = new ArrayList<>();
      Node lastNode = root;
      StringBuffer curr = new StringBuffer();
      for (char c : prefix.toCharArray()) {
         lastNode = lastNode.children.get(c);
         if (lastNode == null)
               return list;
         curr.append(c);
      }
      // takes top three guesses and saves them into a list 
      // guesss the best three guesses and sends them
      List<Node> top3 = new ArrayList<>();
      guessAssist(lastNode, list, curr, top3);
      return list;
   }
}