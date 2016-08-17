package com.ola.boggle;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ola on 19.08.16.
 */

/**
 * Only for lowercase strings.
 */
public class StringComparator implements Comparator<String> {

    private Character[] polishChars = {'a', 'ą', 'b', 'c', 'ć', 'd', 'e', 'ę', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'ł', 'm', 'n', 'ń', 'o', 'ó', 'p', 'q', 'r', 's', 'ś', 't', 'u',
            'v', 'w', 'x', 'y', 'z', 'ź', 'ż'};
    
    private Map<Character, Integer> rank = new HashMap<>();

    public StringComparator() {
        for(int i = 0; i < polishChars.length; i++) {
            rank.put(polishChars[i], i);
        }
    }

    @Override
    public int compare(String s, String t) {
        int lenMin = Math.min(s.length(), t.length());
        for(int i = 0; i < lenMin; i++) {
            int compRes = rank.get(s.charAt(i)) - rank.get(t.charAt(i));
            if(compRes != 0)
                return compRes;
        }
        return s.length() - t.length();
    }
}
