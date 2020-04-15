/**
 *
 */
package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author Yifei Miao
 *
 */
public class GUI extends Application {
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 500;
    private static final int SEARCHBOX_WIDTH = 600;
    private static final String APP_TITLE = "Milk Weight Manager-Ateam 37";
    private Database database;
    private FileManager fileManager;
    private FileOutputer fileOutputer;

    @Override
    public void start(Stage primaryStage) throws Exception {
	this.database = new Database();
	this.fileManager = new FileManager(this.database);
	this.fileOutputer = new FileOutputer(this.database);
	this.FileManagerScene(primaryStage);
	primaryStage.setTitle(APP_TITLE);
	primaryStage.setResizable(false);
	primaryStage.show();
    }

    private void FileManagerScene(Stage primaryStage) {
	BorderPane pane = new BorderPane();
	// Top VBox
	VBox topVBox = new VBox();
	topVBox.setPrefHeight(50);
	pane.setTop(topVBox);
	;
	// Left VBox
	VBox leftVBox = new VBox();
	leftVBox.setPrefWidth(100);
	pane.setLeft(leftVBox);
	// Center VBox.
	VBox centerVBox = new VBox();
	centerVBox.setPrefWidth(SEARCHBOX_WIDTH);

	// SearchBar HBox.
	HBox searchBarHBox = new HBox();
	// Text field for file addresses.
	TextField textfield = new TextField();
	textfield.setPromptText("Enter or Choose files...");
	textfield.setPrefWidth(450);
	textfield.setEditable(false);
	// FileChooser.
	FileChooser fileChooser = new FileChooser();
	fileChooser.setTitle("Choose files to open");
	// Button to open fileChooser.
	Button fileChooserBtn = new Button("Choose");
	fileChooserBtn.setPrefWidth(75);
	// Button to open fileChooser.
	Button fileOpenBtn = new Button("Open");
	fileOpenBtn.setPrefWidth(75);
	// Add components to SearchBar.
	searchBarHBox.getChildren().addAll(textfield, fileChooserBtn, fileOpenBtn);

	// Add SearchBar and list to centerVBox.
	centerVBox.getChildren().addAll(searchBarHBox);
	centerVBox.setPrefWidth(SEARCHBOX_WIDTH);

	// Add centerVBox to the pane.
	pane.setCenter(centerVBox);

	// Add pane and progrressBar to the vbox.
	VBox vbox = new VBox();
	vbox.getChildren().addAll(this.ProgrressBar(1), pane);
	// Display.
	primaryStage.setScene(new Scene(vbox, WINDOW_WIDTH, WINDOW_HEIGHT));

	// Button operations.
	List<File> files = new ArrayList<File>();
	fileChooserBtn.setOnAction(event -> {
	    List<File> tmpfiles = fileChooser.showOpenMultipleDialog(primaryStage);
	    if (tmpfiles != null) {
		for (File e : tmpfiles) {
		    if (!files.contains(e)) {
			files.add(e);
		    }
		}
	    }
	    if (files.size() != 0) {
		String text = "";
		for (int i = 0; i < files.size() - 1; i++) {
		    text += files.get(i).getName() + ",";
		}
		text += files.get(files.size() - 1).getName().toString();
		textfield.setText(text);
	    }
	});
	fileOpenBtn.setOnAction(event -> {
	    if (files.size() > 0) {
		textfield.clear();
	    }
	});
    }

    private void DataDisplayScene(Stage primaryStage) {
    }

    private void OutputScene(Stage primaryStage) {
    }

    private HBox ProgrressBar(int page) {
	HBox result = new HBox();
	result.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
	Label label1 = new Label();
	label1.setText("1.Input and edit");
	label1.setPrefWidth(100);
	label1.setPrefHeight(30);
	label1.setAlignment(Pos.CENTER);
	Label label2 = new Label();
	label2.setText("2.Display");
	label2.setPrefWidth(100);
	label2.setPrefHeight(30);
	label2.setAlignment(Pos.CENTER);
	Label label3 = new Label();
	label3.setText("3.Output");
	label3.setPrefWidth(100);
	label3.setPrefHeight(30);
	label3.setAlignment(Pos.CENTER);
	if (page == 1) {
	    label1.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
	    label1.setTextFill(Color.WHITE);
	} else if (page == 2) {
	    label2.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
	    label2.setTextFill(Color.WHITE);
	} else if (page == 3) {
	    label3.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
	    label3.setTextFill(Color.WHITE);
	}
	result.getChildren().addAll(label1, label2, label3);
	return result;
    }
}
