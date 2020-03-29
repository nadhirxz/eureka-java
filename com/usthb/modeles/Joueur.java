package EUREKA.com.usthb.modeles;

import java.util.Date;
import java.util.HashMap;

public class Joueur {
    static int n = 0;
    int num;
    String fname;
    String lname;
    Date date;
    String password;
    public HashMap<String, Integer> lastLevel = new HashMap<String, Integer>(); // this might change idk
    public static int[] coef = new int[] {1,1,1,1,1};
    public Joueur(String f, String s, Date d, String p) {
        this.num = Joueur.n;
        this.fname = f;
        this.lname = s;
        this.date = d;
        this.password = p;
        initLastLevel();
        n++;
    }

    public int getTotalScore() {
        int score = 0;
        for (String key : this.lastLevel.keySet()) {
            int i = 0;
            switch (key) {
                case "HIS": i=0; break;
                case "GEO": i=1; break;
                case "SAN": i=2; break;
                case "CUL": i=3; break;
                case "ISL": i=4; break;
            }
            if (this.lastLevel.get(key) > 0) score += 5*coef[i];
            if (this.lastLevel.get(key) > 1) score += 10*coef[i];
            if (this.lastLevel.get(key) > 2) score += 18*coef[i];
            if (this.lastLevel.get(key) > 3) score += 28*coef[i];
            if (this.lastLevel.get(key) > 4) score += 40*coef[i];
        }
        return score;
    }

    public String getFName() {
        return this.fname;
    }

    public String getLName() {
        return this.lname;
    }

    public String getPassword() {
        return this.password;
    }

    public void initLastLevel() {
        lastLevel.put("HIS", 0);
        lastLevel.put("GEO", 0);
        lastLevel.put("SAN", 0);
        lastLevel.put("CUL", 0);
        lastLevel.put("ISL", 0);
    }

    public int getLastLevel(String type) {
        return this.lastLevel.get(type);
    }

    public int getNum() {
        return this.num;
    }

    public Date getDate() {
        return this.date;
    }

    public void increaseLevel(String type) {
        if (this.lastLevel.get(type) < 5)
            this.lastLevel.put(type, getLastLevel(type) + 1);
    }

    public void resetLevel(String type) {
        this.lastLevel.put(type, 0);
    }

    public void setData(String f, String l, String p, Date d) {
        this.fname = f;
        this.lname = l;
        this.password = p;
        this.date = d;
    }
}