package EUREKA.com.usthb;

import EUREKA.com.usthb.dessin.*;
import EUREKA.com.usthb.modeles.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.SwingUtilities;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class EUREKA extends Application {
    HashMap<Integer, Joueur> users = new HashMap<Integer, Joueur>();
    HashSet<ThemeJeu> themes = new HashSet<ThemeJeu>();
    Stage window;
    Scene mainGame;
    //String imagesFolder = "file:///C:/Users/Nadhir/Desktop/Docs/JavaFx/com/usthb/";
    //String questionsFile = "C:\\Users\\Me\\Desktop\\Docs\\JavaFx\\com\\usthb\\questions.txt";
    String imagesFolder = "";
    String questionsFile = "";
    String[] allThemes = new String[] { "Histoire", "Géographie", "Santé", "Culture Générale", "Islam" };
    String[][][] questions = new String[5][5][3];
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;

        VBox mainLayout = new VBox(20);
        Scene mainScene = new Scene(mainLayout, 640, 320);

        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPrefWidth(150);

        Button regButton = new Button("S'inscrire");
        Button loginButton = new Button("S'identifier");
        Button settingsButton = new Button("Préférences");
        Button aboutBtn = new Button("À propos");
        regButton.setMinWidth(mainLayout.getPrefWidth());
        loginButton.setMinWidth(mainLayout.getPrefWidth());
        settingsButton.setMinWidth(mainLayout.getPrefWidth());
        aboutBtn.setMinWidth(mainLayout.getPrefWidth());
        
        regButton.setOnAction(e -> {
            createRegisterPage(mainScene);
        });
        loginButton.setOnAction(e -> {
            createLoginPage(mainScene);
        });
        settingsButton.setOnAction(e -> {
            createSettingsPage(mainScene,mainLayout);
        });
        aboutBtn.setOnAction(e -> {
            createAboutPage(mainScene);
        });

        mainLayout.getChildren().addAll(regButton, loginButton, settingsButton, aboutBtn);
        mainLayout.requestFocus();

        window.setScene(mainScene);
        window.setResizable(false);
        window.sizeToScene(); // because setResizable() adds some weird margins idk why

        window.setTitle("EUREKA");


        initThemes(regButton,loginButton);

        window.show();
    }
    
    public void createRegisterPage(Scene mainScene) {
        GridPane regLayout = new GridPane();
        Scene regScene = new Scene(regLayout, 640, 320);
        regLayout.setAlignment(Pos.CENTER);
        regLayout.setPadding(new Insets(20, 20, 20, 20));
        regLayout.setHgap(20);
        regLayout.setVgap(20);
        ColumnConstraints culumn1 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        culumn1.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints column2 = new ColumnConstraints(200,200, Double.MAX_VALUE);
        column2.setHgrow(Priority.ALWAYS);

        regLayout.getColumnConstraints().addAll(culumn1, column2);
        // Add Header
        Label headerLabel = new Label("S'inscrire");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        regLayout.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        // Add Name Label
        Label fnameLabel = new Label("Prénom : ");
        regLayout.add(fnameLabel, 0,1);

        // Add Name Text Field
        TextField fnameField = new TextField();
        fnameField.setPrefHeight(40);
        regLayout.add(fnameField, 1,1);


        // Add Email Label
        Label lnameLabel = new Label("Nom : ");
        regLayout.add(lnameLabel, 0, 2);

        // Add Email Text Field
        TextField lnameField = new TextField();
        lnameField.setPrefHeight(40);
        regLayout.add(lnameField, 1, 2);

        // Add Password Label
        Label passwordLabel = new Label("Mot de passe : ");
        regLayout.add(passwordLabel, 0, 3);

        // Add Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        regLayout.add(passwordField, 1, 3);
        // Date
        Label dateLabel = new Label("D. de naissaince : ");
        regLayout.add(dateLabel, 0, 4);

        DatePicker dateField = new DatePicker();
        regLayout.add(dateField, 1, 4);

        // Add Submit And Cancel Button
        HBox box = new HBox(50);
        Button submitButton = new Button("S'inscrire");
        submitButton.setPrefHeight(40);
        submitButton.setMinWidth(100);
        Button cancelButton = new Button("Annuler");
        cancelButton.setPrefHeight(40);
        cancelButton.setMinWidth(100);
        box.getChildren().addAll(submitButton,cancelButton);
        regLayout.add(box, 1, 5, 2, 1);
        GridPane.setHalignment(box, HPos.CENTER);
        submitButton.setOnAction(event -> {
            register(fnameField.getText(),lnameField.getText(),passwordField.getText(),dateField.getValue(),regLayout,mainScene);
        });
        cancelButton.setOnAction(event -> {
            window.setScene(mainScene);
        });
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

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints column2 = new ColumnConstraints(200,200, Double.MAX_VALUE);
        column2.setHgrow(Priority.ALWAYS);

        loginLayout.getColumnConstraints().addAll(culumn1, column2);
        // Add Header
        Label headerLabel = new Label("S'identifier");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        loginLayout.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        // Add Name Label
        Label fnameLabel = new Label("Prénom : ");
        loginLayout.add(fnameLabel, 0,1);

        // Add Name Text Field
        TextField fnameField = new TextField();
        fnameField.setPrefHeight(40);
        loginLayout.add(fnameField, 1,1);


        // Add Email Label
        Label lnameLabel = new Label("Nom : ");
        loginLayout.add(lnameLabel, 0, 2);

        // Add Email Text Field
        TextField lnameField = new TextField();
        lnameField.setPrefHeight(40);
        loginLayout.add(lnameField, 1, 2);

        // Add Password Label
        Label passwordLabel = new Label("Mot de passe : ");
        loginLayout.add(passwordLabel, 0, 3);

        // Add Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        loginLayout.add(passwordField, 1, 3);

        // Add Submit And Cancel Button
        HBox box = new HBox(50);
        Button submitButton = new Button("S'identifier");
        submitButton.setPrefHeight(40);
        submitButton.setMinWidth(100);
        Button cancelButton = new Button("Annuler");
        cancelButton.setPrefHeight(40);
        cancelButton.setMinWidth(100);
        box.getChildren().addAll(submitButton,cancelButton);
        loginLayout.add(box, 1, 5, 2, 1);
        GridPane.setHalignment(box, HPos.CENTER);
        submitButton.setOnAction(event -> {
            login(fnameField.getText(),lnameField.getText(),passwordField.getText(),loginLayout,mainScene);
        });
        cancelButton.setOnAction(event -> {
            window.setScene(mainScene);
        });
        loginLayout.setAlignment(Pos.CENTER);
        window.setScene(loginScene);
    }

    public void createSettingsPage(Scene mainScene, VBox mainLayout) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        Scene settings = new Scene(layout,640,320);


        FileChooser questFile = new FileChooser();
        questFile.getExtensionFilters().addAll(new ExtensionFilter("Text", "*.txt"));
        
        HBox question = new HBox(5);
        question.setAlignment(Pos.CENTER);
        TextField questionPath = new TextField();
        Button chooseQuestFile = new Button("Sélectionner le fichier de questions");
        chooseQuestFile.setOnAction(e -> {
            File file = questFile.showOpenDialog(window); 
            if (file != null) { 
                questionPath.setText(file.getAbsolutePath()); 
            } 
        });
        question.getChildren().addAll(questionPath,chooseQuestFile);

        DirectoryChooser imageFile = new DirectoryChooser();
        questFile.getExtensionFilters().addAll(new ExtensionFilter("Text", "*.txt"));
        
        HBox image = new HBox(5);
        image.setAlignment(Pos.CENTER);
        TextField imagePath = new TextField();
        Button chooseImgFolder = new Button("Sélectionner le dossier des images");
        chooseImgFolder.setOnAction(e -> {
            File file = imageFile.showDialog(window); 
            if (file != null) { 
                imagePath.setText(file.getAbsolutePath()); 
            }
        });
        image.getChildren().addAll(imagePath,chooseImgFolder);


        questionPath.setMinWidth(320);
        imagePath.setMinWidth(320);
        chooseQuestFile.setMinWidth(220);
        chooseImgFolder.setMinWidth(220);


        HBox buttons = new HBox(20);
        Button save = new Button("Sauvegarder");
        Button cancel = new Button("Retourner");
        buttons.getChildren().addAll(save,cancel);
        buttons.setAlignment(Pos.CENTER);

        save.setMinWidth(100);
        cancel.setMinWidth(100);
        save.setOnAction(e -> {
            if(!questionPath.getText().isEmpty()) {
                questionsFile = questionPath.getText();
                mainLayout.getChildren().get(0).setDisable(false); // register button
                mainLayout.getChildren().get(1).setDisable(false); // login button
                initThemes((Button)mainLayout.getChildren().get(0),(Button)mainLayout.getChildren().get(1)); // getting login and reg button
            }
            if(!imagePath.getText().isEmpty()) imagesFolder = "file:///"+imagePath.getText().replace("\\", "/")+"/";
            window.setScene(mainScene);
        });
        cancel.setOnAction(e -> {
            window.setScene(mainScene);
        });


        Button manual = new Button("Saisir les questions");
        manual.setMinWidth(250);
        manual.setOnAction(e -> {
            questionsFile = questionPath.getText();
            initThemes(null,null);
            writeQuestions(questionPath);
        });

        questionPath.setText(questionsFile);
        if (!imagesFolder.isEmpty()) {
            imagePath.setText(imagesFolder.substring(8,imagesFolder.length()-1).replace("/", "\\"));   
        }
        questionPath.setEditable(false);
        imagePath.setEditable(false);

        layout.getChildren().addAll(question,image,manual,buttons);
        layout.requestFocus();
        window.setScene(settings);
    }

    public void writeQuestions(TextField questionPathField) {
        Stage newWindow = new Stage();
        VBox box = new VBox(10);
        GridPane layout = new GridPane();
        //layout.setGridLinesVisible(true);
        layout.setPadding(new Insets(30,30,30,30));
        Scene scene = new Scene(box,720,480);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            if(i==0) colConst.setPercentWidth(50);
            else colConst.setPercentWidth(25);
            colConst.setHalignment(HPos.CENTER);
            layout.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < 6; i++) {
            RowConstraints row1 = new RowConstraints();
            if(i==0) row1.setPercentHeight(10);
            else row1.setPercentHeight(18);
            layout.getRowConstraints().add(row1);
        }

        Text text = new Text();
        text.setFont(Font.font(48));

        Text q = new Text("Question");
        Text r = new Text("Réponse");
        Text i = new Text("Image");

        q.setFont(Font.font(18));
        r.setFont(Font.font(18));
        i.setFont(Font.font(18));

        layout.add(q,0,0);
        layout.add(r,1,0);
        layout.add(i,2,0);

        layout.setHgap(10);

        TextField[] questionFields = new TextField[] {
            new TextField(),
            new TextField(),
            new TextField(),
            new TextField(),
            new TextField()
        };
        TextField[] reponseFields = new TextField[] {
            new TextField(),
            new TextField(),
            new TextField(),
            new TextField(),
            new TextField()
        };
        TextField[] imageFields = new TextField[] {
            new TextField(),
            new TextField(),
            new TextField(),
            new TextField(),
            new TextField()
        };
        for (int j = 1; j < 6; j++) {
            layout.add(questionFields[j-1], 0, j);
        }
        for (int j = 1; j < 6; j++) {
            layout.add(reponseFields[j-1], 1, j);
        }
        for (int j = 1; j < 6; j++) {
            layout.add(imageFields[j-1], 2, j);
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
        
        buttons.getChildren().addAll(next,prev);
        

        HBox coefBox = new HBox(5);
        coefBox.setMinWidth(100);
        coefBox.setAlignment(Pos.CENTER);
        Text coefText = new Text("Coefficient :");
        TextField coefField = new TextField("1");
        coefBox.getChildren().addAll(coefText,coefField);



        createPages(0,questionFields,reponseFields,imageFields,text,next,prev,newWindow,questionPathField,coefField); // 0 is page 0



        layout.setAlignment(Pos.CENTER);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(text,layout,coefBox,buttons);

        newWindow.setScene(scene);
        newWindow.setResizable(false);
        newWindow.show();
    }

    public void createPages(int k, TextField[] questionFields, TextField[] reponseFields, TextField[] imageFields, Text text, Button next, Button prev, Stage newWindow, TextField questionPathField, TextField coefField) {
        text.setText(allThemes[k]);
        for (int i = 0; i < 5; i++) {
            questionFields[i].setText(questions[k][i][0]);
            reponseFields[i].setText(questions[k][i][1]);
            imageFields[i].setText(questions[k][i][2]);
        }
        coefField.setText(Integer.toString(Joueur.coef[k]));
        if (k==0) {
            prev.setText("Annuler");
            prev.setOnAction(e -> {
                questions = null;
                newWindow.close();
            });
        } else {
            prev.setText("Précédent");
            prev.setOnAction(e -> {
                for (int i = 0; i < 5; i++) {
                    questions[k][i][0] = questionFields[i].getText();
                    questions[k][i][1] = reponseFields[i].getText();
                    questions[k][i][2] = imageFields[i].getText();
                }
                if (coefField.getText().isEmpty()) Joueur.coef[k] = 1;
                else Joueur.coef[k] = Integer.parseInt(coefField.getText());
                createPages(k-1, questionFields, reponseFields, imageFields, text, next, prev, newWindow, questionPathField, coefField);
            });
        }
        if (k<4) {
            next.setText("Suivant");
            next.setOnAction(e -> {
                for (int i = 0; i < 5; i++) {
                    if (questionFields[i].getText()==null) {
                        showAlert(Alert.AlertType.ERROR, newWindow, "Erreur", "Veuillez remplir tous les champs de 'question'");
                        return;
                    }
                    if (reponseFields[i].getText()==null) {
                        showAlert(Alert.AlertType.ERROR, newWindow, "Erreur", "Veuillez remplir tous les champs de 'réponse'");
                        return;
                    }
                    if (coefField.getText()==null) {
                        showAlert(Alert.AlertType.ERROR, newWindow, "Erreur", "Veuillez remplir le champs de coefficient");
                        return;
                    }
                    questions[k][i][0] = questionFields[i].getText();
                    questions[k][i][1] = reponseFields[i].getText();
                    questions[k][i][2] = imageFields[i].getText();
                }
                Joueur.coef[k] = Integer.parseInt(coefField.getText());

                createPages(k+1, questionFields, reponseFields, imageFields, text, next, prev, newWindow, questionPathField, coefField);
            });
        } else {
            next.setText("Sauvegarder");
            next.setOnAction(e -> {
                for (int i = 0; i < 5; i++) {
                    if (questionFields[i].getText()==null) {
                        showAlert(Alert.AlertType.ERROR, newWindow, "Erreur", "Veuillez remplir tous les champs de 'question'");
                        return;
                    }
                    if (reponseFields[i].getText()==null) {
                        showAlert(Alert.AlertType.ERROR, newWindow, "Erreur", "Veuillez remplir tous les champs de 'réponse'");
                        return;
                    }
                    if (coefField.getText()==null) {
                        showAlert(Alert.AlertType.ERROR, newWindow, "Erreur", "Veuillez remplir le champs de coefficient");
                        return;
                    }
                    questions[k][i][0] = questionFields[i].getText();
                    questions[k][i][1] = reponseFields[i].getText();
                    questions[k][i][2] = imageFields[i].getText();
                }
                Joueur.coef[k] = Integer.parseInt(coefField.getText());

                questionsFile = saveQuestionsFile(newWindow);
                questionPathField.setText(questionsFile);
                newWindow.close();
            });
        }
    }

    public String saveQuestionsFile(Stage newWindow) {
        String theFile = new String();
        for (int i = 0; i < 5; i++) {
            theFile += Joueur.coef[i];
            for (int j = 0; j < 5; j++) {
                theFile+= "\n"+questions[i][j][0];
                theFile+= "##"+questions[i][j][1];
                if(questions[i][j][2]!=null) theFile+="##"+questions[i][j][2];
            }
            if(i<4) theFile+="\n--------------------------------------------------\n";
        }
        FileChooser f = new FileChooser();
        f.getExtensionFilters().addAll(new ExtensionFilter("Text", "*.txt"));
        File file = f.showSaveDialog(newWindow);
        if (file != null) {
            saveTextToFile(theFile, file);
        }
        return file==null ? "" : file.getAbsolutePath();
    }

    public void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void createAboutPage(Scene mainScene) {
        VBox abt = new VBox(20);
        Scene about = new Scene(abt,640,320);
        Text text = new Text("eureka bla bla bla\ni made this okay whatever\nname and stuff here\ndata here");
        text.setFont(Font.font(18));
        //text.setTextAlignment(TextAlignment.CENTER);;
        Button close = new Button("Fermer");
        close.setMinWidth(100);
        close.setOnAction(event -> {
            window.setScene(mainScene);
        });
        abt.getChildren().addAll(text,close);
        abt.setAlignment(Pos.CENTER);
        window.setScene(about);
    }






    public void register(String fname, String lname, String password, LocalDate bdate, GridPane layout, Scene mainScene) {
        if(fname.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Entrez votre prénom s'il vous plait");
            return;
        }
        if(lname.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Entrez votre nom s'il vous plait");
            return;
        }
        if(password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Entrez votre mot de passe s'il vous plait");
            return;
        }
        if(password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Entrez votre mot de passe s'il vous plait");
            return;
        }
        if (bdate == null) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Entrez votre date de naissance s'il vous plait");
            return;
        }

        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date date = Date.from(bdate.atStartOfDay(defaultZoneId).toInstant());
        if (date.after(new Date())) { // if he's born in the future lol
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Date de naissance invalide");
            return;
        }

        Joueur j = new Joueur(fname, lname, date, password);

        if (!users.isEmpty()&&userExists(fname,lname)) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Cet utilisateur existe déjà");
            return;
        }

        users.put(j.getNum(),j);
        showAlert(Alert.AlertType.INFORMATION, layout.getScene().getWindow(), "Inscription réussi!", "Bonjour "+fname+" "+lname);
        startScreen(mainScene,j);
    }   

    public void login(String fname, String lname, String password, GridPane layout, Scene mainScene) {
        if(fname.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Entrez votre prénom s'il vous plait");
            return;
        }
        if(lname.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Entrez votre nom s'il vous plait");
            return;
        }
        if(password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Entrez votre mot de passe s'il vous plait");
            return;
        }
        if (!userExists(fname, lname)) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Cet utilisateur n'existe pas");
            return;
        }
        if (!checkPassword(fname, lname, password)) {
            showAlert(Alert.AlertType.ERROR, layout.getScene().getWindow(), "Erreur", "Mot de passe incorrect");
            return;
        }
        showAlert(Alert.AlertType.INFORMATION, layout.getScene().getWindow(), "Identification réussite!", "Bonjour "+fname+" "+lname);
        int i = getJoueur(fname, lname, password);
        startScreen(mainScene,users.get(i));
    }

    public boolean userExists(String f, String l) {
        for (Integer i: users.keySet()){
            String fn = users.get(i).getFName();  
            String ln = users.get(i).getLName();  
            if (f.equals(fn)&&l.equals(ln)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPassword(String f, String l, String password) {
        for (Integer i: users.keySet()){
            String fn = users.get(i).getFName();  
            String ln = users.get(i).getLName();  
            String pw = users.get(i).getPassword();
            if (f.equals(fn)&&l.equals(ln)&&password.equals(pw)) {
                return true;
            }
        }
        return false;
    }

    public int getJoueur(String f, String l, String p) {
        for (Integer i: users.keySet()){
            String fn = users.get(i).getFName();  
            String ln = users.get(i).getLName();  
            String pw = users.get(i).getPassword();
            int num = users.get(i).getNum();
            if (f.equals(fn)&&l.equals(ln)&&p.equals(pw)) {
                return num;
            }
        }
        return 0;
    }






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

    public void userInfo(Scene prevScene, Joueur j) {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();
        Scene userPage = new Scene(gridPane,640,320);
        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // Add Column Constraints

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);


        Label headerLabel = new Label("Détails de l'utilisateur");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        // Add FName Label
        Label nameLabel = new Label("Prénom : ");
        gridPane.add(nameLabel, 0,1);

        // Add FName Text Field
        TextField fnameField = new TextField();
        fnameField.setPrefHeight(40);
        gridPane.add(fnameField, 1,1);


        // Add LName Label
        Label lnameLabel = new Label("Nom : ");
        gridPane.add(lnameLabel, 0, 2);

        // Add LName Text Field
        TextField lnameField = new TextField();
        lnameField.setPrefHeight(40);

        gridPane.add(lnameField, 1, 2);

        // Add Password Label
        Label passwordLabel = new Label("Mot de passe : ");
        gridPane.add(passwordLabel, 0, 3);

        // Add Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 3);

        // Date
        Label dateLabel = new Label("D. de naissance : ");
        gridPane.add(dateLabel, 0, 4);

        DatePicker dateField = new DatePicker();
        gridPane.add(dateField, 1, 4);


        // Add Submit And Cancel Button
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
            editButtonClick(fnameField, lnameField, passwordField, dateField, editButton, cancelButton, j, prevScene);
        });
        cancelButton.setOnAction(event -> {
            window.setScene(prevScene);
        });


        fnameField.setText(j.getFName());
        lnameField.setText(j.getLName());
        passwordField.setText(j.getPassword());
        dateField.setValue(j.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        fnameField.setDisable(true);
        lnameField.setDisable(true);
        passwordField.setDisable(true);
        dateField.setDisable(true);

        window.setScene(userPage);
    }

    public void editButtonClick(TextField fnameField, TextField lnameField, PasswordField passwordField, DatePicker dateField, Button editButton, Button cancelButton, Joueur j, Scene prevScene) {
        fnameField.setDisable(false);
        lnameField.setDisable(false);
        passwordField.setDisable(false);
        dateField.setDisable(false);

        editButton.setText("Sauvegarder");
        cancelButton.setText("Annuler");

        cancelButton.setOnAction(e -> {
            
            cancelButton.setText("Retourner");
            editButton.setText("Modifier");
            editButton.setOnAction(ev -> {
                editButtonClick(fnameField, lnameField, passwordField, dateField, editButton, cancelButton, j, prevScene);
            });
            cancelButton.setOnAction(ev -> {
                window.setScene(prevScene);
            });

            fnameField.setDisable(true);
            lnameField.setDisable(true);
            passwordField.setDisable(true);
            dateField.setDisable(true);
        });

        editButton.setOnAction(e -> {
            String f = fnameField.getText();
            String l = lnameField.getText();
            if ((!f.equals(j.getFName())&&!l.equals(j.getLName()))&&userExists(f,l)) {
                showAlert(Alert.AlertType.ERROR, window.getScene().getWindow(), "Erreur", "Cet utilisateur existe déjà"); 
            } else {
                ZoneId defaultZoneId = ZoneId.systemDefault();
                Date date = Date.from(dateField.getValue().atStartOfDay(defaultZoneId).toInstant());
                if (date.after(new Date())) {
                    showAlert(Alert.AlertType.ERROR, window.getScene().getWindow(), "Erreur", "Date de naissance invalide"); 
                } else {
                    j.setData(fnameField.getText(), lnameField.getText(), passwordField.getText(), date);
                    editButton.setText("Modifier");
                    cancelButton.setText("Retourner");
                    editButton.setOnAction(ev -> {
                        editButtonClick(fnameField, lnameField, passwordField, dateField, editButton, cancelButton, j, prevScene);
                    });
                    cancelButton.setOnAction(ev -> {
                        window.setScene(prevScene);
                    });
                    fnameField.setDisable(true);
                    lnameField.setDisable(true);
                    passwordField.setDisable(true);
                    dateField.setDisable(true);
                }
            }
        });
    }






    public void initGame(Scene prevScene, Joueur j) {
        VBox mainGameLayout = new VBox(10);
        mainGameLayout.setAlignment(Pos.CENTER);
        mainGameLayout.setPrefWidth(150);
        mainGame = new Scene(mainGameLayout,640,320);
        Iterator<ThemeJeu> i = themes.iterator();
        while (i.hasNext()) {
            ThemeJeu theme = i.next();
            String txt = theme.getThemeText();
            Button btn = new Button(txt);
            btn.setMinWidth(mainGameLayout.getPrefWidth());
            mainGameLayout.getChildren().add(btn);
            btn.setOnAction(e -> {
                String questionKey = txt.substring(0,3).replace("é", "e").toUpperCase();
                int lastlevel = j.getLastLevel(questionKey);
                if(lastlevel==5) {
                    // here the player answered all questions of this theme
                    // what do you want to do ?
                    // think about it later nigga
                    Stage alertWindow = new Stage();
                    VBox alertLayout = new VBox(5);
                    alertLayout.setAlignment(Pos.CENTER);
                    Scene alertScene = new Scene(alertLayout,320,160);
                    mainGameLayout.setDisable(true);
                    Text theText = new Text("Vous avez terminé tous les niveaux de ce thème.\nVoulez-vous le réinitialiser?");
                    Button reset = new Button("Réinitialiser");
                    Button cancel = new Button("Annuler");
                    reset.setMinWidth(100);
                    cancel.setMinWidth(100);
                    reset.setOnAction(event -> {
                        j.resetLevel(questionKey);
                        PartieJeu p = new PartieJeu(theme, questionKey+1);
                        createGame(p,j);
                        mainGameLayout.setDisable(false);
                        alertWindow.close();
                    });
                    cancel.setOnAction(event -> {
                        mainGameLayout.setDisable(false);
                        alertWindow.close();
                    });
                    alertLayout.getChildren().addAll(theText,reset,cancel);
                    alertWindow.setScene(alertScene);
                    alertWindow.show();
                    alertWindow.setOnCloseRequest(event -> {
                        mainGameLayout.setDisable(false);
                    });
                } else {
                    PartieJeu p = new PartieJeu(theme, questionKey+(lastlevel+1));
                    createGame(p,j);
                }
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
    
    public void createGame(PartieJeu p, Joueur j) {
        GridPane startGame = new GridPane();
        //startGame.setGridLinesVisible(true);
        startGame.setPadding(new Insets(5, 5, 5, 5));
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
        row1.setPercentHeight(20);
        startGame.getRowConstraints().add(row1);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(80);
        startGame.getRowConstraints().add(row2);

        Text titleText = new Text(p.getThemeText());
        titleText.setFont(Font.font(42));
        titleText.setStyle("-fx-font-weight: bold");

        VBox userText = new VBox();
        userText.setAlignment(Pos.CENTER);
        Text welcome = new Text("Bonjour !");
        welcome.setFont(Font.font(14));
        StackPane userPane = new StackPane();
        userPane.setAlignment(Pos.CENTER);
        userPane.setMinWidth(210);

        Text fname = new Text(j.getFName());
        Text lname = new Text(j.getLName());
        fname.setFont(Font.font(16));
        fname.setStyle("-fx-font-weight: bold");
        lname.setFont(Font.font(16));
        lname.setStyle("-fx-font-weight: bold");
        userText.getChildren().addAll(welcome,fname,lname);

        startGame.add(titleText, 1, 0);
        startGame.add(userText, 0, 0);
        Button help = new Button("?");
        help.setOnAction(e -> {
            showHelp();
        });
        startGame.add(help,2,0);


        VBox details = new VBox();
        details.setPadding(new Insets(30,30,30,30));
        setDetails(details,j);
        startGame.add(details,0,1);


        // SETTING THE POTENCE
        Potence potence = new Potence();
        VBox potenceBody = new VBox(20);
        SwingNode potenceNode = new SwingNode();
        Text potenceText = new Text("Reste 8 coups à jouer !");
        createSwingContent(potenceNode,potence);
        potenceText.setFont(Font.font(16));
        potenceText.setText("Reste 8 coups à jouer !");
        potenceBody.getChildren().addAll(potenceNode,potenceText);
        potenceBody.setPadding(new Insets(-20,5,5,5));
        potenceBody.setAlignment(Pos.CENTER);
        Button playAgain = new Button("Rejouer ?");
        playAgain.setOnAction(e -> {
            PartieJeu partie = new PartieJeu(p.getTheme(), p.getNumQuest());
            createGame(partie, j);
        });
        playAgain.setVisible(false);
        potenceBody.getChildren().add(playAgain);

        VBox questionBody = new VBox(10);
        questionBody.setPadding(new Insets(5,5,5,5));
        questionBody.setAlignment(Pos.CENTER);
        questionBody.setMinWidth(343);

        TextField txt1 = new TextField();
        TextField txt2 = new TextField();
        setQuestionBody(questionBody,p,txt1,txt2,potence,potenceText,potenceBody,j,details);
        questionBody.setStyle("-fx-background-fill: black, white ; -fx-background-insets: 0, 0 1 1 0 ;");
        startGame.add(questionBody,1,1);
        startGame.add(potenceBody,2,1);

        Scene start = new Scene(startGame,720,560); // OI NIGGA YOU FORGOT TO INCLUDE THAT PartieJeu SHIT DO IT !!
        window.setScene(start);                     // ALRIGHT I DID IT STOP SCREAMING YOU ASSHOLE !!
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
    
    public void setDetails(VBox details, Joueur j) {
        details.getChildren().clear();
        Text level = new Text ("Niveaux :");
        level.setFont(Font.font(16));
        level.setStyle("-fx-font-weight: bold");
        level.setUnderline(true);
        details.getChildren().add(level);
        Iterator<ThemeJeu> i = themes.iterator();
        while (i.hasNext()) {
            String theme = i.next().getThemeText();
            Text txt = new Text(theme+" : "+j.getLastLevel(theme.substring(0,3).replace("é", "e").toUpperCase()));
            txt.setFont(Font.font(14));
            details.getChildren().add(txt);
        }
        Text totalScore = new Text("\nVotre score total:");
        totalScore.setFont(Font.font(16));
        totalScore.setUnderline(true);
        Text score = new Text();
        score.setFont(Font.font(26));
        score.setText(Integer.toString(j.getTotalScore())+"\n"); // to add some space before the next button
        score.setStyle("-fx-font-weight: bold");
        Button close = new Button("Fermer");
        close.setMinWidth(100);
        close.setAlignment(Pos.CENTER);
        close.setOnAction(event -> {
            window.setScene(mainGame);
        });
        details.getChildren().addAll(totalScore,score,close);
    }
    
    public void setQuestionBody(VBox body, PartieJeu p, TextField txt1, TextField txt2, Potence potence, Text potenceText, VBox potenceBody, Joueur j, VBox details) {
        String text = p.getQuestion();
        String reponse = p.getQuestionReponse();
        String image = p.getImage();
        Text questNum = new Text("Question "+p.getNumQuest().substring(3)+" :");
        questNum.setFont(Font.font(14));
        body.getChildren().add(questNum);

        Text question = new Text(text);
        question.setFont(Font.font(16));
        question.setTextAlignment(TextAlignment.CENTER);
        question.wrappingWidthProperty().bind(body.widthProperty());
        
        if (!image.isEmpty()&&!imagesFolder.isEmpty()) { // if there is is an image
            ImageView img = new ImageView(imagesFolder+p.getImage());
            img.setFitWidth(343); // reset image width to 343 px
            img.setFitHeight(170); // image height to
            img.setPreserveRatio(true); // preserve :
            body.getChildren().add(img);
        }
        body.getChildren().add(question);


        txt1.setMaxWidth(250);
        txt2.setMaxWidth(250);
        txt1.setAlignment(Pos.CENTER);
        txt1.setEditable(false);
        txt2.setAlignment(Pos.CENTER);

        txt1.setStyle("-fx-highlight-fill: null; -fx-highlight-text-fill: null;"); // to disable selection
        txt2.setStyle("-fx-highlight-fill: null; -fx-highlight-text-fill: null;"); // either with mouse or keyboard

        Text r1 = new Text("La réponse");
        Text r2 = new Text("Votre réponse");
        String rep = reponse.replaceAll("(?s).", "*");
        txt1.setText(rep);
        txt1.setFont(Font.font(16));
        txt2.setFont(Font.font(16));

        Boolean[] found = new Boolean[rep.length()]; // this thing helps to get indexes of characters that occured more than once
        for (int i = 0; i < rep.length(); i++) {
            found[i] = false;
        }

        txt2.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length()>=oldValue.length()) { // if the user didn't press BACKSPACE
                p.setReponse(newValue);
                input(p,txt1,txt2,found,potence,potenceText,potenceBody,j,details);
            }
        });
        body.getChildren().addAll(r1,txt1,r2,txt2);
    }
    
    public void input(PartieJeu p, TextField txt1, TextField txt2, Boolean[] found, Potence potence, Text text, VBox potenceBody, Joueur j, VBox details) {
        StringBuffer reponse = p.getReponse();
        if(reponse.length()>0) {
            char c = Character.toUpperCase(reponse.charAt(reponse.length()-1));
            boolean foundThisChar = false;
            if (p.checkCharacter(c)) {
                int index = p.getQuestionReponse().toUpperCase().indexOf(c); // i'm using uppercase cuz java is case sensitive
                while(index<p.getQuestionReponse().length()) {
                    if (!found[index]&&p.getQuestionReponse().toUpperCase().charAt(index)==c) break;
                    else index ++;
                }
                if (index<p.getQuestionReponse().length()){
                    found[index] = true;
                    foundThisChar = true;
                    String str = txt1.getText().substring(0, index) + p.getQuestionReponse().charAt(index) + txt1.getText().substring(index + 1);
                    txt1.setText(str);
                }
            }
            if (txt1.getText().equals(p.getTheme().getQuestion(p.getNumQuest()).getReponse())) {
                potence.setTrouve(true);
                txt2.setEditable(false);
                reponseTrouve(potenceBody,j,p,details);
            }
            if(!foundThisChar) { // if the character the user wrote isn't among the *****
                // draw from potence and decrease lives (increace ETAT) etc ...
                potence.repaint();
                potence.incrementEtat();
            }
            if (potence.getEtat() == 8) {
                text.setText("Vous êtes pendu !");
                txt2.setEditable(false);
                potenceBody.getChildren().get(2).setVisible(true); // getting the play again button
            }
            else if (potence.getEtat() == 7) text.setText("Reste un coup à jouer !");
            else text.setText("Reste " + (8 - potence.getEtat()) + " coups à jouer");
        }
    }
    
    public void reponseTrouve(VBox potenceBody, Joueur j, PartieJeu p, VBox details) {
        
        j.increaseLevel(p.getNumQuest().substring(0, 3));

        potenceBody.getChildren().clear();
        Text text = new Text("Bravo!\nVous avez trouvé");
        text.setFont(Font.font(18));
        text.setTextAlignment(TextAlignment.CENTER);
        potenceBody.getChildren().add(text);
        if (j.getLastLevel(p.getNumQuest().substring(0, 3))==5) {
            Text t = new Text("Vous avez terminé tous\nles niveaux de ce thème");
            potenceBody.getChildren().add(t);
        } else {
            Button next = new Button("Niveau suivant");
            next.setOnAction(e -> {
                PartieJeu newGame = new PartieJeu(p.getTheme(), p.getNextQuest());
                createGame(newGame, j);
            });
            potenceBody.getChildren().add(next);
            setDetails(details, j);
        }
    }
    







    public void initThemes(Button reg, Button log) {
        themes.clear();
        Question.resetIntegers();
        try {
            questions = loadQuestions(questionsFile);   
        } catch (IOException e) {
            if (reg!=null) {
                reg.setDisable(true);
                log.setDisable(true);
            }
            questionsFile = "";
        }

        for (int i = 0; i < 5; i++) {
            LinkedList<Question> quest = createQuestionList(questions[i], allThemes[i]);
            ThemeJeu theme = createOneTheme(allThemes[i], Joueur.coef[i], quest); // that coef thing .. find a use for it
            themes.add(theme);
        }
    }

    public String[][][] loadQuestions(String fileName) throws IOException {
        String theWholeFile = new String(Files.readAllBytes(Paths.get(fileName)));
        String[] data = theWholeFile.split("--------------------------------------------------",-1);
        String[][][] questions = new String[5][5][3];
        for (int i = 0; i < data.length; i++) {
            String[] lines = data[i].split("\\r?\\n", -1);
            String[] qr; // question reponse
            int a = 1;
            if(i>0) a = 2;
            Joueur.coef[i] = Integer.parseInt(lines[a-1]);
            for (int j = a; j < lines.length; j++) { // START FROM TWO BECAUSE FIRST LINE IS \n AND THE SECOND LINE IS THE THEME NAME
                if (lines[j].length()>3) {
                    qr = lines[j].split("##",-1);
                    for (int k = 0; k < qr.length; k++) {
                        questions[i][j-a][k] = qr[k];
                    }
                }
            }
        }
        return questions;
    }
    
    public ThemeJeu createOneTheme(String type, int coef, LinkedList<Question> questions) {
        ThemeJeu theme = new ThemeJeu(type, type.substring(0, 3).replace("é", "e").toUpperCase(), coef, questions);
        return theme;
    }

    public LinkedList<Question> createQuestionList(String[][] questions, String theme) {
        LinkedList<Question> list = new LinkedList<Question>();
        for (int i = 0; i < questions.length; i++) {
            String image = "";
            if (questions[i][2]!=null){
                image = questions[i][2];
            }
            Question q = createQuestion(questions[i][0], theme, questions[i][1], i+1, image);
            list.add(q);
        }
        return list;
    }

    public Question createQuestion(String label, String type, String reponse, int niveau, String image) {
        Question q;
        type = type.substring(0, 3).replace("é", "e").toUpperCase();
        q = new Question(label, type, reponse, niveau, image);
        return q;
    }


    public void createSwingContent(SwingNode swingNode, Potence p) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNode.setContent(p);
            }
        });
    }

    public void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}