public class Test {
   public static void main(String[] args) {
      Trie testTrie = new Trie();
      testTrie.insert("pedro");
      testTrie.insert("Case");
      testTrie.insert("xablau");
      
      System.out.println(testTrie.search("xablau"));
      System.out.println(testTrie.search("Case"));
      System.out.println(testTrie.search("jonb"));
      


   }
}
