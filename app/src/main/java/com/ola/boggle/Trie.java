package com.ola.boggle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ola on 17.08.16.
 */
public class Trie {

    private TrieNode root;

    public Trie() {
        root = new TrieNode('#');
    }

    /**
     * @param s cannot be null or empty
     */
    public void add(String s) {
        TrieNode node = root;
        int position = 0;
        while(position < s.length()) {
            char c = s.charAt(position);
            TrieNode next = node.getChild(c);
            if(next == null) {
                next = node.addChild(c);
            }
            node = next;
            position++;
        }
        node.setTerminal(true);
    }

    /**
     * @return:
     * 0 if there is no given path in this trie
     * 1 if given path exists, but does not end with terminal
     * 2 if given path exists and ends with terminal
     */
    public int contains(String s) {
        TrieNode node = root;
        int position = 0;
        while(position < s.length()) {
            char c = s.charAt(position);
            TrieNode next = node.getChild(c);
            if(next == null) {
                return 0;
            }
            node = next;
            position++;
        }
        return node.terminal ? 2 : 1;
    }

    private static class TrieNode {
        private char letter;
        private boolean terminal;
        private List<TrieNode> children;

        private TrieNode(char letter) {
            this.letter = letter;
            terminal = false;
            children = new ArrayList<>();
        }

        private void setTerminal(boolean terminal) {
            this.terminal = terminal;
        }

        /**
         * @return child with given letter if such exists, null otherwise
         */
        private TrieNode getChild(char c) {
            for(TrieNode child : children) {
                if(child.letter == c)
                    return child;
            }
            return null;
        }

        /**
         * Assumption: there is no child with such letter
         * @return created child
         */
        private TrieNode addChild(char c) {
            TrieNode newChild = new TrieNode(c);
            children.add(newChild);
            return newChild;
        }
    }
}
