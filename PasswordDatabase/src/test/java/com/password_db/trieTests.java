package com.password_db;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;

import com.password_db.gui.Record;
import com.password_db.trie.Trie;

public class trieTests {
    private Trie createExampleTrie(){
        Trie trie = new Trie();

        trie.insert("Programming", new Record());
        trie.insert("is", new Record());
        trie.insert("a", new Record());
        trie.insert("way", new Record());
        trie.insert("of", new Record());
        trie.insert("life", new Record());

        return trie;
    }

    @Test
    public void givenATrie_WhenAddingElements_ThenTrieNotEmpty() {
        Trie trie = createExampleTrie();

        assertFalse(trie.isEmpty());
    }

    @Test
    public void givenATrie_WhenAddingElements_ThenTrieContainsThoseElements() {
        Trie trie = createExampleTrie();

        assertFalse(trie.findWord("3"));
        assertFalse(trie.findWord("vida"));
        assertTrue(trie.findWord("life"));
    }

    @Test
    public void whenDeletingElements_ThenTreeDoesNotContainThoseElements() {
        Trie trie = createExampleTrie();

        assertTrue(trie.findWord("Programming"));
    
        trie.deleteWord("Programming");
        assertFalse(trie.findWord("Programming"));
    }

    @Test
    public void whenTraversing_TraverseInCorrectOrder() {
        Trie trie = createExampleTrie();

        trie.traversePreOrder("",trie.getRoot());

        ArrayList<String> a = new ArrayList<String>();
        a.add("Programming");
        a.add("is");
        a.add("a");
        a.add("way");
        a.add("of");
        a.add("life");

        assertTrue(trie.getWordsBeginningWith().containsAll(a));
    }

    @Test
    public void whenFindingWordsThatBeginAtTheRoot(){
        Trie trie = createExampleTrie();

        ArrayList<String> a = new ArrayList<String>();
        a.add("Programming");
        a.add("is");
        a.add("a");
        a.add("way");
        a.add("of");
        a.add("life");

        ArrayList<String> b = trie.findWordsBeginningWith("");

        assertTrue(a.containsAll(b) && a.size() == b.size());
    }

    @Test
    public void whenFindingWordsThatBeginWithTheLetterP(){
        Trie trie = createExampleTrie();

        ArrayList<String> a = new ArrayList<String>();
        a.add("Programming");

        ArrayList<String> b = trie.findWordsBeginningWith("P");

        assertTrue(a.containsAll(b) && a.size() == b.size());
    }

    @Test
    public void whenFindingWordsThatBeginWithTheLetterP_BeforeAndAfterDeletingTheWordProgramming(){
        Trie trie = createExampleTrie();

        ArrayList<String> a = new ArrayList<String>();
        a.add("Programming");

        ArrayList<String> b = trie.findWordsBeginningWith("P");

        assertTrue(a.containsAll(b) && a.size() == b.size());

        trie.deleteWord("Programming");

        b = trie.findWordsBeginningWith("P");

        assertFalse(a.containsAll(b) && a.size() == b.size());
    }

    @Test
    public void whenFindingWordsThatBeginWithProgram_AfterAddingTheWordProgrammatically(){
        Trie trie = createExampleTrie();
        trie.insert("Programmatically", new Record());

        ArrayList<String> a = new ArrayList<String>();
        a.add("Programming");
        a.add("Programmatically");

        ArrayList<String> b = trie.findWordsBeginningWith("Pro");

        assertTrue(a.containsAll(b) && a.size() == b.size());
    }

    @Test
    public void whenFindingWordsThatBeginWithProgram_AfterAddingTheWordProgrammatically_DoesTheTrieRemainTheSame(){
        Trie trie = createExampleTrie();
        Trie trie2 = createExampleTrie();
        trie.insert("Programmatically", new Record());
        trie2.insert("Programmatically", new Record());

        assertTrue(trie.equals(trie2));

        ArrayList<String> a = new ArrayList<String>();
        a.add("Programming");
        a.add("Programmatically");
        
        assertTrue(trie.equals(trie2));

        @SuppressWarnings("unused")
        ArrayList<String> b = trie.findWordsBeginningWith("Pro");

        assertTrue(trie.equals(trie2));
    }
}