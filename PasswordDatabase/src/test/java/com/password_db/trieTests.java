package com.password_db;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.password_db.trie.Trie;

public class trieTests {
    @Test
    public void givenATrie_WhenAddingElements_ThenTrieNotEmpty() {
        Trie trie = new Trie();
        trie = trie.createTrie();

        assertFalse(trie.isEmpty());
    }
}