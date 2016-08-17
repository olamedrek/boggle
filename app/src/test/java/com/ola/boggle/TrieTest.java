package com.ola.boggle;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ola on 20.08.16.
 */
public class TrieTest {

    Trie trie = new Trie();

    @Test
    public void simpleCase() {
        assertTrue(trie.contains("a") == 0);

        trie.add("ala");
        assertTrue(trie.contains("ala") == 2);
        assertTrue(trie.contains("al") == 1);
        assertTrue(trie.contains("a") == 1);
        assertTrue(trie.contains("ale") == 0);
        assertTrue(trie.contains("alaa") == 0);
        assertTrue(trie.contains("b") == 0);
    }

    @Test
    public void complicatedCase() {
        trie.add("aa");
        trie.add("ab");
        trie.add("ac");
        trie.add("ba");
        trie.add("bb");
        trie.add("bc");
        trie.add("ca");
        trie.add("cb");
        trie.add("cc");
        trie.add("aaa");
        trie.add("a");
        trie.add("ab");
        trie.add("aaa");

        assertTrue(trie.contains("a") == 2);
        assertTrue(trie.contains("aa") == 2);
        assertTrue(trie.contains("aaa") == 2);
        assertTrue(trie.contains("ab") == 2);
        assertTrue(trie.contains("ac") == 2);
        assertTrue(trie.contains("ba") == 2);
        assertTrue(trie.contains("bb") == 2);
        assertTrue(trie.contains("bc") == 2);
        assertTrue(trie.contains("ca") == 2);
        assertTrue(trie.contains("cb") == 2);
        assertTrue(trie.contains("cc") == 2);
        assertTrue(trie.contains("b") == 1);
        assertTrue(trie.contains("c") == 1);
        assertTrue(trie.contains("aaaa") == 0);
        assertTrue(trie.contains("aba") == 0);
        assertTrue(trie.contains("d") == 0);
        assertTrue(trie.contains("z") == 0);
    }

}