package com.ola.boggle;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

/**
 * Created by ola on 21.08.16.
 */
public class SolutionTest {

    private Solution solution;
    private String path = "src/main/res/raw/dictionary";

    @Before
    public void before() throws FileNotFoundException {
        File dictionary = new File(path);
        solution = new Solution(new FileInputStream(dictionary));
    }

    @Test
    public void test1() throws IOException {
        String board = "ićronepagatrałbo";

        String[] expectedSubset = {"operator", "błaganie", "proteina", "protega", "pataren",
                "aerator", "aborter", "negator", "operat", "orator", "arenga", "bagnet", "rotang",
                "pretor", "tarpan", "traper", "parter", "torpor", "patena", "panier", "patera",
                "tanieć", "opart", "opera", "areał", "arena", "baner", "raper", "ratan", "rapeć",
                "retro", "retor", "borta", "regał", "apage", "renga", "teina", "trapa", "parob",
                "łapeć", "torba", "panga", "pater", "pager", "tanga", "traper", "tabor", "apret",
                "apate", "abort", "agnat", "agapa", "agape", "ganić", "gater", "gabro", "nerpa",
                "napar", "opar", "opat", "opał", "open", "aren", "bałt", "ropa", "rota", "brat",
                "arba", "roba", "rata", "rapt", "rapa", "bora", "bort", "tera", "pora", "pani",
                "pała", "pera", "tani", "perć", "pean", "atar", "etan", "etap", "enat", "agat",
                "gnić", "gnet", "gnat", "getr", "geta", "gapa", "nera", "neta", "napa", "naga",
                "gała", "ort", "oba", "ban", "bat", "rap", "bot", "rep", "ret", "rea", "bor", "tab",
                "pen", "tag", "ani", "era", "ate", "abo", "eta", "aga", "nić", "nie", "gen", "gan"};

        testTemplate(board, expectedSubset);
    }

    @Test
    public void test2() throws IOException {
        String board = "łośiimcnoitópwia";

        String[] expectedSubset = {"miłośnictwo", "powiat", "mości", "pomoc", "pitia", "nici",
                "wici", "twoi", "tico", "oiom", "pita", "piwo", "nic", "śmo", "coś", "com", "wic",
                "wio", "cip", "moc", "mit", "łoś", "moi", "mop", "łom", "omo", "pic", "iii"};

        testTemplate(board, expectedSubset);
    }


    private void testTemplate(String board, String[] expectedSubset) throws IOException {
        Set<String> result = solution.findAllWords(board);

        for(String s : expectedSubset) {
            assert result.contains(s) : "Word " + s + " not found";
        }

        InputStream inputStream = new FileInputStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while((line = reader.readLine()) != null) {
            if(result.contains(line)) {
                result.remove(line);
            }
        }

        if(result.size() != 0) {
            assert false: "Words not from dictionary were found: ";
            for(String s : result) {
                System.out.println(s);
            }
        }
    }
}