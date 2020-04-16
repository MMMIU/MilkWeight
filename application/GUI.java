/**
 *
 */
package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
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
    // Widths.
    private static final int WINDOW_WIDTH = 900;
    private static final int LEFTBOX_WIDTH = 100;
    private static final int CENTERBOX_WIDTH = 650;
    private static final int SEARCHBARBUTTON_WIDTH = 75;
    private static final int RIGHTBOX_WIDTH = 150;
    // Heights.
    private static final int WINDOW_HEIGHT = 500;
    private static final int PROGRESSBAR_Height = 30;
    private static final int TOPBOX_HEIGHT = 50;
    private static final int CENTERBOX_HEIGHT = 350;
    private static final int BUTTOMBOX_HEIGHT = 70;

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
	// Fist pane.
	BorderPane pane = new BorderPane();

	// Top VBox
	VBox topVBox = new VBox();
	topVBox.setMinHeight(TOPBOX_HEIGHT);
	pane.setTop(topVBox);

	// Left VBox
	VBox leftVBox = new VBox();
	leftVBox.setMinWidth(LEFTBOX_WIDTH);
	pane.setLeft(leftVBox);

	// Center VBox.
	VBox centerVBox = new VBox(15);
	centerVBox.setMinWidth(CENTERBOX_WIDTH);
	centerVBox.setMinHeight(CENTERBOX_HEIGHT);
	// SearchBar HBox.
	HBox searchBarHBox = new HBox();
	// Text field for file addresses.
	TextField textfield = new TextField();
	textfield.setPromptText("Enter or Choose files...");
	textfield.setMinWidth(CENTERBOX_WIDTH - 3 * SEARCHBARBUTTON_WIDTH);
	textfield.setEditable(false);
	// FileChooser.
	FileChooser fileChooser = new FileChooser();
	fileChooser.setTitle("Choose files to open");
	// Button to open fileChooser.
	Button fileChooserBtn = new Button("Choose");
	fileChooserBtn.setMinWidth(SEARCHBARBUTTON_WIDTH);
	Button fileClearBtn = new Button("Clear");
	fileClearBtn.setMinWidth(SEARCHBARBUTTON_WIDTH);
	// Button to open fileChooser.
	Button fileOpenBtn = new Button("Open");
	fileOpenBtn.setMinWidth(SEARCHBARBUTTON_WIDTH);
	// Add components to SearchBar.
	searchBarHBox.getChildren().addAll(textfield, fileChooserBtn, fileClearBtn, fileOpenBtn);
	// TableView.
	ObservableList<OneRecord> tableData = FXCollections.observableArrayList(this.database.getAllRecords());
	TableView<OneRecord> table = new TableView<>();
	table.setMinWidth(CENTERBOX_WIDTH);
	table.setMinHeight(CENTERBOX_HEIGHT - 50);
	TableColumn<OneRecord, String> nameCol = new TableColumn<>("Farm ID");
	nameCol.setMinWidth(100);
	nameCol.setCellValueFactory(new PropertyValueFactory<>("farmID"));

	TableColumn<OneRecord, Integer> dateCol = new TableColumn<>("Date");
	dateCol.setMinWidth(100);
	dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

	TableColumn<OneRecord, Integer> weightCol = new TableColumn<>("Weight");
	weightCol.setMinWidth(200);
	weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

	table.setItems(tableData);
	table.getColumns().addAll(nameCol, dateCol, weightCol);
	// Add SearchBar and table to centerVBox.
	centerVBox.getChildren().addAll(searchBarHBox, table);
	centerVBox.setMinWidth(CENTERBOX_WIDTH);
	// Add centerVBox to the pane.
	pane.setCenter(centerVBox);

	// Right VBox
	VBox rightVBox = new VBox(15);
	rightVBox.setMinWidth(RIGHTBOX_WIDTH);
	rightVBox.setAlignment(Pos.TOP_CENTER);
	VBox rightTopSpace = new VBox();
	rightTopSpace.setMinHeight(100);
	Button addBtn = new Button("Add");
	addBtn.setMinHeight(30);
	addBtn.setMinWidth(100);
	Button deleteButton = new Button("Delete");
	deleteButton.setMinHeight(30);
	deleteButton.setMinWidth(100);
	rightVBox.getChildren().addAll(rightTopSpace, addBtn, deleteButton);
	// Add rightVBox to the pane.
	pane.setRight(rightVBox);

	// Bottom Panel.
	HBox buttomHBox = new HBox(15);
	buttomHBox.setMinHeight(BUTTOMBOX_HEIGHT);
	buttomHBox.setPadding(new Insets(10));
	Button exitBtn = new Button("Exit");
	exitBtn.setMinHeight(30);
	exitBtn.setMinWidth(100);
	Button nextBtn = new Button("Next");
	nextBtn.setMinHeight(30);
	nextBtn.setMinWidth(100);

	DropShadow shadow = new DropShadow();
	// exitBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
	// exitBtn.setEffect(shadow);
	// });
	// exitBtn.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
	// exitBtn.setEffect(null);
	// });

	buttomHBox.getChildren().addAll(exitBtn, nextBtn);
	buttomHBox.setAlignment(Pos.BOTTOM_RIGHT);
	pane.setBottom(buttomHBox);

	// Add pane and progrressBar to the vbox.
	VBox vbox = new VBox();
	vbox.getChildren().addAll(this.ProgrressBar(1), pane);
	// Display.
	primaryStage.setScene(new Scene(vbox, WINDOW_WIDTH, WINDOW_HEIGHT));

	// Button operations.
	List<File> files = new ArrayList<File>();
	// FileChooser button.
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
	// Exit Button
	exitBtn.setOnAction((ActionEvent e) -> {
	    primaryStage.close();
	    System.exit(0);
	});
	// Next Button
	nextBtn.setOnAction((ActionEvent e) -> {
	    this.DataDisplayScene(primaryStage);
	});
    }

    private void DataDisplayScene(Stage primaryStage) {
	VBox vbox = new VBox();
	// Display.
	primaryStage.setScene(new Scene(vbox, WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    private void OutputScene(Stage primaryStage) {
    }

    private boolean addBtnWindow() {

	return false;
    }

    private HBox ProgrressBar(int page) {
	HBox result = new HBox();
	result.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
	result.setMinHeight(PROGRESSBAR_Height);
	Label label1 = new Label();
	label1.setText("1.Input");
	label1.setMinWidth(100);
	label1.setMinHeight(PROGRESSBAR_Height);
	label1.setAlignment(Pos.CENTER);
	Label label2 = new Label();
	label2.setText("2.Display");
	label2.setMinWidth(100);
	label2.setMinHeight(PROGRESSBAR_Height);
	label2.setAlignment(Pos.CENTER);
	Label label3 = new Label();
	label3.setText("3.Output");
	label3.setMinWidth(100);
	label3.setMinHeight(PROGRESSBAR_Height);
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
