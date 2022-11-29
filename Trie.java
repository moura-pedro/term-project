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

   public String[] suggestHelper(TrieNode root, TrieNode[] top3, StringBuffer curr, int stepper, String[] list) {

      if (root.weight > 0 && stepper < 3) {
         top3[stepper] = root;
         list[stepper] = curr.toString();
      } else if (root.weight > 0) {
         for (int i = 0; i < 3; i++) {
            if (root.weight > top3[i].weight) {
               top3[i] = root;
               list[i] = curr.toString();
               break;
            }
         }
      }

      if (root.children == null || root.children.isEmpty())
         return list;

      for (TrieNode child : root.children.values()) {
         suggestHelper(child, top3, curr.append(child.value), stepper + 1, list);
         curr.setLength(curr.length() - 1);
      }

      return list;
   }

   public String[] suggest(String prefix) {
      String[] list = new String[3];
      TrieNode lastNode = root;
      StringBuffer curr = new StringBuffer();
      for (char c : prefix.toCharArray()) {
         lastNode = lastNode.children.get(c);
         if (lastNode == null)
               return list;
         curr.append(c);
      }

      TrieNode[] top = new TrieNode[3];
      list = suggestHelper(lastNode, top, curr, 0, list);

      return list;
   }
}
