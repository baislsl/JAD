package com.baislsl.techsupport;

import java.util.HashSet;
import java.util.Scanner;

public class StopWord extends HashSet<String> {
    public StopWord() {
        super();
        init();
    }

    /**
     * load basic stopwords lists form file
     */
    private void init() {
        Scanner scanner = new Scanner(StopWord.class.getResourceAsStream("/stopwords"));
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            this.add(line);
        }

    }
}
