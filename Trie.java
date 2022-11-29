public class Trie {

// trie node
   public class Node {
      Node[] children = new Node[26];

      // isLastLetter is true if the node represents
      // end of a word
      boolean isLastLetter;
      int weight;

      public Node(){
         this.isLastLetter = false;
         weight = 0;
         for (int i = 0; i < 26; i++) {
            children[i] = null;
         }
      }
   }

   Node root;

   // If not present, inserts word into trie
   // If the word is prefix of trie node,
   // just marks leaf node
   public void insert(String word) {
      int height;
      int length = word.length();
      int index;

      Node currNode = root;

      for (height = 0; height < length; height++) {
         index = word.charAt(height) - 'a';
         if (currNode.children[index] == null) {
            currNode.children[index] = new Node();
         }
         currNode = currNode.children[index];
      }

         // mark last node as leaf
      currNode.isLastLetter = true;
   }

   // Returns true if word presents in trie, else false
   public boolean search(String word) {
      int height;
      int length = word.length();
      int index;
      Node currNode = root;

      for (height = 0; height < length; height++) {
         index = word.charAt(height) - 'a';

         if (currNode.children[index] == null) {
            return false;
         }
         currNode = currNode.children[index];
      }

      return (currNode.isLastLetter);
   }
}
