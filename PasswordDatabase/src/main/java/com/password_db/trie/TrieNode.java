package com.password_db.trie;

import java.util.HashMap;

public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private String content;
    private boolean isWord;

    public TrieNode(){
        this.children = new HashMap<Character, TrieNode>();
        this.content = "";
        this.isWord = false;
    }

    public TrieNode(String content){
        this.children = new HashMap<Character, TrieNode>();
        this.content = content;
        this.isWord = false;
    }

    public TrieNode(String content, boolean isWord){
        this.children = new HashMap<Character, TrieNode>();
        this.content = content;
        this.isWord = isWord;
    }

    public TrieNode(HashMap<Character, TrieNode> children, String content, boolean isWord){
        this.children = children;
        this.content = content;
        this.isWord = isWord;
    }

    public HashMap<Character, TrieNode> getChildren() {
        return children;
    }

    public String getContent() {
        return content;
    }

    public boolean getisWord(){
        return isWord;
    }

    public void setChildren(HashMap<Character, TrieNode> children) {
        this.children = children;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setWord(boolean isWord) {
        this.isWord = isWord;
    }

    public void addChild(Character c, TrieNode node){
        this.children.put(c, node);
    }
}
