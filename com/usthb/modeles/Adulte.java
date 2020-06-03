package com.usthb.modeles;

import java.util.Date;
import java.util.LinkedList;

public class Adulte extends Joueur {
    private static final long serialVersionUID = 1L;

    public Adulte(String f, String s, Date d, String p) {
        super(f, s, d, p);
    }

    @Override
    public LinkedList<Question> getQuestions(LinkedList<Question> questionsList) {
        LinkedList<Question> questions = new  LinkedList<Question>();
        for (int i = 0; i < questionsList.size(); i++) {
            Question question = questionsList.get(i);
            if(question instanceof QuestionAdulte) questions.add(question);
        }
        return questions;
    }
}