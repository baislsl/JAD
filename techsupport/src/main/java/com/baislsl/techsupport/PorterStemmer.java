package com.baislsl.techsupport;

import org.tartarus.martin.Stemmer;

public class PorterStemmer {

    public static String stemmer(String s){
        Stemmer stemmer = new Stemmer();
        stemmer.add(s.toCharArray(), s.length());
        stemmer.stem();
        return stemmer.toString();
    }
}

