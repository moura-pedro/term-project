import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class Trie {
 
   public class TrieNode {
      Map<Character, TrieNode> children;
      char c;
      boolean isWord;
      

      public TrieNode(char c) {
         this.c = c;
         children = new HashMap<>();
      }

      public TrieNode() {
         children = new HashMap<>();
      }

      public void insert(String word) {
         if (word == null || word.isEmpty()) {
            return;
         }
         char firstChar = word.charAt(0);
         TrieNode child = children.get(firstChar);
         if (child == null) {
            child = new TrieNode(firstChar);
            children.put(firstChar, child);
         }

         if (word.length() > 1) {
            child.insert(word.substring(1));
         }
         else {
            child.isWord = true;
         }
      }

   }

   TrieNode root;

   public Trie() {
      root = new TrieNode();
   }

   public Trie(List<String> words) {
      root = new TrieNode();
      for (String word : words) {
         root.insert(word);
      }
   }

   public boolean find(String prefix, boolean exact) {
      TrieNode lastNode = root;
      for (char c : prefix.toCharArray()) {
         lastNode = lastNode.children.get(c);
         if (lastNode == null) { return false; }
      }
      return (!exact || lastNode.isWord);
   }

   public boolean find(String prefix) {
      return find(prefix, false);
   }

   public void suggestHelper(TrieNode root, List<String> list, StringBuffer curr) {
      if (root.isWord) {
         list.add(curr.toString());
      }

      if (root.children == null || root.children.isEmpty())
         return;

      for (TrieNode child : root.children.values()) {
         suggestHelper(child, list, curr.append(child.c));
         curr.setLength(curr.length() - 1);
      }
   }

   public List<String> suggest(String prefix) {
      List<String> list = new ArrayList<>();
      TrieNode lastNode = root;
      StringBuffer curr = new StringBuffer();
      for (char c : prefix.toCharArray()) {
         lastNode = lastNode.children.get(c);
         if (lastNode == null)
               return list;
         curr.append(c);
      }
      suggestHelper(lastNode, list, curr);
      return list;
   }

   // iterates through the letters of the given word and adds 1 to the weight of the last letter in the word
   public void addWeight(String word) {
      TrieNode currentNode = root;
      for (char c : word.toCharArray()) {
         currentNode = currentNode.children.get(c);
         // if statement that checks if weight is not 0, if it not 0 then we know it is the end of a word, thus we can add 1 to that weight
         if (!currentNode.weight.equals(0)) {
            currentNode.weight += 1;
         }
      }
   }
}