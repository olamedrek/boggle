package com.ola.boggle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by ola on 15.08.16.
 */
public class Solution {

    private Set<String> wordsOnBoard;
    private StringComparator stringComparator;
    private Trie trie;
    private char currLetterInTrie;
    private String firstLine;
    private BufferedReader bufferedReader;

    public Solution(InputStream inputStream) {
        stringComparator = new StringComparator();
        trie = new Trie();
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public Set<String> findAllWords(String board) {
        wordsOnBoard = new HashSet<>();
        board = board.toLowerCase();

        boolean[] visited = new boolean[16];

        Integer[] positions = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        Arrays.sort(positions, new BoardSortingComparator(board));

        for(int i = 0; i < 16; i++) {
            loadNewLetterToTrie(board.charAt(positions[i]));
            findAllWordsRecursively(board, positions[i], new StringBuilder(), visited.clone());
        }
        return wordsOnBoard;
    }

    private void loadNewLetterToTrie(char c) {
        if(c == currLetterInTrie) {
            return;
        }
        trie = new Trie();

        if(firstLine != null) {
            char first = firstLine.charAt(0);
            if(first == c) {
                trie.add(firstLine);
            }
        }

        String line;
        try {
            while((line = bufferedReader.readLine()) != null) {
                char first = line.charAt(0);
                int compRes = stringComparator.compare(String.valueOf(first), String.valueOf(c));
                if(compRes == 0) {
                    trie.add(line);
                }
                if(compRes > 0) {
                    break;
                }
            }
            firstLine = line;
        } catch (IOException e) {
            e.printStackTrace();
        }

        currLetterInTrie = c;

    }

    private void findAllWordsRecursively(String board, int position, StringBuilder builder, boolean[] visited) {
        visited[position] = true;

        builder.append(board.charAt(position));
        String currentWord = builder.toString();

        int result = trie.contains(currentWord);

        if(result == 2) {
            wordsOnBoard.add(currentWord);
        }
        if(result != 0) {
            for(int neighbour : getAllNeighbours(position)) {
                if(!visited[neighbour]) {
                    findAllWordsRecursively(board, neighbour, new StringBuilder(builder), visited.clone());
                }
            }
        }
    }

    private List<Integer> getAllNeighbours(int position) {
        List<Integer> neighbours = new LinkedList<>();
        if(position % 4 != 3) {
            neighbours.add(position + 1);
        }
        if(position % 4 != 0) {
            neighbours.add(position - 1);
        }
        if(position / 4 != 0) {
            neighbours.add(position - 4);
        }
        if(position / 4 != 3) {
            neighbours.add(position + 4);
        }
        if(position % 4 != 3 && position / 4 != 0) {
            neighbours.add(position - 4 + 1);
        }
        if(position % 4 != 0 && position / 4 != 0) {
            neighbours.add(position - 4 - 1);
        }
        if(position % 4 != 3 && position / 4 != 3) {
            neighbours.add(position + 4 + 1);
        }
        if(position % 4 != 0 && position / 4 != 3) {
            neighbours.add(position + 4 - 1);
        }
        return neighbours;
    }
}
