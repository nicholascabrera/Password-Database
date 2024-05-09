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

    public boolean findWord(String word){
        TrieNode current = this.root;

        for (char c : word.toCharArray()){
            if(!current.getChildren().containsKey(c)){
                return false;
            }
            current = current.getChildren().get(c);
        }

        return current.getisWord();
    }

    public void deleteWord(String word){
        deleteWord(this.root, word, 0);
    }

    private boolean deleteWord(TrieNode current, String word, int index){
        if (index == word.length()) {
            if (!current.getisWord()) {
                return false;
            }
            current.setWord(false);
            return current.getChildren().isEmpty();
        }

        char c = word.charAt(index);
        TrieNode node = current.getChildren().get(c);

        if (node == null) {
            return false;
        }

        boolean shouldDeleteCurrentNode = deleteWord(node, word, index + 1) && !node.getisWord();
    
        if (shouldDeleteCurrentNode) {
            current.getChildren().remove(c);
            return current.getChildren().isEmpty();
        }

        return false;
    }
}