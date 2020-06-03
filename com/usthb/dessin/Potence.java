package com.usthb.dessin;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Component;

public class Potence extends Component {
    private static final long serialVersionUID = 1L;
    
    int etat;
    boolean trouve;
    Dimension dimension;

    public Potence() {
        this.etat = 0;
        this.trouve = false;
        dimension = new Dimension(155, 210);
        this.setPreferredSize(dimension);
        this.setBackground(Color.RED);
    }

    public void incrementEtat() {
        this.etat++;
    }

    public boolean trouve() {
        return trouve;
    }

    public int getEtat() {
        return this.etat;
    }

    public void incrementEtat(int etat) {
        this.etat = etat;
    }

    public void setTrouve(boolean trouve) {
        this.trouve = trouve;
    }

    // Dessin de la potence
    public void paint(Graphics g) {
        // Le dessin s'adapte à l'espace attribué
        // dimension = getSize(); // de Component
        g.clearRect(7, 0, dimension.width - 1, dimension.height - 1); // effacer
        g.drawRect(7, 2, dimension.width - 8, dimension.height - 3); // tracer le cadre
        // s'adapter à l'espace du composant
        int taille = 12 * (dimension.width / 120);
        if (taille < 8)
            taille = 8;
        // g.setFont(new Font("TimesRoman", Font.PLAIN, taille));
        if (etat >= 1)
            g.drawLine(l(30), h(120), l(90), h(120));
        if (etat >= 2)
            g.drawLine(l(30), h(120), l(30), h(40));
        if (etat >= 3)
            g.drawLine(l(60), h(120), l(30), h(90));
        if (etat >= 4)
            g.drawLine(l(30), h(40), l(80), h(40));
        if (etat >= 5)
            g.drawLine(l(30), h(60), l(50), h(40));
        if (etat >= 6)
            g.drawLine(l(70), h(40), l(70), h(60));
        if (etat >= 7)
            g.drawOval(l(65), h(60), l(10) + 35, h(10) + 50); // tête
        if (etat >= 8) {
            g.drawLine(l(70), h(70), l(70), h(85)); // corps
            g.drawLine(l(70), h(70), l(65), h(75)); // corps
            g.drawLine(l(70), h(70), l(75), h(75)); // corps
            g.drawLine(l(70), h(85), l(65), h(95)); // corps
            g.drawLine(l(70), h(85), l(75), h(95)); // corps
        } /*
           * if (trouve) g.drawString("Bravo! vous avez trouvé", l(10), h(150)); else if
           * (etat == 8) g.drawString("Vous êtes pendu !", l(10), h(150)); else if (etat
           * == 7) g.drawString("Reste un coup à jouer !", l(10), h(150)); else // (etat
           * >=0 && etat <7) g.drawString("Reste " + (8 - etat) + "coups à jouer", l(10),
           * h(150));
           */
    }

    // Mise à l'échelle en largeur de v
    int l(int v) {
        double k = Math.min(dimension.width / 140., dimension.height / 160);
        return (int) (v * 2 * k) - 35;
    }

    // Mise à l'échelle en hauteur de v
    int h(int v) {
        double k = Math.min(dimension.width / 140., dimension.height / 160);
        return (int) (v * 2 * k) - 50;
    }
}