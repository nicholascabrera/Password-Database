package com.password_db.trie;

import java.util.ArrayList;
import java.util.HashMap;

public class Trie {
    private TrieNode root;
    private ArrayList<String> wordsBeginningWith;

    public Trie(){
        this.root = new TrieNode("", false);
        this.wordsBeginningWith = new ArrayList<>();
    }

    public Trie(TrieNode root){
        this.root = root;
        this.wordsBeginningWith = new ArrayList<>();
    }

    public TrieNode getRoot() {
        return root;
    }

    public void setRoot(TrieNode root) {
        this.root = root;
    }

    public ArrayList<String> getWordsBeginningWith() {
        return wordsBeginningWith;
    }

    public void setWordsBeginningWith(ArrayList<String> wordsBeginningWith) {
        this.wordsBeginningWith = wordsBeginningWith;
    }

    public boolean isEmpty(){
        return root.getChildren().isEmpty();
    }

    public void insert(String word){
        TrieNode current = this.root;

        for (char c : word.toCharArray()){
            if (!current.getChildren().containsKey(c)){
                TrieNode node = new TrieNode(Character.toString(c));
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

    public ArrayList<String> findWordsBeginningWith(String word){
        TrieNode current = this.root;
        ArrayList<String> wordsBeginningWith = new ArrayList<String>();

        /* traverse through the trie until the last character is found */
        for (char c : word.toCharArray()){
            if(!current.getChildren().containsKey(c)){
                /* this means that the searched word isn't in the trie */
                return wordsBeginningWith;
            }
            current = current.getChildren().get(c);
        }

        /* if all the characters in the searched word are found, then the searched word exists within the trie. */
        /* if there are no children, only the searched word is returned */
        if (current.getChildren().isEmpty()){
            wordsBeginningWith.add(word);
            return wordsBeginningWith;
        }

        /* we must find all the complete child words that continue from the current node */
        /* then we add those words to a list, and return the list */
        /* a depth first preorder traversal will be used */

        if(word.length() == 0){
            traversePreOrder(word, current);
            return this.getWordsBeginningWith();
        }

        traversePreOrder(word.substring(0, word.length()-1), current);
        return this.getWordsBeginningWith();
    }

    public void traversePreOrder(String order, TrieNode node) {
        if (node.getisWord()){
            this.wordsBeginningWith.add(order.concat(node.getContent()));
        }
        if (node != null) {
            order += node.getContent();
            for (HashMap.Entry<Character, TrieNode> entry : node.getChildren().entrySet()){
                traversePreOrder(order, entry.getValue());
            }
        }
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