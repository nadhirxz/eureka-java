package com.usthb.modeles;

public class Question {
    String libelle;
    static int HIS = 0;
    static int GEO = 0;
    static int SAN = 0;
    static int CUL = 0;
    static int ISL = 0;
    String num;
    String image = "";
    String reponse;
    int niveau;

    public Question(String libelle, String type, String reponse, int niveau, String image) {
        this.libelle = libelle;
        this.reponse = reponse;
        this.niveau = niveau;
        this.image = image;
        int n = 0;
        switch (type) {
            case "HIS": HIS++; n=HIS; break;
            case "GEO": GEO++; n=GEO; break;
            case "SAN": SAN++; n=SAN; break;
            case "CUL": CUL++; n=CUL; break;
            case "ISL": ISL++; n=ISL; break;
        }
        this.num = type+n;
    }

    public int getNBPoints() {
        if (this.niveau==1) return 5;
        if (this.niveau==2) return 10;
        if (this.niveau==3) return 18;
        if (this.niveau==4) return 28;
        return 40;
    }

    public String getNumQuest() {
        return this.num;
    }

    public String getText() {
        return this.libelle;
    }

    public String getReponse() {
        return this.reponse;
    }

    public String getImage() {
        return this.image;
    }
    
    public static void resetIntegers() {
        Question.HIS = 0;
        Question.GEO = 0;
        Question.SAN = 0;
        Question.CUL = 0;
        Question.ISL = 0;
    }
}