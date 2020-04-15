/**
 *
 */
package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author Yifei Miao
 *
 */
public class GUI extends Application {
    private static final int WINDOW_WIDTH = 960;
    private static final int WINDOW_HEIGHT = 540;
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
	// Center VBox.
	VBox centerVBox = new VBox();

	// SearchBar HBox.
	HBox searchBarHBox = new HBox();
	// Text field for file addresses.
	TextField textfield = new TextField();
	textfield.setPromptText("Enter or Choose files...");
	// FileChooser.
	FileChooser fileChooser = new FileChooser();
	fileChooser.setTitle("Choose files to open");
	// Button to open fileChooser.
	Button fileChooserbtn = new Button("Open");
	// Add components to SearchBar.
	searchBarHBox.getChildren().addAll(textfield, fileChooserbtn);

	// Add SearchBar and list to centerVBox.
	centerVBox.getChildren().addAll(searchBarHBox);
	centerVBox.setPrefWidth(SEARCHBOX_WIDTH);

	// Add centerVBox to the pane.
	pane.setCenter(centerVBox);

	// Add pane and progrressBar to the vbox.
	VBox vbox = new VBox();
	vbox.getChildren().addAll(pane);
	// Display.
	primaryStage.setScene(new Scene(vbox, WINDOW_WIDTH, WINDOW_HEIGHT));

	// Button operations.
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
		List<File> tmpfiles = fileChooser.showOpenMultipleDialog(primaryStage);
		if (tmpfiles != null) {
		    for (File e : tmpfiles) {
			if (!files.contains(e)) {
			    files.add(e);
			}
		    }
		}
	    }
	});
    }

    private void DataDisplayScene(Stage primaryStage) {
    }

    private void OutputScene(Stage primaryStage) {
    }

    private HBox ProgrressBar(int page) {
	return null;
    }
}
