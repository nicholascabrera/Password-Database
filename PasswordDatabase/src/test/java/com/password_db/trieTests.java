package com.password_db;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;

import com.password_db.trie.Trie;

public class trieTests {
    private Trie createExampleTrie(){
        Trie trie = new Trie();

        trie.insert("Programming");
        trie.insert("is");
        trie.insert("a");
        trie.insert("way");
        trie.insert("of");
        trie.insert("life");

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

        ArrayList<String> o = new ArrayList<String>();
        o.add("Programming");
        o.add("is");
        o.add("a");
        o.add("way");
        o.add("of");
        o.add("life");

        boolean l = (trie.getWordsBeginningWith().containsAll(o));

        assertTrue(l);
    }
}