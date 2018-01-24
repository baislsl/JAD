package com.baislsl.techsupport;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.function.Predicate;

public class QuestionDictionary {
    private final static Logger log = LoggerFactory.getLogger(QuestionDictionary.class);
    private final static int STOPWORD_DIV = 3;

    private StopWord stopWords = new StopWord();
    private Map<String, Set<Question>> map = new HashMap<>();
    private List<Question> questionList = new ArrayList<>();

    private QuestionDictionary() {
    }

    public static QuestionDictionary build(Reader reader) throws IOException, ParseException {
        QuestionDictionary d = new QuestionDictionary();
        d.questionList = readin(reader);
        d.map = buildMap(d.questionList, d::isKeyWord);
        d.stopWords = buildStopWord(d.map, d.questionList.size() / STOPWORD_DIV);
        return d;
    }

    private static List<Question> readin(Reader reader) throws IOException, ParseException {
        List<Question> questionList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONObject root = (JSONObject) parser.parse(reader);

        for (Object o1 : (JSONArray) root.get("data")) {
            JSONObject article = (JSONObject) o1;
            for (Object o2 : (JSONArray) article.get("paragraphs")) {
                JSONObject paragraph = (JSONObject) o2;
                for (Object o3 : (JSONArray) paragraph.get("qas")) {
                    JSONObject question = (JSONObject) o3;
                    Question q = new Question();
                    JSONObject answer = (JSONObject) ((JSONArray) (question.get("answers"))).get(0);
                    q.answer = (String) answer.get("text");  // 为了简单考虑， 选择第一个答案作为最终答案
                    q.title = (String) question.get("question");
                    questionList.add(q);
                }

            }
        }

        return questionList;
    }

    private static StopWord buildStopWord(Map<String, Set<Question>> map, int count) {
        StopWord stopWord = new StopWord();
        for (Map.Entry<String, Set<Question>> e : map.entrySet()) {
            if (e.getValue().size() > count / STOPWORD_DIV)
                stopWord.add(e.getKey());
        }
        return stopWord;
    }

    private static Map<String, Set<Question>> buildMap(List<Question> questionList, Predicate<? super String> keyWordPredicate) {
        Map<String, Set<Question>> map = new HashMap<>();
        for (Question question : questionList) {
            Arrays.stream(question.title.split("\\s+"))
                    .map(PorterStemmer::stemmer)
                    .filter(keyWordPredicate)
                    .forEach(e -> {
                        map.computeIfAbsent(e, (k) -> new HashSet<>())
                                .add(question);
                    });
        }
        return map;
    }


    public boolean isKeyWord(String word) {
        return !isStopWord(word);
    }

    public boolean isStopWord(String word) {
        return stopWords.contains(PorterStemmer.stemmer(word));
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public Map<String, Set<Question>> getMap() {
        return map;
    }
}
