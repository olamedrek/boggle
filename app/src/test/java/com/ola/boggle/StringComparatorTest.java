package com.ola.boggle;

import org.junit.Test;

/**
 * Created by ola on 19.08.16.
 */
public class StringComparatorTest {

    private StringComparator comparator = new StringComparator();

    @Test
    public void shouldCompareCharactersCorrectly() {

        String[] polishChars = {"a", "ą", "b", "c", "ć", "d", "e", "ę", "f", "g", "h", "i",
                "j", "k", "l", "ł", "m", "n", "ń", "o", "ó", "p", "q", "r", "s", "ś", "t", "u",
                "v", "w", "x", "y", "z", "ź", "ż"};

        for(int i = 0; i < polishChars.length; i++) {
            for(int j = i + 1; j < polishChars.length; j++) {
                String char1 = polishChars[i];
                String char2 = polishChars[j];
                assert (comparator.compare(char1, char2) < 0) : char1 + ">" + char2;
            }
        }
    }

    @Test
    public void shouldCompareWordsCorrectly() {

        String[] words = {"a", "aa", "ąąą", "ąbb", "cło", "ćło", "ćłóo", "e", "ęe", "ęę", "głuchy",
                "lama", "ławka", "łyko", "qa", "zły", "żyła"};

        for(int i = 0; i < words.length; i++) {
            for(int j = i + 1; j < words.length; j++) {
                String s1 = words[i];
                String s2 = words[j];
                assert (comparator.compare(s1, s2) < 0) : s1 + ">" + s2;
            }
        }

    }
}