public class Test {
   public static void main(String[] args) {
      Trie testTrie = new Trie();
      testTrie.root.insert("pedro");
      testTrie.root.insert("pedra");
      testTrie.root.insert("petro");
      testTrie.root.insert("Case");
      testTrie.root.insert("xablau");
      
      System.out.println(testTrie.find("xablau"));
      System.out.println(testTrie.find("Case"));
      System.out.println(testTrie.find("jonb"));

      testTrie.suggest("asd");
      


   }
}
