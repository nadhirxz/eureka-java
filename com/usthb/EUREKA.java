package com.usthb;

import com.usthb.modeles.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

@SuppressWarnings({"unchecked"})
public class EUREKA extends Application {
    public static HashMap<Integer, Joueur> users = new HashMap<Integer, Joueur>(); // le map contenant tous les utilisateurs
    public static HashSet<ThemeJeu> themes = new HashSet<ThemeJeu>(); // le set contenat tous les themes

    public static Stage window;
    public static Scene mainGame;
    public static Scene buttonsScene;

    // public static String questionsFile = "C:\\Users\\Me\\Desktop\\Docs\\JavaFx\\com\\usthb\\questions.txt";
    // public static String imagesFolder = "file:///C:/Users/Me/Desktop/Docs/JavaFx/com/usthb/";
    // public static String usersFile = C:\\Users\\Me\\Desktop\\Docs\\JavaFx\\com\\usthb\\users.txt";
    public static String questionsFile = ""; // chemin du fichier des questions
    public static String imagesFolder = ""; // chemin du dossier des images
    public static String usersFile = ""; // chemin du fichier des utilisateurs

    String[] allThemes = new String[] { "Histoire", "Géographie", "Santé", "Culture Générale", "Islam" };
    String[][][] questions; // toutes les questions
    LinkedList<Question> questionsList[] = new LinkedList[5];

    boolean saveData = true;

    public static void main(String[] args) { // MAIN
        launch(args);
    }

    public boolean checkIfItExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    @Override
    public void start(Stage primaryStage) { // cette fonction lancera au début

        // vérifier si les fichiers users.txt, questions.txt et le dossier images existent dans le répertoire du programme
        if (checkIfItExists(System.getProperty("user.dir")+"\\questions.txt")) questionsFile = System.getProperty("user.dir")+"\\questions.txt";
        if (checkIfItExists(System.getProperty("user.dir")+"\\users.txt")) usersFile = System.getProperty("user.dir")+"\\users.txt";
        if (checkIfItExists(System.getProperty("user.dir")+"\\images")) imagesFolder = "file///:"+(System.getProperty("user.dir")+"\\images/").replace("\\", "/");
        
        // vérifier s'il y a des données précédentes de ce programme enregistrées dans eureka.data
        if (saveData) saveDataFile(true);

        window = primaryStage;

        VBox mainLayout = new VBox(20);
        Scene mainScene = new Scene(mainLayout, 640, 320);

        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPrefWidth(150);

        // les buttons
        Button regButton = new Button("S'inscrire");
        Button loginButton = new Button("S'identifier");
        Button settingsButton = new Button("Préférences");
        Button usersButton = new Button("Utilisateurs");
        Button aboutBtn = new Button("À propos");
        regButton.setMinWidth(mainLayout.getPrefWidth());
        loginButton.setMinWidth(mainLayout.getPrefWidth());
        settingsButton.setMinWidth(mainLayout.getPrefWidth());
        usersButton.setMinWidth(mainLayout.getPrefWidth());
        aboutBtn.setMinWidth(mainLayout.getPrefWidth());

        // fonctions des buttons
        regButton.setOnAction(e -> {
            createRegisterPage(mainScene);
        });
        loginButton.setOnAction(e -> {
            createLoginPage(mainScene);
        });
        settingsButton.setOnAction(e -> {
            createSettingsPage(mainScene, mainLayout);
        });
        usersButton.setOnAction(e -> {
            afficherInfosJoueursInscrits(mainScene);
        });
        aboutBtn.setOnAction(e -> {
            createAboutPage(mainScene);
        });

        // l'ajout de composants à la disposition principale
        mainLayout.getChildren().addAll(regButton, loginButton, settingsButton, usersButton, aboutBtn);
        mainLayout.requestFocus();

        // réglage de la fenêtre
        window.setScene(mainScene);
        window.setResizable(false);
        window.sizeToScene();
        window.setTitle("EUREKA");

        initQuestionsList(regButton, loginButton); // initialisations des questions
        loadUsers(); // chargement des utilisateurs du fichier
        
        window.show(); // lancement de la fenêtre
    }

    public void createRegisterPage(Scene mainScene) {
        GridPane regLayout = new GridPane();
        Scene regScene = new Scene(regLayout, 640, 320);
        regLayout.setAlignment(Pos.CENTER);
        regLayout.setPadding(new Insets(20, 20, 20, 20));
        regLayout.setHgap(20);
        regLayout.setVgap(20);

        // création de colonnes et de lignes du GridPane
        ColumnConstraints culumn1 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        culumn1.setHalignment(HPos.RIGHT);
        ColumnConstraints column2 = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        column2.setHgrow(Priority.ALWAYS);
        regLayout.getColumnConstraints().addAll(culumn1, column2);

        // l'ajout de composants
        Label headerLabel = new Label("S'inscrire");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        regLayout.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        Label prenomLabel = new Label("Prénom : ");
        regLayout.add(prenomLabel, 0, 1);

        TextField prenomField = new TextField();
        prenomField.setPrefHeight(40);
        regLayout.add(prenomField, 1, 1);

        Label nomLabel = new Label("Nom : ");
        regLayout.add(nomLabel, 0, 2);

        TextField nomField = new TextField();
        nomField.setPrefHeight(40);
        regLayout.add(nomField, 1, 2);

        Label passwordLabel = new Label("Mot de passe : ");
        regLayout.add(passwordLabel, 0, 3);

        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        regLayout.add(passwordField, 1, 3);

        Label dateLabel = new Label("D. de naissaince : ");
        regLayout.add(dateLabel, 0, 4);

        DatePicker dateField = new DatePicker();
        dateField.setMinWidth(200);
        dateField.setMaxWidth(200);
        dateField.setPrefWidth(200);

        HBox line = new HBox(0);
        line.getChildren().add(dateField);

        // type du joueur
        TilePane r = new TilePane();
        Label l = new Label("Type :"); 
        l.setPadding(new Insets(0,0,0,30));

        ToggleGroup tg = new ToggleGroup();

        RadioButton r1 = new RadioButton("Adulte"); 
        RadioButton r2 = new RadioButton("Enfant");
        r1.setSelected(true);
        r1.setPadding(new Insets(0,10,0,10));

        r1.setToggleGroup(tg);
        r2.setToggleGroup(tg);

        r.getChildren().addAll(l,r1,r2);
        line.getChildren().add(r);

        regLayout.add(line, 1, 4);


        HBox box = new HBox(50);
        Button submitButton = new Button("S'inscrire");
        submitButton.setPrefHeight(40);
        submitButton.setMinWidth(100);
        Button cancelButton = new Button("Annuler");
        cancelButton.setPrefHeight(40);
        cancelButton.setMinWidth(100);
        box.getChildren().addAll(submitButton, cancelButton);
        regLayout.add(box, 1, 5, 2, 1);
        GridPane.setHalignment(box, HPos.CENTER);

        // fonctions des buttons
        submitButton.setOnAction(event -> {
            RadioButton rb = (RadioButton)tg.getSelectedToggle(); 
            String type = rb.getText(); 
            register(prenomField.getText(), nomField.getText(), passwordField.getText(), dateField.getValue(), type, regLayout, mainScene);
        });
        cancelButton.setOnAction(event -> {
            window.setScene(mainScene);
        });

        // changement du page
        window.setScene(regScene);
    }

    public void createLoginPage(Scene mainScene) {
        GridPane loginLayout = new GridPane();
        Scene loginScene = new Scene(loginLayout, 640, 320);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(20, 20, 20, 20));
        loginLayout.setHgap(20);
        loginLayout.setVgap(20);

        ColumnConstraints culumn1 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        culumn1.setHalignment(HPos.RIGHT);
        ColumnConstraints column2 = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        column2.setHgrow(Priority.ALWAYS);
        loginLayout.getColumnConstraints().addAll(culumn1, column2);

        Label headerLabel = new Label("S'identifier");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        loginLayout.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        Label prenomLabel = new Label("Prénom : ");
        loginLayout.add(prenomLabel, 0, 1);

        TextField prenomField = new TextField();
        prenomField.setPrefHeight(40);
        loginLayout.add(prenomField, 1, 1);

        Label nomLabel = new Label("Nom : ");
        loginLayout.add(nomLabel, 0, 2);

        TextField nomField = new TextField();
        nomField.setPrefHeight(40);
        loginLayout.add(nomField, 1, 2);

        Label passwordLabel = new Label("Mot de passe : ");
        loginLayout.add(passwordLabel, 0, 3);

        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        loginLayout.add(passwordField, 1, 3);

        HBox box = new HBox(50);
        Button submitButton = new Button("S'identifier");
        submitButton.setPrefHeight(40);
        submitButton.setMinWidth(100);
        Button cancelButton = new Button("Annuler");
        cancelButton.setPrefHeight(40);
        cancelButton.setMinWidth(100);
        box.getChildren().addAll(submitButton, cancelButton);
        loginLayout.add(box, 1, 5, 2, 1);
        GridPane.setHalignment(box, HPos.CENTER);

        submitButton.setOnAction(event -> {
            login(prenomField.getText(), nomField.getText(), passwordField.getText(), loginLayout, mainScene);
        });
        cancelButton.setOnAction(event -> {
            window.setScene(mainScene);
        });

        loginLayout.setAlignment(Pos.CENTER);

        window.setScene(loginScene);
    }

    public void createSettingsPage(Scene mainScene, VBox mainLayout) { // la page des préférences
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        Scene settings = new Scene(layout, 640, 320);

        FileChooser questFile = new FileChooser(); // pour sélectionner le fichier
        questFile.getExtensionFilters().addAll(new ExtensionFilter("Text", "*.txt")); // filtre

        HBox question = new HBox(5);
        question.setAlignment(Pos.CENTER);
        TextField questionPath = new TextField();
        if (!questionsFile.isEmpty())
            questionPath.setText(questionsFile);
        Button chooseQuestFile = new Button("Sélectionner le fichier de questions");
        chooseQuestFile.setOnAction(e -> {
            File file = questFile.showOpenDialog(window);
            if (file != null) {
                questionPath.setText(file.getAbsolutePath());
            }
        });
        question.getChildren().addAll(questionPath, chooseQuestFile);

        DirectoryChooser imageFile = new DirectoryChooser(); // pour sélectionner le dossier
        questFile.getExtensionFilters().addAll(new ExtensionFilter("Text", "*.txt"));

        HBox image = new HBox(5);
        image.setAlignment(Pos.CENTER);
        TextField imagePath = new TextField();
        if (!imagesFolder.isEmpty())
            imagePath.setText(imagesFolder);
        Button chooseImgFolder = new Button("Sélectionner le dossier des images");
        chooseImgFolder.setOnAction(e -> {
            File file = imageFile.showDialog(window);
            if (file != null) {
                imagePath.setText(file.getAbsolutePath());
            }
        });
        image.getChildren().addAll(imagePath, chooseImgFolder);

        HBox user = new HBox(5);
        user.setAlignment(Pos.CENTER);
        TextField usersPath = new TextField();
        if (!usersFile.isEmpty())
            usersPath.setText(usersFile);
        Button chooseUsersFile = new Button("Sélectionner le fichier des utilisateurs");
        FileChooser userFile = new FileChooser(); // pour sélectionner le fichier
        userFile.getExtensionFilters().addAll(new ExtensionFilter("Text", "*.txt")); // filtre
        chooseUsersFile.setOnAction(e -> {
            File file = userFile.showOpenDialog(window);
            if (file != null) {
                usersPath.setText(file.getAbsolutePath());
            }
        });
        user.getChildren().addAll(usersPath, chooseUsersFile);

        questionPath.setMinWidth(320);
        imagePath.setMinWidth(320);
        usersPath.setMinWidth(320);
        chooseQuestFile.setMinWidth(220);
        chooseImgFolder.setMinWidth(220);
        chooseUsersFile.setMinWidth(220);

        HBox buttons = new HBox(20);
        Button save = new Button("Sauvegarder");
        Button cancel = new Button("Retourner");
        buttons.getChildren().addAll(save, cancel);
        buttons.setAlignment(Pos.CENTER);

        save.setMinWidth(100);
        cancel.setMinWidth(100);

        // fonctions des buttons
        save.setOnAction(e -> {
            if (!questionPath.getText().isEmpty()) { // si l'utilisateur a choisi un fichier des questions
                if (usersPath.getText().isEmpty()) { // si l'utilisateur n'a pas choisi un fichier des utilisateurs nous obtenons le chemin du fichier de questions et créons un fichier d'utilisateurs dans le même dossier
                    String[] f = questionPath.getText().split("\\\\");
                    String path = String.join("\\", Arrays.copyOf(f, f.length - 1)) + "\\users.txt";
                    File file = new File(path);
                    try {
                        file.createNewFile();
                        usersFile = path;
                    } catch (IOException e1) {
                        //
                    }
                }
                questionsFile = questionPath.getText();
                mainLayout.getChildren().get(0).setDisable(false); // getting register button
                mainLayout.getChildren().get(1).setDisable(false); // getting login button
                initQuestionsList((Button) mainLayout.getChildren().get(0), (Button) mainLayout.getChildren().get(1));
            }
            if (!imagePath.getText().isEmpty())
                imagesFolder = "file:///" + imagePath.getText().replace("\\", "/") + "/"; // si l'utilisateur a choisi un dossier

            if (!usersPath.getText().isEmpty())
                usersFile = usersPath.getText(); // si l'utilisateur a choisi un fichier des utilisateurs

            if (saveData) saveDataFile(false);
            window.setScene(mainScene);
        });

        cancel.setOnAction(e -> {
            window.setScene(mainScene);
        });

        Button writeQuestionsButton = new Button("Saisir les questions");
        writeQuestionsButton.setMinWidth(250);
        writeQuestionsButton.setOnAction(e -> {
            questionsFile = questionPath.getText();
            initQuestionsList(null, null);
            writeQuestions(questionPath);
        });

        questionPath.setText(questionsFile); // nous écrirons ce que l'utilisateur a choisi dans le TextField

        if (imagesFolder.length() > 9) { // si le chemin est plus que "file:///"
            imagePath.setText(imagesFolder.substring(8, imagesFolder.length() - 1).replace("/", "\\")); // nous écrirons ce que l'utilisateur a choisi dans le TextField
        }

        questionPath.setEditable(false);
        imagePath.setEditable(false);
        usersPath.setEditable(false);

        layout.getChildren().addAll(question, image, user, writeQuestionsButton, buttons);
        layout.requestFocus();
        window.setScene(settings);
    }

    public void writeQuestions(TextField questionPathField) { // créer une nouvelle fenêtre pour que l'utilisateur puisse écrire des questions
        Stage newWindow = new Stage();
        VBox box = new VBox(20);
        GridPane layout = new GridPane();
        // layout.setGridLinesVisible(true);
        layout.setPadding(new Insets(0, 30, 0, 30));
        Scene scene = new Scene(box, 720, 480);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            if (i == 0)
                colConst.setPercentWidth(50);
            else
                colConst.setPercentWidth(25);
            colConst.setHalignment(HPos.CENTER);
            layout.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < 6; i++) {
            RowConstraints row1 = new RowConstraints();
            if (i == 0)
                row1.setPercentHeight(10);
            else
                row1.setPercentHeight(18);
            layout.getRowConstraints().add(row1);
        }

        Text text = new Text();
        text.setFont(Font.font(36));
        text.setTextAlignment(TextAlignment.CENTER);

        Text q = new Text("Question");
        Text r = new Text("Réponse");
        Text i = new Text("Image");

        q.setFont(Font.font(18));
        r.setFont(Font.font(18));
        i.setFont(Font.font(18));

        layout.add(q, 0, 0);
        layout.add(r, 1, 0);
        layout.add(i, 2, 0);

        layout.setHgap(10);

        TextField[] questionFields = new TextField[] { new TextField(), new TextField(), new TextField(), new TextField(), new TextField() };
        TextField[] reponseFields = new TextField[] { new TextField(), new TextField(), new TextField(), new TextField(), new TextField() };
        TextField[] imageFields = new TextField[] { new TextField(), new TextField(), new TextField(), new TextField(), new TextField() };

        for (int j = 1; j < 6; j++) {
            layout.add(questionFields[j - 1], 0, j);
        }
        for (int j = 1; j < 6; j++) {
            layout.add(reponseFields[j - 1], 1, j);
        }
        for (int j = 1; j < 6; j++) {
            layout.add(imageFields[j - 1], 2, j);
        }

        HBox buttons = new HBox(20);
        Button next = new Button("Suivant");
        Button prev = new Button("Annuler");
        buttons.setAlignment(Pos.CENTER);
        next.setMinWidth(100);
        prev.setMinWidth(100);
        prev.setOnAction(e -> {
            newWindow.close();
        });

        buttons.getChildren().addAll(prev,next);

        HBox coefBox = new HBox(5);
        coefBox.setMinWidth(100);
        coefBox.setAlignment(Pos.CENTER);
        Text coefText = new Text("Coefficient :");
        TextField coefField = new TextField("1");
        coefBox.getChildren().addAll(coefText, coefField);

        // nous voulons lire 5 questions, 5 réponses et probablement 5 images pour chaque thème
        // le fait de mettre tous les TextFields sur une seule page est trop
        // donc nous créons plusieurs pages avec des paramètres différents mais la même mise en page

        createPages(0, questionFields, reponseFields, imageFields, text, next, prev, newWindow, questionPathField, coefField);
        // ce 0 signifie la page 0, correspondant au 0ème thème dans le tableau "allThemes" ci-dessus

        layout.setAlignment(Pos.CENTER);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(text, layout, coefBox, buttons);

        newWindow.setScene(scene);
        newWindow.setResizable(false);
        newWindow.show(); // affichage d'une nouvelle fenêtre
    }

    // la creations des pages pour la saisie des questions
    public void createPages(int k, TextField[] questionFields, TextField[] reponseFields, TextField[] imageFields, Text text, Button next, Button prev, Stage newWindow, TextField questionPathField, TextField coefField) {
        // k est le numéro de la page en cours, correspondant au k ème thème sur le tableau "allThemes"

        if(k>4) { // k>4 signifie que nous sommes dans les pages des questions des enfants
            coefField.setDisable(true);
            text.setText("Questions Enfant\n"+allThemes[k%5]);
        } else {
            coefField.setDisable(false);
            text.setText("Questions Adulte\n"+allThemes[k%5]);
        }
        coefField.setText(Integer.toString(Joueur.coef[k%5]));

        int start = 0, end = 5;
        if (k>4) {
            start = 5;
            end = 10;
        }
        for (int i = start; i < end; i++) {
            questionFields[i%5].setText(questions[k%5][i][0]);
            reponseFields[i%5].setText(questions[k%5][i][1]);
            imageFields[i%5].setText(questions[k%5][i][2]);
        }

        if (k == 0) { // si nous sommes sur la première page
            prev.setText("Annuler"); // nous voulons que le bouton permette à l'utilisateur de quitter
            prev.setOnAction(e -> {
                newWindow.close();
            });
        } else {
            prev.setText("Précédent"); // nous voulons que le bouton permette de revenir d'une page
            prev.setOnAction(e -> {
                fonction(k, questions, questionFields, reponseFields, imageFields, coefField, newWindow);
                createPages(k - 1, questionFields, reponseFields, imageFields, text, next, prev, newWindow, questionPathField, coefField);
                // on veut revenir à la page précédente, donc k-1
            });
        }
        if (k < 9) { // si nous sommes sur un des 9 premières pages
            next.setText("Suivant");
            next.setOnAction(e -> {
                fonction(k, questions, questionFields, reponseFields, imageFields, coefField, newWindow);
                createPages(k + 1, questionFields, reponseFields, imageFields, text, next, prev, newWindow, questionPathField, coefField);
                // et on passe à la page suivante, donc k+1
            });
        } else { // si nous sommes dans la dernière page
            next.setText("Sauvegarder"); // nous voulons que le bouton permette à l'utilisateur de sauvegarder
            next.setOnAction(e -> {
                fonction(k,questions, questionFields, reponseFields, imageFields, coefField, newWindow);

                questionsFile = saveQuestionsFile(newWindow); // parce que c'est la dernière page, on veut tout sauvegarder, cette fonction retourne le chemin du fichier enregistré
                questionPathField.setText(questionsFile); // et on change le texte sur le TextField de la fenêtre précédente

                newWindow.close(); // et on ferme la fenêtre
            });
        }
    }

    public void fonction(int k, String[][][] questions, TextField[] questionFields, TextField[] reponseFields, TextField[] imageFields, TextField coefField, Window newWindow) {
        int start=0,end=0;
        if(k>4) {
            start = 5;
            end = 10;
        }
        for (int i = start; i < end; i++) {
            // nous vérifions si l'utilisateur a tout saisi
            if (questionFields[i%5].getText() == null) {
                showAlert(Alert.AlertType.ERROR, newWindow, "Erreur", "Veuillez remplir tous les champs de 'question'");
                return;
            }
            if (reponseFields[i%5].getText() == null) {
                showAlert(Alert.AlertType.ERROR, newWindow, "Erreur", "Veuillez remplir tous les champs de 'réponse'");
                return;
            }
            if (coefField.getText() == null) {
                showAlert(Alert.AlertType.ERROR, newWindow, "Erreur", "Veuillez remplir le champs de coefficient");
                return;
            }
            questions[k%5][i][0] = questionFields[i%5].getText();
            questions[k%5][i][1] = reponseFields[i%5].getText();
            questions[k%5][i][2] = imageFields[i%5].getText();

            if(k<5) Joueur.coef[k] = Integer.parseInt(coefField.getText());
        }
    }

    public String saveQuestionsFile(Stage newWindow) {
        // cela va créer un String à partir de toutes les données entrées que nous avons
        // enregistrées dans le tableau
        String theFile = new String();

        for (int i = 0; i < 10; i++) {
            theFile += Joueur.coef[i%5];
            int start = 0, end = 5;
            if (i>4) {
                start = 5;
                end = 10;
            }
            for (int j = start; j < end; j++) {
                theFile += "\n" + questions[i%5][j][0];
                theFile += "##" + questions[i%5][j][1];
                if (questions[i%5][j][2] != null) theFile += "##" + questions[i%5][j][2];
            }
            if (i%5 < 5&&i<9) theFile += "\n--------------------------------------------------\n";
        }

        FileChooser f = new FileChooser();
        f.getExtensionFilters().addAll(new ExtensionFilter("Text", "*.txt"));
        File file = f.showSaveDialog(newWindow);
        if (file != null) {
            saveTextToFile(theFile, file); // appeler une fonction qui enregistre un String dans un fichier
        }
        return file == null ? "" : file.getAbsolutePath(); // et nous retournons le chemin du fichier enregistré
    }

    public void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            //
        }
    }

    public void afficherInfosJoueursInscrits(Scene mainScene) {
        VBox layout = new VBox(20);
        Scene usr = new Scene(layout,640,320);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10,25,10,25));

        Text usrs = new Text("Utilisateurs");
        usrs.setFont(Font.font(26));
        layout.getChildren().add(usrs);

        if (users.size()>0) {
            layout.getChildren().add(createTable());
        } else {
            usrs.setText("Pas d'utilisateurs");
        }

        Button close = new Button("Retourner");
        close.setOnAction(e -> {
            window.setScene(mainScene);
        });
        layout.getChildren().add(close);
        window.setScene(usr);
    }

    public TableView<Joueur> createTable() {
        TableView<Joueur> table = new TableView<>();

        // colonne du prénom
        TableColumn<Joueur, String> c2 = new TableColumn<>("Prénom");
        c2.setMinWidth(140);
        c2.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        // colonne du nom
        TableColumn<Joueur, String> c1 = new TableColumn<>("Nom");
        c1.setMinWidth(140);
        c1.setCellValueFactory(new PropertyValueFactory<>("nom"));

        // colonne du date de naissance
        TableColumn<Joueur, String> c3 = new TableColumn<>("D. de naissance");
        c3.setMinWidth(140);
        //c3.setCellValueFactory(new PropertyValueFactory<>("date"));
        c3.setCellValueFactory(features -> {
            Locale locale = new Locale("fr", "FR");
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
            String date = dateFormat.format(features.getValue().getDate());
            return new SimpleStringProperty(date);
        });

        // colonne du type du joueur
        TableColumn<Joueur, String> c4 = new TableColumn<>("Type");
        c4.setMinWidth(80);
        c4.setCellValueFactory(features -> {
            return Bindings.when(Bindings.createBooleanBinding(() -> features.getValue() instanceof Adulte))
            .then("Adulte")
            .otherwise("Enfant");
        });

        // colonne du niveau du joueur
        TableColumn<Joueur, Integer> c5 = new TableColumn<>("Niveau");
        c5.setMinWidth(70);
        c5.setCellValueFactory(new PropertyValueFactory<>("Niveau"));

        table.setItems(getUsers());
        table.setStyle("-fx-background-color: #E3E3E3;-fx-hover-color: transparent;");
        table.getColumns().addAll(c1,c2,c3,c4,c5);
        
        return table;
    }

    public ObservableList<Joueur> getUsers() {
        ObservableList<Joueur> u = FXCollections.observableArrayList();
        for (Integer i : users.keySet()) {
            u.add(users.get(i));
        }
        return u;
    }

    public void createAboutPage(Scene mainScene) { // la page À propos
        VBox abt = new VBox(20);
        Scene about = new Scene(abt, 640, 320);

        GridPane layout = new GridPane();
        layout.setMaxWidth(600);
        layout.setAlignment(Pos.CENTER);
        // layout.setGridLinesVisible(true);
        for (int i = 0; i < 2; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            if (i == 0)
                colConst.setPercentWidth(20);
            if (i == 1)
                colConst.setPercentWidth(5);
            if (i == 2)
                colConst.setPercentWidth(35);
            layout.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < 2; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(20);
            layout.getRowConstraints().add(row);
        }

        Text text1 = new Text("EUREKA");
        Text text2 = new Text("Créé par");
        Text text3 = new Text("Nadhir");
        Text text4 = new Text("Date de création");
        Text text5 = new Text("04 Mai 2020");
        Text text6 = new Text("Version");
        Text text7 = new Text("1.1");
        Text text8 = new Text(":");
        Text text9 = new Text(":");
        Text text10 = new Text(":");
        text1.setFont(Font.font(48));
        text2.setFont(Font.font(14));
        text3.setFont(Font.font(14));
        text4.setFont(Font.font(14));
        text5.setFont(Font.font(14));
        text6.setFont(Font.font(14));
        text7.setFont(Font.font(14));
        text8.setFont(Font.font(14));
        text9.setFont(Font.font(14));
        text10.setFont(Font.font(14));

        // text2.setUnderline(true);
        // text3.setUnderline(true);
        // text4.setUnderline(true);

        layout.add(text2, 0, 0);
        layout.add(text3, 2, 0);
        layout.add(text4, 0, 1);
        layout.add(text5, 2, 1);
        layout.add(text6, 0, 2);
        layout.add(text7, 2, 2);

        layout.add(text8, 1, 0);
        layout.add(text9, 1, 1);
        layout.add(text10, 1, 2);

        Button close = new Button("Fermer");
        close.setMinWidth(100);
        close.setOnAction(event -> {
            window.setScene(mainScene);
        });
        abt.getChildren().addAll(text1, layout, close);
        abt.setAlignment(Pos.CENTER);
        window.setScene(about);
    }

    public void register(String prenom, String nom, String password, LocalDate bdate, String type, GridPane layout, Scene mainScene) {
        // nous vérifions si l'utilisateur a tout saisi
        if (prenom.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur",
                    "Entrez votre prénom s'il vous plait");
            return;
        }
        if (nom.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Entrez votre nom s'il vous plait");
            return;
        }
        if (password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Entrez votre mot de passe s'il vous plait");
            return;
        }
        if (password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Entrez votre mot de passe s'il vous plait");
            return;
        }
        if (bdate == null) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Entrez votre date de naissance s'il vous plait");
            return;
        }

        // et si la date de naissance est valide
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date date = Date.from(bdate.atStartOfDay(defaultZoneId).toInstant());
        if (date.after(new Date())) { // if he's born in the future lol
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Date de naissance invalide");
            return;
        }

        if (!users.isEmpty() && userExists(prenom, nom)) { // si cet utilisateur existe déjà
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Cet utilisateur existe déjà");
            return;
        }

        char t = type.charAt(0);
        Joueur j;
        if (t=='A') {
            j = new Adulte(prenom, nom, date, password);
        } else {
            j = new Enfant(prenom, nom, date, password);
        }

        // si tout va bien alors nous procédons nous enregistrons le nouvel utilisateur
        // sur le HashMap users
        users.put(j.getNum(), j);

        // initialisation des themes
        initThemes(j);

        // l'enregistrement des utilisateurs dans le fichier
        saveUsers();

        // un message de bienvenue
        showAlert(Alert.AlertType.INFORMATION, layout.getScene().getWindow(), "Inscription réussi!", "Bonjour " + prenom + " " + nom);
        startScreen(mainScene, j); // créer une page pour cet utilisateur
    }

    public static void saveUsers() {
        try {
            FileOutputStream f = new FileOutputStream(new File(usersFile));
            ObjectOutputStream s = new ObjectOutputStream(f);
            s.writeObject(users);
            f.close();
            s.close();
        } catch (Exception e) {
            //
        }
    }


    public void loadUsers() {
        try {
            FileInputStream f = new FileInputStream(new File(usersFile));
            ObjectInputStream s = new ObjectInputStream(f);
            users = (HashMap<Integer, Joueur>) s.readObject();
            f.close();
            s.close();
        } catch (Exception e) {
            //
        }
        if(users.size()>0) Joueur.n = users.size()+1;
    }

    public void login(String prenom, String nom, String password, GridPane layout, Scene mainScene) {

        // la même chose que register
        if(prenom.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Entrez votre prénom s'il vous plait");
            return;
        }
        if(nom.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Entrez votre nom s'il vous plait");
            return;
        }
        if(password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Entrez votre mot de passe s'il vous plait");
            return;
        }

        //mais cette fois, nous voulons laisser l'utilisateur entrer s'il existe dans le HashMap users ..
        if (!userExists(prenom, nom)) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Cet utilisateur n'existe pas");
            return;
        }
        
        // et si leur mot de passe est correct
        if (!checkPassword(prenom, nom, password)) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Mot de passe incorrect");
            return;
        }

        // un message de bienvenue
        showAlert(Alert.AlertType.INFORMATION, layout.getScene().getWindow(), "Identification réussite!", "Bonjour "+prenom+" "+nom);
        
        int i = getJoueur(prenom, nom, password);
        Joueur j = users.get(i);
        // initialisation des themes
        initThemes(j);

        startScreen(mainScene,j); // créer une page pour cet utilisateur
    }

    public boolean userExists(String f, String l) {
        for (Integer i: users.keySet()){
            String fn = users.get(i).getPrenom();  
            String ln = users.get(i).getNom();  
            if (f.equals(fn)&&l.equals(ln)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPassword(String f, String l, String password) {
        for (Integer i: users.keySet()){
            String fn = users.get(i).getPrenom();  
            String ln = users.get(i).getNom();  
            String pw = users.get(i).getPassword();
            if (f.equals(fn)&&l.equals(ln)&&password.equals(pw)) {
                return true;
            }
        }
        return false;
    }

    public int getJoueur(String f, String l, String p) {
        for (Integer i: users.keySet()){
            String fn = users.get(i).getPrenom();  
            String ln = users.get(i).getNom();  
            String pw = users.get(i).getPassword();
            int num = users.get(i).getNum();
            if (f.equals(fn)&&l.equals(ln)&&p.equals(pw)) {
                return num;
            }
        }
        return 0;
    }





    // création de la première page que les utilisateurs voient lorsqu'ils s'identifient ou s'inscrivent
    public void startScreen(Scene mainScene, Joueur j) {
        VBox layout = new VBox(10);
        Scene thisScene = new Scene(layout,640,320);
        layout.setAlignment(Pos.CENTER);
        layout.setMinWidth(150);

        Button play = new Button("Jouer");
        Button user = new Button("Utilisateur");
        Button logout = new Button("Se déconnecter");

        play.setMinWidth(layout.getMinWidth());
        user.setMinWidth(layout.getMinWidth());
        logout.setMinWidth(layout.getMinWidth());
        layout.getChildren().addAll(play,user,logout);

        play.setOnAction(e -> {
            initGame(thisScene,j);
        });
        user.setOnAction(e -> {
            userInfo(thisScene,j);
        });
        logout.setOnAction(e -> {
            window.setScene(mainScene);
        });

        window.setScene(thisScene);
    }

    // la page où l'utilisateur peut voir et modifier ses informations
    public void userInfo(Scene prevScene, Joueur j) {
        GridPane gridPane = new GridPane();
        Scene userPage = new Scene(gridPane,640,320);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);


        Label headerLabel = new Label("Détails de l'utilisateur");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        Label nameLabel = new Label("Prénom : ");
        gridPane.add(nameLabel, 0,1);

        TextField prenomField = new TextField();
        prenomField.setPrefHeight(40);
        gridPane.add(prenomField, 1,1);

        Label nomLabel = new Label("Nom : ");
        gridPane.add(nomLabel, 0, 2);

        TextField nomField = new TextField();
        nomField.setPrefHeight(40);
        gridPane.add(nomField, 1, 2);

        Label passwordLabel = new Label("Mot de passe : ");
        gridPane.add(passwordLabel, 0, 3);

        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 3);

        Label dateLabel = new Label("D. de naissance : ");
        gridPane.add(dateLabel, 0, 4);

        DatePicker dateField = new DatePicker();
        gridPane.add(dateField, 1, 4);

        HBox box = new HBox(50);

        Button editButton = new Button("Modifier");
        editButton.setPrefHeight(40);
        editButton.setMinWidth(100);

        Button cancelButton = new Button("Retourner");
        cancelButton.setPrefHeight(40);
        cancelButton.setMinWidth(100);

        box.getChildren().addAll(editButton,cancelButton);

        gridPane.add(box, 1, 5, 2, 1);
        GridPane.setHalignment(box, HPos.CENTER);

        editButton.setOnAction(event -> {
            editButtonClick(prenomField, nomField, passwordField, dateField, editButton, cancelButton, j, prevScene);
        });
        cancelButton.setOnAction(event -> {
            window.setScene(prevScene);
        });


        prenomField.setText(j.getPrenom());
        nomField.setText(j.getNom());
        passwordField.setText(j.getPassword());
        dateField.setValue(j.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        prenomField.setDisable(true);
        nomField.setDisable(true);
        passwordField.setDisable(true);
        dateField.setDisable(true);

        window.setScene(userPage);
    }

    // lorsque l'utilisateur clique sur le bouton modifier
    public void editButtonClick(TextField prenomField, TextField nomField, PasswordField passwordField, DatePicker dateField, Button editButton, Button cancelButton, Joueur j, Scene prevScene) {
        prenomField.setDisable(false);
        nomField.setDisable(false);
        passwordField.setDisable(false);
        dateField.setDisable(false);

        editButton.setText("Sauvegarder");
        cancelButton.setText("Annuler");

        cancelButton.setOnAction(e -> {
            
            cancelButton.setText("Retourner");
            editButton.setText("Modifier");
            editButton.setOnAction(ev -> {
                editButtonClick(prenomField, nomField, passwordField, dateField, editButton, cancelButton, j, prevScene);
            });
            cancelButton.setOnAction(ev -> {
                window.setScene(prevScene);
            });

            prenomField.setDisable(true);
            nomField.setDisable(true);
            passwordField.setDisable(true);
            dateField.setDisable(true);
        });

        editButton.setOnAction(e -> {
            String f = prenomField.getText();
            String l = nomField.getText();
            if ((!f.equals(j.getPrenom())&&!l.equals(j.getNom()))&&userExists(f,l)) {
                showAlert(Alert.AlertType.ERROR, window.getScene().getWindow(), "Erreur", "Cet utilisateur existe déjà"); 
            } else {
                ZoneId defaultZoneId = ZoneId.systemDefault();
                Date date = Date.from(dateField.getValue().atStartOfDay(defaultZoneId).toInstant());
                if (date.after(new Date())) {
                    showAlert(Alert.AlertType.ERROR, window.getScene().getWindow(), "Erreur", "Date de naissance invalide"); 
                } else {
                    j.setData(prenomField.getText(), nomField.getText(), passwordField.getText(), date);
                    
                    saveUsers();

                    editButton.setText("Modifier");
                    cancelButton.setText("Retourner");
                    editButton.setOnAction(ev -> {
                        editButtonClick(prenomField, nomField, passwordField, dateField, editButton, cancelButton, j, prevScene);
                    });
                    cancelButton.setOnAction(ev -> {
                        window.setScene(prevScene);
                    });
                    prenomField.setDisable(true);
                    nomField.setDisable(true);
                    passwordField.setDisable(true);
                    dateField.setDisable(true);
                }
            }
        });
    }

    // lorsque l'utilisateur clique sur le bouton Jouer
    public static void initGame(Scene prevScene, Joueur j) {
        buttonsScene = prevScene;
        VBox mainGameLayout = new VBox(10);
        mainGameLayout.setAlignment(Pos.CENTER);
        mainGameLayout.setPrefWidth(150);
        mainGame = new Scene(mainGameLayout,640,320);

        for (int i = 0; i < 5; i++) {
            int niveau = i+1;
            Button btn = new Button("Niveau "+(niveau));
            if (j.getNiveau()>=i||i==0) btn.setDisable(false);
            else btn.setDisable(true);
            btn.setMinWidth(mainGameLayout.getPrefWidth());
            mainGameLayout.getChildren().add(btn);
            btn.setOnAction(e -> {
                PartieJeu[][] parties = new PartieJeu[6][5]; // toutes les parties possibles
                Iterator<ThemeJeu> it = themes.iterator();
                int k = 0;
                while (it.hasNext()) {
                    ThemeJeu theme = it.next();
                    LinkedList<Question> questions = theme.getAllQuestions();
                    for (int n = 1; n <= 5; n++) {
                        parties[n][k] = new PartieJeu(theme, questions.get(n-1).getNumQuest());
                        // parties[niveau][numero du question]
                    }
                    k++;
                }
                // la creation du partie
                j.Jouer(parties, niveau, 0, false);
            });
        }

        Button goBack = new Button("Retourner");
        goBack.setMinWidth(mainGameLayout.getPrefWidth());
        mainGameLayout.getChildren().add(goBack);

        goBack.setOnAction(e -> {
            window.setScene(prevScene);
        });

        window.setScene(mainGame);
    }
    
    public void initThemes(Joueur j) {
        themes.clear();
        Question.resetIntegers();

        for (int i = 0; i < 5; i++) {
            LinkedList<Question> quest = j.getQuestions(questionsList[i]);
            ThemeJeu theme = createOneTheme(allThemes[i], Joueur.coef[i], quest);
            themes.add(theme);
        }
    }

    public void initQuestionsList(Button reg, Button log) {
        try {
            questions = loadQuestions(questionsFile);
        } catch (IOException e) {
            if (reg!=null) {
                reg.setDisable(true);
                log.setDisable(true);
            }
            questionsFile = "";
            return;
        }
        for (int i = 0; i < 5; i++) {
            questionsList[i] = createQuestionList(questions[i], allThemes[i]);
        }
    }

    public String[][][] loadQuestions(String fileName) throws IOException {
        String theWholeFile = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        String[] data = theWholeFile.split("--------------------------------------------------");
        String[][][] questions = new String[5][10][3]; // questions[theme][numquestion][question,réponse,image]
        for (int i = 0; i < 10; i++) {
            String[] lns = data[i].split("\\r?\\n");
            String[] lines = new String[6];
            int ii = 0;
            for (String line : lns) {
                if (line!=null&&line.length()>0) {
                    lines[ii] = line;
                    ii++;
                }
            }

            String[] qr;
            if (i<5) Joueur.coef[i] = Integer.parseInt(lines[0]);
            for (int j = 1; j < 6; j++) { // partir de 1 car la ligne 0 est le coefficient du thème
                if (lines[j].length()>3) {
                    qr = lines[j].split("##",-1);
                    for (int k = 0; k < qr.length; k++) {
                        int a = 0;
                        if (i>4) a+=5;
                        questions[i%5][j-1+a][k] = qr[k];
                    }
                }
            }
        }
        return questions;
    }
    
    public ThemeJeu createOneTheme(String type, int coef, LinkedList<Question> questions) {
        ThemeJeu theme = new ThemeJeu(type, coef, questions);
        return theme;
    }

    public LinkedList<Question> createQuestionList(String[][] questions, String theme) {
        LinkedList<Question> list = new LinkedList<Question>();
        for (int i = 0; i < questions.length; i++) {
            String image = "";
            if (questions[i][2]!=null){
                image = questions[i][2];
            }
            Question q = createQuestion(questions[i][0], theme, questions[i][1], i+1, image, i);
            list.add(q);
        }
        return list;
    }

    public Question createQuestion(String label, String type, String reponse, int niveau, String image, int num) {
        type = type.substring(0, 3).replace("é", "e").toUpperCase();
        if (num<5) return new QuestionAdulte(label, type, reponse, niveau, image);
        else return new QuestionEnfant(label, type, reponse, niveau, image);
    }


    

    public void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public void saveDataFile(Boolean f) {
        // cela crée/charge un fichier "eureka.data" dans le dossier du programme
        // ce fichier contenant les deux dernières questionsFile, imagesFolder et usersFile saisient
        // vous n'ayez pas à sélectionner le fichier de questions, le dossier d'images et le fichier des utilisateurs à chaque fois que vous ouvrez le programme

        File file = new File(System.getProperty("user.dir") + "\\eureka.data");
        if (file.exists()&&f) {
            try {
                String theWholeFile = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "\\eureka.data")));
                String[] data = theWholeFile.split("@@");
                questionsFile = data[0];
                imagesFolder = data[1];
                usersFile = data[2];
            } catch (IOException e1) {
                //
            }
        } else { // if file not found
            String data = questionsFile + "@@" + imagesFolder + "@@" + usersFile + "@@";
            saveTextToFile(data, file);
        }
    }
}