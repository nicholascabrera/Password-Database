package com.password_db;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

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
}