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
         for (int i = 0; i < 3; i++) {
            if (root.weight > top3.get(i).weight) {
               list.set(i, curr.toString());
               top3.set(i, root);
               break;
            }
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

   // iterates through the letters of the given word and adds 1 to the weight of the last letter in the word
   public void addWeight(String word) {
      System.out.println("Entered addWeight...");
      TrieNode currentNode = root;
      // for each loop to itereate through the tree to get to assign current node the last letter of the word
      for (char c : word.toCharArray()) {
         currentNode = currentNode.children.get(c);
      }

      // if statement that checks if weight is not 0, if it not 0 then we know it is the end of a word, thus we can add 1 to that weight
      if (currentNode.weight != 0) {
         currentNode.weight += 1;
         System.out.println("Current Node: " + currentNode.value + " Current Node weight: " + currentNode.weight );
      }
   }
}