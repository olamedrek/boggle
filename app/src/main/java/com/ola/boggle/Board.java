package com.ola.boggle;

import java.util.Random;

/**
 * Created by ola on 11.08.16.
 */
public class Board {

    private final Character[] letters = {'A', 'Ą', 'B', 'C', 'Ć', 'D', 'E', 'Ę', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'Ł', 'M', 'N', 'Ń', 'O', 'Ó', 'P', 'Q', 'R', 'S', 'Ś', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'Ź', 'Ż'};

    private final double[] freq = {0.0891, 0.0099, 0.0147, 0.0396, 0.0040, 0.0325,
            0.0766, 0.0111, 0.0030, 0.0142, 0.0108, 0.0821, 0.0228, 0.0351, 0.0210, 0.0182,
            0.0280, 0.0552, 0.0020, 0.0775, 0.0085, 0.0313, 0.0014, 0.0469, 0.0432, 0.0066,
            0.0398, 0.0250, 0.0004, 0.0465, 0.0002, 0.0376, 0.0564, 0.0006, 0.0083};

    private double[] freqPref = new double[freq.length];

    private Random random = new Random();
    private String currentBoard;

    public Board() {
        freqPref[0] = freq[0];
        for(int i = 1; i < freq.length; i++) {
            freqPref[i] = freqPref[i-1] + freq[i];
        }
    }

    public String getNewBoard() {
        StringBuilder builder = new StringBuilder();
        while(builder.length() < 16) {
            Double d = random.nextDouble();
            for(int i = 0; i < freqPref.length; i++) {
                if(freqPref[i] > d) {
                    builder.append(letters[i]);
                    break;
                }
            }
        }
        currentBoard = builder.toString();
        return currentBoard;
    }

    public String getCurrentBoard() {
        return currentBoard;
    }
}
