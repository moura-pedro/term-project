/*
  Authors (group members): Jonathan Bailey, Pedro Moura, Jordan Synodis, Michael Richards
  Email addresses of group members: pmoura2020@my.fit.edu, Jbailey2021@my.fit.edu, jsynodis2021@my.fit.edu, mrichards2021@my.fit.edu
  Group name: 12JPMJ

  Course: CSE 2010
  Section: 1

  Description of the overall algorithm: This program takes in the user input and attempts to guess the rest of the word by providing 3
                                        guesses based on the already typed letters
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class Trie {
 
   public class TrieNode {
      Map<Character, TrieNode> children;
      char value;
      int weight;

      public TrieNode(char c) {
         this.value = c;
         children = new HashMap<>();
         this.weight = 0;
      }

      public TrieNode() {
         children = new HashMap<>();
         this.weight = 0;
      }

      public void insert(String word) {
         if (word == null || word.isEmpty())
            return;
         char first = word.charAt(0);
         TrieNode child = children.get(first);
         if (child == null) {
            child = new TrieNode(first);
            children.put(first, child);
         }

         if (word.length() > 1) {
            child.insert(word.substring(1));
         } else {
            child.weight += 1;
         }
      }

      public void addWord(String word) {
         if (word == null || word.isEmpty())
            return;
         char first = word.charAt(0);
         TrieNode child = children.get(first);
         if (child == null) {
            child = new TrieNode(first);
            children.put(first, child);
         }

         if (word.length() > 1) {
            child.insert(word.substring(1));
         } else {
            child.weight += 2;
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
      return (!exact || lastNode.weight > 0);
   }

   public boolean find(String prefix) {
      return find(prefix, false);
   }

   public void suggestHelper(TrieNode root, List<String> list, StringBuffer curr, List<TrieNode> top3) {
      if (root.weight > 0 && top3.size() < 3) {
         list.add(curr.toString());
         top3.add(root);
      } else if (root.weight > 0) {
         int[] dif = new int[3];
         for (int i = 0; i < 3; i++) {
            dif[i] = root.weight - top3.get(i).weight;
         }
         int index = -1;
         for (int i = 0; i < 3; i++) {
            if (dif[i] > 0 && index == -1) {
               index = i;
            } else if (dif[i] > 0 && dif[index] < dif[i]) {
               index = i;
            }
         }
         if (index != -1) {
            list.set(index, curr.toString());
            top3.set(index, root);
         }
      }

      if (root.children == null || root.children.isEmpty())
         return;

      for (TrieNode child : root.children.values()) {
         suggestHelper(child, list, curr.append(child.value), top3);
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
      List<TrieNode> top3 = new ArrayList<>();
      suggestHelper(lastNode, list, curr, top3);
      return list;
   }
}
