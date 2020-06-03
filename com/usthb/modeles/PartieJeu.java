package com.usthb.modeles;

public class PartieJeu {
    int num;
    ThemeJeu theme;
    String numQuest;
    StringBuffer reponse; // the answer that the user is currently writing NOT the one in the question
    int score;

    public PartieJeu(ThemeJeu theme, String numQuest) {
        this.theme = theme;
        this.numQuest = numQuest;
        this.reponse = new StringBuffer("");
        this.score = 0;
    }

    public boolean checkCharacter(char c) {
        if (getQuestionReponse().toUpperCase().contains(Character.toString(c).toUpperCase())) {
            // toUpperCase()
            // java est sensible aux majuscules et minuscules
            // si on veut comparer des characteres
            // nous les transformons en majuscules ou en minuscules
            // nous utilisons des majuscules
            return true;
        } else {
            return false;
        }
    }

    public String getThemeText() {
        return this.theme.getThemeText();
    }

    public String getThemeType() {
        return this.theme.getThemeType();
    }

    public ThemeJeu getTheme() {
        return this.theme;
    }

    public String getNumQuest() {
        return this.numQuest;
    }

    public String getNextQuest() {
        String num;
        num = getNumQuest().substring(0, 3);
        int n = Character.getNumericValue(getNumQuest().charAt(3)) + 1;
        num += n;
        return num;
    }

    public String getQuestion() {
        return this.theme.getQuestion(this.numQuest).getText();
    }

    public String getQuestionReponse() {
        return this.theme.getQuestion(this.numQuest).getReponse();
    }

    public StringBuffer getReponse() {
        return this.reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = new StringBuffer(reponse);
    }

    public String getImage() {
        return this.theme.getQuestion(numQuest).getImage();
    }
}