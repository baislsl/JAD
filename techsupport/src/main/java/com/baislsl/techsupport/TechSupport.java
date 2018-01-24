package com.baislsl.techsupport;

import java.util.*;
import java.util.stream.Collectors;

public class TechSupport {
    private QuestionDictionary dictionary;

    public TechSupport(QuestionDictionary dictionary) {
        this.dictionary = dictionary;
    }

    Question analyze(String input) {
        List<String> str = Arrays.stream(input.split("\\s+"))
                .map(PorterStemmer::stemmer)
                .filter(dictionary::isKeyWord)
                .collect(Collectors.toList());

        Map<Question, Long> correlation = dictionary.getQuestionList().stream()
                .collect(Collectors.toMap(
                        p -> p,             // key
                        p -> str.stream()   // value
                                .filter(s -> dictionary.getMap().containsKey(s))
                                .filter(s -> dictionary.getMap().get(s).contains(p))
                                .count()
                ));
        return Collections.max(correlation.entrySet(), Comparator.comparingLong(Map.Entry<Question, Long>::getValue))
                .getKey();
    }

}


