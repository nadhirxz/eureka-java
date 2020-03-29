package EUREKA.com.usthb.modeles;

import java.util.LinkedList;

public class ThemeJeu {
    String type;
    String label;
    int coef;
    LinkedList<Question> questions = new LinkedList<Question>();

    public ThemeJeu(String label, String type, int coef, LinkedList<Question> questions) {
        this.label = label;
        this.type = type;
        this.coef = coef;
        this.questions = questions;
    }

    public String getThemeText() {
        return this.label;
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
}