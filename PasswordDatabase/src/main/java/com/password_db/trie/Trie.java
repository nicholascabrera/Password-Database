package com.password_db.trie;

public class Trie {
    private TrieNode root;

    public Trie(){
        this.root = new TrieNode("", false);
    }

    public Trie(TrieNode root){
        this.root = root;
    }

    public TrieNode getRoot() {
        return root;
    }

    public void setRoot(TrieNode root) {
        this.root = root;
    }

    public boolean isEmpty(){
        return root.getChildren().isEmpty();
    }

    public void insert(String word){
        TrieNode current = this.root;

        for (char c : word.toCharArray()){
            if (!current.getChildren().containsKey(c)){
                TrieNode node = new TrieNode();
                current.addChild(c, node);
                current = node;
            } else {
                current = current.getChildren().get(c);
            }
        }

        current.setWord(true);
    }
}