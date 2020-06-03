package com.usthb.modeles;

import com.usthb.EUREKA;
import com.usthb.dessin.Potence;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.awt.Color;
import javax.swing.JPanel;
import javafx.embed.swing.SwingNode;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public abstract class Joueur implements Serializable {
    private static final long serialVersionUID = 1L;

    public static int n = 0;
    int num;
    String prenom;
    String nom;
    Date date;
    String password;
    int dernierNiveau;
    public HashMap<String, Integer> dernierNiveaux = new HashMap<String, Integer>();
    public static int[] coef = new int[] {1,1,1,1,1};
    public Joueur(String f, String s, Date d, String p) {
        this.num = Joueur.n;
        this.prenom = f;
        this.nom = s;
        this.date = d;
        this.password = p;
        this.dernierNiveau = 0;
        initDernierNiveauxAtteint();
        n++;
    }

    public int getTotalScore() {
        int score = 0;
        for (String key : this.dernierNiveaux.keySet()) {
            score += getThemeScore(key);
        }
        return score;
    }

    public int getThemeScore(String key) {
        int score = 0;
        int i = 0;
        switch (key) {
            case "HIS": i=0; break;
            case "GEO": i=1; break;
            case "SAN": i=2; break;
            case "CUL": i=3; break;
            case "ISL": i=4; break;
        }
        if (this.dernierNiveaux.get(key) > 0) score += 5*coef[i];
        if (this.dernierNiveaux.get(key) > 1) score += 10*coef[i];
        if (this.dernierNiveaux.get(key) > 2) score += 18*coef[i];
        if (this.dernierNiveaux.get(key) > 3) score += 28*coef[i];
        if (this.dernierNiveaux.get(key) > 4) score += 40*coef[i];
        return score;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public String getPassword() {
        return this.password;
    }

    public void initDernierNiveauxAtteint() {
        dernierNiveaux.put("HIS", 0);
        dernierNiveaux.put("GEO", 0);
        dernierNiveaux.put("SAN", 0);
        dernierNiveaux.put("CUL", 0);
        dernierNiveaux.put("ISL", 0);
    }

    public int getDernierNiveauxAtteint(String type) {
        return this.dernierNiveaux.get(type);
    }

    public int getNum() {
        return this.num;
    }

    public Date getDate() {
        return this.date;
    }

    public void incDernierNiveauxAtteint(String type) {
        if (this.dernierNiveaux.get(type) < 5)
            this.dernierNiveaux.put(type, getDernierNiveauxAtteint(type) + 1);
    }

    public void resetDernierNiveauAtteint(String type) {
        this.dernierNiveaux.put(type, 0);
    }

    public void setData(String f, String l, String p, Date d) {
        this.prenom = f;
        this.nom = l;
        this.password = p;
        this.date = d;
    }

    public int getNiveau() {
        return this.dernierNiveau;
    }

    public int[] getNiveauxDesThemes() {
        int[] n = new int[5];
        int i = 0;
        for (String key : this.dernierNiveaux.keySet()) {
            n[i] = this.dernierNiveaux.get(key);
            i++;
        }
        return n;
    }

    public void incNiveau() {
        this.dernierNiveau ++;
    }
    
    public abstract LinkedList<Question> getQuestions(LinkedList<Question> questions);

    public void Jouer(PartieJeu[][] parties, int niveau, int n, boolean rejouer) {
        PartieJeu p = parties[niveau][n];

        if (!rejouer) {
            String t = p.getThemeType();
            int dernierNiveauAtteint = getDernierNiveauxAtteint(t);

            while(n<5&&dernierNiveauAtteint>=niveau) { // on voit si le joueur a déjà répondu à cette question
                n++;
                if (n<5) {
                    p = parties[niveau][n];
                    t = p.getThemeType();
                    dernierNiveauAtteint = getDernierNiveauxAtteint(t);
                }
            }

            // si le joueur a répondu à toutes les questions de ce niveau (n==5), il est entrain de rejouer le niveau
            if(n==5) {
                n = 0;
                p = parties[niveau][n];
                t = p.getThemeType();
                dernierNiveauAtteint = getDernierNiveauxAtteint(t);
                rejouer = true;
            }   

        }

        GridPane startGame = new GridPane();
        //startGame.setGridLinesVisible(true);

        startGame.setPadding(new Insets(10, 5, 5, 5));
        for (int k = 0; k < 3; k++) {
            ColumnConstraints colConst = new ColumnConstraints();
            if (k==1) {
                colConst.setPercentWidth(50);
            }
            else colConst.setPercentWidth(25);
            colConst.setHalignment(HPos.CENTER);
            startGame.getColumnConstraints().add(colConst);
        }

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(15);
        startGame.getRowConstraints().add(row1);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(85);
        startGame.getRowConstraints().add(row2);

        Text titleText = new Text("\nNiveau "+(niveau)+"\n"+p.getThemeText()+"\n"); // niveau + le thème choisis
        titleText.setFont(Font.font(32));
        titleText.setStyle("-fx-font-weight: bold");
        titleText.setTextAlignment(TextAlignment.CENTER);

        // un message de bienvenue
        VBox userText = new VBox();
        userText.setAlignment(Pos.CENTER);
        Text welcome = new Text("Bonjour !");
        welcome.setFont(Font.font(14));
        StackPane userPane = new StackPane();
        userPane.setAlignment(Pos.CENTER);
        userPane.setMinWidth(210);
        Text prenom = new Text(this.getPrenom());
        Text nom = new Text(this.getNom());
        Text compte = new Text();
        if (this instanceof Adulte) compte.setText("(Compte Adulte)");
        else compte.setText("(Compte Enfant)");
        prenom.setFont(Font.font(16));
        prenom.setStyle("-fx-font-weight: bold");
        nom.setFont(Font.font(16));
        nom.setStyle("-fx-font-weight: bold");
        userText.getChildren().addAll(welcome,prenom,nom,compte);

        startGame.add(titleText, 1, 0);
        startGame.add(userText, 0, 0);

        // help
        Button help = new Button("?");
        help.setOnAction(e -> {
            showHelp();
        });
        startGame.add(help,2,0);

        // détails de l'avancement de l'utilisateur et le score
        VBox details = new VBox();
        details.setPadding(new Insets(30,30,30,30));
        details.getChildren().clear();
        Text niveaux = new Text ("Score des niveaux :");
        niveaux.setFont(Font.font(16));
        niveaux.setStyle("-fx-font-weight: bold");
        niveaux.setUnderline(true);
        details.getChildren().add(niveaux);

        Iterator<ThemeJeu> i = EUREKA.themes.iterator();
        while (i.hasNext()) { // l'ajout de textes de thème à la page
            String theme = i.next().getThemeType();
            Text txt = new Text(theme+" ("+getDernierNiveauxAtteint(theme)+") : "+getThemeScore(theme));
            txt.setFont(Font.font(14));
            details.getChildren().add(txt);
        }

        Text totalScore = new Text("\nVotre score total:");
        totalScore.setFont(Font.font(16));
        totalScore.setUnderline(true);

        Text score = new Text(); // le score en chiffres
        score.setFont(Font.font(26));
        score.setText(Integer.toString(getTotalScore())+"\n");
        score.setStyle("-fx-font-weight: bold");

        Button close = new Button("Fermer");
        close.setMinWidth(100);
        close.setAlignment(Pos.CENTER);
        close.setOnAction(event -> {
            EUREKA.initGame(EUREKA.buttonsScene, this);
            EUREKA.window.setScene(EUREKA.mainGame);
        });

        details.getChildren().addAll(totalScore,score,close);
        startGame.add(details,0,1);

        // potence
        Potence potence = new Potence();
        VBox potenceBody = new VBox(20);
        SwingNode potenceNode = new SwingNode();
        Text potenceText = new Text("Reste 8 coups à jouer !");

        JPanel potencePanel = new JPanel();
        potencePanel.add(potence);
        Color background = new Color(244,244,244);
        potencePanel.setBackground(background);

        // nous utilisons JavaFx Scene Builder, mais la potence est un Component, qui est un élément Swing
        // les scènes javafx n'acceptent que les noeuds comme éléments, les éléments Swing ne sont pas des noeuds
        // nous créeons donc un noeud à partir la potence qui est dans le JPanel potencePanel et le place dans la page
        potenceNode.setContent(potencePanel);


        potenceText.setFont(Font.font(16));
        potenceText.setText("Reste 8 coups à jouer !");
        potenceBody.getChildren().addAll(potenceNode,potenceText);
        // potenceBody.setPadding(new Insets(-20,5,5,0));
        potenceBody.setAlignment(Pos.CENTER);

        Button playAgain = new Button("Rejouer ?");

        // si j'utilise n ou rejouer j'ai cetter erreur : Local variable n defined in an enclosing scope must be final or effectively finalJava(536871575)
        int nn = n;
        boolean r = rejouer;
        playAgain.setOnAction(e -> {
            Jouer(parties, niveau, nn, r);
        });

        playAgain.setVisible(false); // le bouton rejouer sera visible quand l'utilisateur a terminé tous les niveaux
        potenceBody.getChildren().add(playAgain);

        // la question
        VBox body = new VBox(10);
        body.setPadding(new Insets(5,5,5,5));
        body.setAlignment(Pos.CENTER);
        body.setMinWidth(343);

        TextField txt1 = new TextField();
        TextField txt2 = new TextField();

        body.setStyle("-fx-background-fill: black, white ; -fx-background-insets: 0, 0 1 1 0 ;");

        String text = p.getQuestion();
        String reponse = p.getQuestionReponse();
        String image = p.getImage();
        Text questNum = new Text("Question "+(n+1)+" :");
        questNum.setFont(Font.font(16));
        questNum.setUnderline(true);
        body.getChildren().add(questNum);

        Text question = new Text(text);
        question.setFont(Font.font(16));
        question.setTextAlignment(TextAlignment.CENTER);
        question.wrappingWidthProperty().bind(body.widthProperty());
        
        if (!image.isEmpty()&&!EUREKA.imagesFolder.isEmpty()) { // s'il y a une image on la met là
            ImageView img = new ImageView(EUREKA.imagesFolder+p.getImage());
            img.setFitWidth(343); // réinitialiser la largeur maximale de l'image à 343 px
            img.setFitHeight(170); // et longueur maximale jusqu'à 170 px
            img.setPreserveRatio(true); // préserver le ration hauteur:largeur
            body.getChildren().add(img);
        }

        body.getChildren().add(question); // ajoutons le texte du question

        txt1.setMaxWidth(250); // txt1 : la réponse du question en ********
        txt2.setMaxWidth(250); // txt2 : la réponse du utilisateur
        txt1.setAlignment(Pos.CENTER);
        txt1.setEditable(false);
        txt2.setAlignment(Pos.CENTER);

        txt1.setStyle("-fx-highlight-fill: null; -fx-highlight-text-fill: null;"); // désactiver la sélection avec souris ou clavier
        txt2.setStyle("-fx-highlight-fill: null; -fx-highlight-text-fill: null;");

        Text r1 = new Text("La réponse");
        Text r2 = new Text("Votre réponse");

        String rep = reponse.replaceAll("(?s).", "*"); // remplacer tous les caractères de la réponse par *

        txt1.setText(rep);
        txt1.setFont(Font.font(16));
        txt2.setFont(Font.font(16));

        Boolean[] found = new Boolean[rep.length()]; // cette chose aide à obtenir des index de caractères qui se sont produits plus d'une fois
        for (int k = 0; k < rep.length(); k++) {
            found[k] = false;
        }

        // pour détecter le changement de texte à l'intérieur du TextField
        // si j'utilise n ou p j'ai cetter erreur : Local variable n defined in an enclosing scope must be final or effectively finalJava(536871575)
        int nnn = n;
        PartieJeu pp = p;
        txt2.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length()>=oldValue.length()) { // if the user didn't press BACKSPACE
                pp.setReponse(newValue); // réponse dans l'object PartieJeu (StringBuffer)

                // si l'utilisateur a écrit quelque chose, nous le vérifions
                input(parties,nnn,niveau,txt1,txt2,found,potence,potenceText,potenceBody,details,r);
            }
        });

        body.getChildren().addAll(r1,txt1,r2,txt2);

        startGame.add(body,1,1);
        startGame.add(potenceBody,2,1);

        Scene start = new Scene(startGame,720,480);
        EUREKA.window.setScene(start);
    }

    public void showHelp() {
        Stage help = new Stage();
        help.setTitle("Help");
        VBox helpLayout = new VBox(20);
        helpLayout.setAlignment(Pos.CENTER);
        Scene helpScene = new Scene(helpLayout,640,500);

        Text txt = new Text("Comment Jouer");
        txt.setFont(Font.font(26));
        txt.setStyle("-fx-font-weight: bold");
        Text hlp = new Text("       Répondez au question en tapant les caractères que vous pensez appartenir à la réponse dans le champ \"Votre réponse\".\n       Vous n'avez que 8 tentatives pour trouver tous les caractères de réponse. Si vous écrivez un caractère qui n'appartient pas à la réponse, une partie d'un dessin de pendu sera dessinée. Si vous trouvez la réponse dans les 8 essais, vous passez au niveau suivant, mais si vous échouez, le dessin du pendu est complété et la partie de jeu est terminée avec un échec.\n        Votre score total est la somme du score de chaque thème.\n  - Le score de thème est calculé de cette façon :\n       score de niveau * coefficient\n  - Le score de niveau est calculé comme suit :\n5 points pour le niveau 1, 10 pour le niveau 2, 18 pour le niveau 3, 28 pour le niveau 4 et 40 pour le niveau 5.");
        hlp.setFont(Font.font(18));
        hlp.setWrappingWidth(530);
        helpLayout.getChildren().addAll(txt,hlp);
        help.setScene(helpScene);
        help.setResizable(false);
        help.show();
    }
    
    // vérification du dernier caractère écrit par l'utilisateur
    public void input(PartieJeu[][] parties, int n, int niveau, TextField txt1, TextField txt2, Boolean[] found, Potence potence, Text text, VBox potenceBody, VBox details, boolean r) {
        PartieJeu p = parties[niveau][n];
        StringBuffer reponse = p.getReponse();
        if(reponse.length()>0) {

            // java est sensible aux majuscules et minuscules
            // si on veut comparer des personnages
            // nous les transformons en majuscules ou en minuscules
            // nous utilisons des majuscules
            char c = Character.toUpperCase(reponse.charAt(reponse.length()-1));

            boolean foundThisChar = false; // avons-nous trouvé ce charactère dans la réponse ? pas encore

            //si le chatactère est l'un des chatactères de la réponse
            if (p.checkCharacter(c)) {
                int index = p.getQuestionReponse().toUpperCase().indexOf(c);
                // toUpperCase()
                // java est sensible aux majuscules et minuscules
                // si on veut comparer des characteres
                // nous les transformons en majuscules ou en minuscules
                // nous utilisons des majuscules

                while(index<p.getQuestionReponse().length()) {
                    // nous vérifions si ce caractère n'est pas trouvé auparavant et est passé de * au charactère
                    if (!found[index]&&p.getQuestionReponse().toUpperCase().charAt(index)==c) break;
                    else index ++;
                }
                if (index<p.getQuestionReponse().length()){
                    // si nous cherchons tout et ne le trouvons pas, l'index sera hors du bout de la longueur de la réponse
                    // donc si index<p.getQuestionReponse().length() nous l'avons trouvé
                    found[index] = true; // nous le rendons vrai donc il saute la prochaine fois que nous vérifions un autre charactère
                    foundThisChar = true; // oui nous l'avons trouvé cette fois

                    // nous supprimons le * et le remplaçons par le caractère
                    String str = txt1.getText().substring(0, index) + p.getQuestionReponse().charAt(index) + txt1.getText().substring(index + 1);
                    txt1.setText(str);
                }
            }

            // si on trouvait toute la réponse
            if (txt1.getText().equals(p.getTheme().getQuestion(p.getNumQuest()).getReponse())) {
                potence.setTrouve(true);
                txt2.setEditable(false); // nous désactivons le TextField , car toute nouvelle insertion ruinera le programme
                reponseTrouve(potenceBody,parties,niveau,n,details,r);
            }
            
            // si le charactère que l'utilisateur a écrit ne figure pas parmi les *****
            if(!foundThisChar) {
                // dessiner la potence et icrémenter l'état
                potence.repaint();
                potence.incrementEtat();
            }
            
            //s'il a utilisé tous ses 8 essais
            if (potence.getEtat() == 8) {
                text.setText("Vous êtes pendu !");
                txt2.setEditable(false);
                potenceBody.getChildren().get(2).setVisible(true); // getting the play again button
            }
            else if (potence.getEtat() == 7) text.setText("Reste un coup à jouer !");
            else text.setText("Reste " + (8 - potence.getEtat()) + " coups à jouer");
        }
    }
    
    public void reponseTrouve(VBox potenceBody, PartieJeu[][] parties, int niveau, int n, VBox details, boolean r) {
        PartieJeu p = parties[niveau][n];

        int dernier = getDernierNiveauxAtteint(p.getNumQuest().substring(0, 3));
        if(dernier<niveau) incDernierNiveauxAtteint(p.getNumQuest().substring(0, 3));
        
        EUREKA.saveUsers();

        potenceBody.getChildren().clear();
        Text text = new Text("Bravo!\nVous avez trouvé");
        text.setFont(Font.font(18));
        text.setTextAlignment(TextAlignment.CENTER);
        potenceBody.getChildren().add(text);

        if (niveau==5&&n==4) { // si l'utilisateur a terminé tous les thèmes de ce niveau
            Text t = new Text("Vous avez terminé tous\nles niveaux !");
            potenceBody.getChildren().add(t);
        } else if (n==4) {
            Text t = new Text("Vous avez terminé tous\nles questions de ce niveau !");
            incNiveau();
            potenceBody.getChildren().add(t);
            Button next = new Button("Niveau Suivant");
            next.setOnAction(e -> {
                Jouer(parties, (niveau+1), 0, false);
            });
            potenceBody.getChildren().add(next);
        } else {
            Button next = new Button("Question suivante"); // si l'utilisateur a trouvé la solution, on lui fait passer au question suivante
            next.setOnAction(e -> {
                Jouer(parties, niveau, (n+1), r);
            });
            potenceBody.getChildren().add(next);
        }
    }
}