package com.ola.boggle;

import java.util.Comparator;

/**
 * Created by ola on 21.08.16.
 */
public class BoardSortingComparator implements Comparator<Integer> {

    private String board;
    private StringComparator comparator;

    public BoardSortingComparator(String board) {
        comparator = new StringComparator();
        this.board = board;
    }

    /**
     * @param i1 has to be in the range 0...board.len-1
     * @param i2 has to be in the range 0...board.len-1
     */
    @Override
    public int compare(Integer i1, Integer i2) {
        return comparator.compare(String.valueOf(board.charAt(i1)), String.valueOf(board.charAt(i2)));
    }
}
