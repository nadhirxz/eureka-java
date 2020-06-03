package com.usthb.modeles;

import java.util.LinkedList;

public class ThemeJeu {
    String type;
    String libelle;
    int coef;
    LinkedList<Question> questions = new LinkedList<Question>();

    public ThemeJeu(String libelle, int coef, LinkedList<Question> questions) {
        this.libelle = libelle;
        this.type = libelle.substring(0, 3).replace("é", "e").toUpperCase();
        this.coef = coef;
        this.questions = questions;
    }

    public String getThemeText() {
        return this.libelle;
    }

    public String getThemeType() {
        return this.type;
    }
    
    public Question getQuestion(String numQuest) {
        for (Question question : questions) {
            if (question.getNumQuest().equals(numQuest)) {
                return question;
            }
        }
        return null;
    }

    public LinkedList<Question> getAllQuestions() {
        return this.questions;
    }

    public void setQuestions(LinkedList<Question> questions) {
        this.questions = questions;
    }

}