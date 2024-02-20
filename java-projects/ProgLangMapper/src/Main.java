

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        ProgLang pl = null;
        try {
            pl =  new ProgLang(System.getProperty("user.home") + "/Programmers.tsv");
        } catch (Exception exc) {
            System.out.println("Faulty constructor: " + exc);
        }
        System.out.println("@1 Language Map:");
        pl.getLangsMap().forEach((k,v)->System.out.println(k+ " = " + v));
        System.out.println("@2 Programmers Map:");
        pl.getProgsMap().forEach((k,v)->System.out.println(k+ " = " + v));
        System.out.println("@3 Languages sorted by the number of programmers:");
        pl.getLangsMapSortedByNumOfProgs()
                .forEach((k,v)->System.out.println(k+ " = " + v));
        System.out.println("@4 Programmers sorted by number of languages:");
        pl.getProgsMapSortedByNumOfLangs()
                .forEach((k,v)->System.out.println(k+ " = " + v));
        System.out.println("@5 Original language map unchanged:");
        pl.getLangsMap().forEach((k,v)->System.out.println(k+ " = " + v));
        System.out.println("@6 Original programmers map unchanged:");
        pl.getProgsMap().forEach((k,v)->System.out.println(k+ " = " + v));
        System.out.println("@7 Map of programmers knowing more than 1 language:");
        pl.getProgsMapForNumOfLangsGreaterThan(1)
                .forEach((k,v)->System.out.println(k+ " = " + v));
        System.out.println("@8 The original developer map not altered:");
        pl.getProgsMap().forEach((k,v)->System.out.println(k+ " = " + v));
    }

}
