package application;

/**
 * Main.java implemented by Yifei Miao in p5
 *
 * Author: Yifei Miao (ymiao29@wisc.edu) Date: @date 2020/4/13 Version : 1.0.0
 *
 * Course: COMPSCI 400 Lecture Number: 001 Semester: Spring 2020
 *
 * List Collaborators: None
 */
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class test extends Application {
    private List<String> args;

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 500;
    private static final String APP_TITLE = "CS400 My First JavaFX Program";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // save args example
        this.args = this.getParameters().getRaw();

        //Set Pane.
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 30));

        //Top Panel.
        Label title = new Label(APP_TITLE);
        title.setFont(new Font("Times New Roman", 25));

        root.setTop(title);
        BorderPane.setAlignment(title, Pos.TOP_CENTER);

        //Left Panel.

        File imageFolder = new File("images");
        File[] images = imageFolder.listFiles();
        List<String> imagesName = new ArrayList<String>();
        for (File e : images) {
            String name = e.getName();
            if (name.substring(name.length() - 3).toLowerCase().equals("jpg")
                    || name.substring(name.length() - 3).toLowerCase()
                            .equals("png")) {
                imagesName.add(e.getName());
            }
        }
        ComboBox<String> comboBox = new ComboBox<String>();
        comboBox.getItems().addAll(imagesName);
        comboBox.setValue(imagesName.get(0));
        comboBox.setMaxWidth(200);
        comboBox.setMinWidth(200);

        root.setLeft(comboBox);

        //Center Panel.
        Image image = new Image("file:images/" + imagesName.get(0));
        ImageView imageView = new ImageView(image);
        comboBox.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Object>() {

                    @Override
                    public void changed(ObservableValue observable,
                            Object oldValue, Object newValue) {
                        Image tmpimage = new Image(
                                "file:images/" + newValue.toString());
                        imageView.setImage(tmpimage);
                        imageView.setFitWidth(200);
                        imageView.setFitHeight(200);
                    }
                });
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);

        root.setCenter(imageView);

        //Bottom Panel.
        Button btn = new Button();
        btn.setText("Done");
        btn.setOnAction((ActionEvent e) -> {
            primaryStage.close();
        });
        DropShadow shadow = new DropShadow();
        btn.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            btn.setEffect(shadow);
        });
        btn.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            btn.setEffect(null);
        });

        root.setBottom(btn);
        BorderPane.setAlignment(btn, Pos.BOTTOM_RIGHT);

        //Right Panel.
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        Label filesList = new Label();
        TextField textfield = new TextField();
        textfield.setPromptText("Enter or Choose files...");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        Button fileChooserbtn = new Button("Open");
        List<File> files = new ArrayList<File>();
        fileChooserbtn.setOnAction(event -> {
            if (!textfield.getText().equals("")) {
                String filePath = textfield.getText();
                textfield.setText("");
                File newFile = new File(filePath);
                if (!files.contains(newFile)) {
                    files.add(newFile);
                }
            } else {
                List<File> tmpfiles = fileChooser
                        .showOpenMultipleDialog(primaryStage);
                if (tmpfiles != null) {
                    for (File e : tmpfiles) {
                        if (!files.contains(e)) {
                            files.add(e);
                        }
                    }
                }
            }
            if (files.size() != 0) {
                String text = "File selected:\n";
                for (int i = 0; i < files.size(); i++) {
                    text += (i + 1) + ". " + files.get(i).getPath().toString()
                            + "\n";
                }
                filesList.setWrapText(true);
                filesList.setText(text);
            }
        });
        hbox.getChildren().addAll(textfield, fileChooserbtn);
        vbox.getChildren().addAll(hbox, filesList);
        vbox.setMinWidth(300);
        vbox.setMaxWidth(300);
        root.setRight(vbox);

        // Add the stuff and set the primary stage
        Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

}
