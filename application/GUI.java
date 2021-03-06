/*
 * GUI.java created by Yifei Miao in Milk Weight project.
 *
 * Author: Yifei Miao (ymiao29@wisc.edu) Date: 2020/04/21 Version : 1.0.0
 * 
 * Date 2020/4/28 Version: 1.1.0
 *
 * Course: COMPSCI 400 Lecture Number: 001 Semester: Spring 2020
 *
 */
package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Yifei Miao
 *
 */
public class GUI extends Application {
	// Final dimension members.
	private static final int WINDOW_WIDTH = 900;
	private static final int LEFTBOX_WIDTH = 100;
	private static final int CENTERBOX_WIDTH = 650;
	private static final int SEARCHBARBUTTON_WIDTH = 75;
	private static final int RIGHTBOX_WIDTH = 150;
	private static final int ADDWIDOW_WIDTH = 900;
	private static final int POPWIDOW_WIDTH = 250;
	private static final int WINDOW_HEIGHT = 500;
	private static final int PROGRESSBAR_Height = 30;
	private static final int TOPBOX_HEIGHT = 40;
	private static final int CENTERBOX_HEIGHT = 360;
	private static final int BUTTOMBOX_HEIGHT = 70;
	private static final int ADDWIDOW_HEIGHT = 35;
	private static final int POPWIDOW_HEIGHT = 200;
	private static final int IMAGEVIEW_WIDTH = 100;
	private static final int IMAGEVIEW_HEIGHT = 100;
	private static final int BUTTONORMAL_WIDTH = 100;
	private static final int BUTTONORMAL_HEIGHT = 30;
	private static final int TABELCOLUMNWIDTH = 200;

	// Private members.
	private FileManager fileManager;
	private FileOutputContentGenerator outputer;
	private static final String APP_TITLE = "Milk Weight Manager-Ateam 37";
	private static final String ADDER_TITLE = "Multiple Records Adder";
	private static final String ERROR_TITLE = "ERROR";
	private static final Label TEAMLABEL = new Label("Authors: Yifei Miao, Chenxi Gao, Chen Wang, Tianyi Zhao");
	private Database database;
	private List<Database> dataBaseHistory;
	private ObservableList<OneRecord> tableList;
	private List<List<OneRecord>> tableListHistory;
	private Label recordsCount, farmCount, weightCount, daysCount, earliestDate, latestDate;
	private Button fileOpenBtn, fileClearBtn, deleteButton, deleteAllButton, unDoButton, reDoButton, nextBtn, getBtn1,
			getBtn2, getBtn3, getBtn4, outputBtn1, outputBtn2, outputBtn3, outputBtn4;
	private TableView<OneRecord> tableView;
	private TextField searchBoxTextField;
	private TextArea textArea;
	private ComboBox<Integer> comboBoxYear1, comboBoxMonth2, comboBoxMonth3, comboBoxRange1, comboBoxRange2;
	private String output1Content, output2String, output3String, output4String, output1Name, output2Name, output3Name,
			output4Name;
	private int imageIndicator, historyIndicator;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.database = new Database();
		this.dataBaseHistory = new ArrayList<Database>();
		this.dataBaseHistory.add(new Database());
		this.tableList = FXCollections.observableArrayList();
		this.tableListHistory = new ArrayList<List<OneRecord>>();
		this.tableListHistory.add(new ArrayList<OneRecord>());
		this.historyIndicator = 0;
		this.fileManager = new FileManager(this.database);
		this.outputer = new FileOutputContentGenerator(database);
		this.FileManagerScene(primaryStage);
		primaryStage.setTitle(APP_TITLE);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	@SuppressWarnings("unchecked")
	private void FileManagerScene(Stage primaryStage) {
		// Main pane.
		BorderPane pane = new BorderPane();

		// Top VBox
		VBox topVBox = new VBox();
		topVBox.setMinHeight(TOPBOX_HEIGHT);
		pane.setTop(topVBox);

		// Left VBox
		VBox leftVBox = new VBox();
		leftVBox.setMinHeight(CENTERBOX_HEIGHT);
		leftVBox.setAlignment(Pos.BOTTOM_CENTER);
		leftVBox.setMinWidth(LEFTBOX_WIDTH);
		this.recordsCount = setALabel("Total Records:\n0");
		this.farmCount = setALabel("Total Farms:\n0");
		this.weightCount = setALabel("Total Weight:\n0");
		this.daysCount = setALabel("Total Days:\n0");
		this.earliestDate = setALabel("Earliset Date:\nUnknown");
		this.latestDate = setALabel("Lateset Date:\nUnknown");
		leftVBox.getChildren().addAll(recordsCount);
		leftVBox.getChildren().addAll(farmCount, weightCount, daysCount, earliestDate, latestDate);
		pane.setLeft(leftVBox);

		// Center VBox.
		VBox centerVBox = new VBox(15);
		centerVBox.setMinWidth(CENTERBOX_WIDTH);
		centerVBox.setMinHeight(CENTERBOX_HEIGHT);
		// SearchBar HBox.
		HBox searchBarHBox = new HBox();
		// Text field for file addresses.
		searchBoxTextField = new TextField();
		searchBoxTextField.setPromptText("Use \"Choose\" button to select files from the system...");
		searchBoxTextField.setMinWidth(CENTERBOX_WIDTH - 3 * SEARCHBARBUTTON_WIDTH);
		searchBoxTextField.setEditable(false);
		// FileChooser.
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose files to open");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
		// Button to open fileChooser.
		Button fileChooserBtn = new Button("Choose");
		fileChooserBtn.setMinWidth(SEARCHBARBUTTON_WIDTH);
		fileClearBtn = new Button("Clear");
		fileClearBtn.setMinWidth(SEARCHBARBUTTON_WIDTH);
		// Button to open files.
		fileOpenBtn = new Button("Open");
		fileOpenBtn.setMinWidth(SEARCHBARBUTTON_WIDTH);
		// Add components to SearchBar.
		searchBarHBox.getChildren().addAll(searchBoxTextField, fileChooserBtn, fileClearBtn, fileOpenBtn);
		// TableView.
		tableView = new TableView<>();
		tableView.requestFocus();
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableView.setMinWidth(CENTERBOX_WIDTH);
		tableView.setMinHeight(CENTERBOX_HEIGHT - 50);
		// Add columns.
		TableColumn<OneRecord, String> nameCol = new TableColumn<>("Farm ID");
		nameCol.setMinWidth(TABELCOLUMNWIDTH);
		nameCol.setCellValueFactory(new PropertyValueFactory<>("farmID"));

		TableColumn<OneRecord, Integer> dateCol = new TableColumn<>("Date");
		dateCol.setMinWidth(TABELCOLUMNWIDTH);
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

		TableColumn<OneRecord, Integer> weightCol = new TableColumn<>("Weight");
		weightCol.setMinWidth(TABELCOLUMNWIDTH);
		weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

		tableView.setItems(this.tableList);
		tableView.getColumns().addAll(nameCol, dateCol, weightCol);
		tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				this.deleteButton.setDisable(false);
			}
		});
		// Add SearchBar and table to centerVBox.
		centerVBox.getChildren().addAll(searchBarHBox, tableView);
		centerVBox.setMinWidth(CENTERBOX_WIDTH);
		// Add centerVBox to the pane.
		pane.setCenter(centerVBox);

		// Right VBox
		VBox rightVBox = new VBox(15);
		rightVBox.setMinWidth(RIGHTBOX_WIDTH);
		rightVBox.setAlignment(Pos.TOP_CENTER);
		// Top space and image.
		VBox rightTopSpace = new VBox();
		rightTopSpace.setMinHeight(100);
		rightTopSpace.setMinWidth(100);
		rightTopSpace.setAlignment(Pos.TOP_CENTER);
		List<String> imagesName = new ArrayList<String>();
		// Get images from image folder.
		File imageFolder = new File("images");
		File[] images = imageFolder.listFiles();
		for (File e : images) {
			String name = e.getName();
			if (name.substring(name.length() - 4).toLowerCase().equals(".jpg")
					|| name.substring(name.length() - 4).toLowerCase().equals(".png")) {
				imagesName.add(e.getName());
			}
		}
		// Add image to pane.
		Image image = null;
		if (imagesName.size() > 0) {
			image = new Image("file:images/" + imagesName.get(0));
		}
		ImageView imageView = new ImageView(image);
		Button imageButton = new Button("", imageView);
		if (imagesName.size() == 0) {
			imageButton.setDisable(true);
		}
		imageView.setFitWidth(IMAGEVIEW_WIDTH);
		imageView.setFitHeight(IMAGEVIEW_HEIGHT);
		imageButton.setPrefSize(IMAGEVIEW_WIDTH, IMAGEVIEW_HEIGHT);
		imageButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		rightTopSpace.getChildren().addAll(imageButton);
		// Buttons.
		Button addBtn = new Button("Add");
		addBtn.setMinHeight(30);
		addBtn.setMinWidth(100);
		deleteButton = new Button("Delete");
		deleteButton.setPrefSize(BUTTONORMAL_WIDTH, BUTTONORMAL_HEIGHT);
		deleteAllButton = new Button("Delete All");
		deleteAllButton.setPrefSize(BUTTONORMAL_WIDTH, BUTTONORMAL_HEIGHT);
		unDoButton = new Button("Undo");
		unDoButton.setPrefSize(BUTTONORMAL_WIDTH, BUTTONORMAL_HEIGHT);
		reDoButton = new Button("Redo");
		reDoButton.setPrefSize(BUTTONORMAL_WIDTH, BUTTONORMAL_HEIGHT);
		rightVBox.getChildren().addAll(rightTopSpace, addBtn, deleteButton, deleteAllButton, unDoButton, reDoButton);
		// Add rightVBox to the pane.
		pane.setRight(rightVBox);

		// Bottom Panel.
		HBox buttomHBox = new HBox(15);
		buttomHBox.setMinHeight(BUTTOMBOX_HEIGHT);
		buttomHBox.setPadding(new Insets(0, 10, 10, 0));
		Button exitBtn = new Button("Exit");
		exitBtn.setPrefSize(BUTTONORMAL_WIDTH, BUTTONORMAL_HEIGHT);
		nextBtn = new Button("Next");
		nextBtn.setPrefSize(BUTTONORMAL_WIDTH, BUTTONORMAL_HEIGHT);
		TEAMLABEL.setPadding(new Insets(0, 390, 0, 0));
		TEAMLABEL.setFont(new Font("Arial", 10));
		buttomHBox.getChildren().addAll(TEAMLABEL, exitBtn, nextBtn);
		buttomHBox.setAlignment(Pos.BOTTOM_RIGHT);
		pane.setBottom(buttomHBox);

		// Add pane and progrressBar to the vbox.
		VBox vbox = new VBox();
		vbox.getChildren().addAll(this.ProgrressBar(1), pane);
		// Display.
		refreshStats();
		refreshTable();
		enableButtons();
		primaryStage.setScene(new Scene(vbox, WINDOW_WIDTH, WINDOW_HEIGHT));

		// Button operations.
		// Choose button.
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
				searchBoxTextField.setText(text);
				this.fileOpenBtn.requestFocus();
			}
			enableButtons();
		});
		// Clear button.
		fileClearBtn.setOnAction(event -> {
			files.clear();
			searchBoxTextField.clear();
			enableButtons();
		});
		// Open button.
		fileOpenBtn.setOnAction(event -> {
			if (files.size() > 0) {
				searchBoxTextField.clear();
				List<File> errorFiles = this.fileManager.addFiles(files);
				refreshStats();
				this.refreshTable();
				if (errorFiles.size() < files.size()) {
					this.historyManager(true, false, false);
				}
				if (errorFiles.size() > 0) {
					String errorText = "";
					List<Integer> positions = this.fileManager.getErrorPositions();
					for (int i = 0; i < errorFiles.size(); i++) {
						errorText += "\n" + (i + 1) + ". " + errorFiles.get(i).getPath().toString();
						if (positions.get(i) > 0) {
							errorText += "\nAt line " + positions.get(i);
						} else {
							errorText += "\n File cannot be found";
						}
					}
					this.errorWindow("Error Reading from " + errorFiles.size()
							+ " file(s), it could be format errors or data errors: " + errorText);
				}
				files.clear();
			}
			enableButtons();
			this.deleteButton.setDisable(true);
		});
		// Image button used to change image.
		imageButton.setOnAction((e) -> {
			imageIndicator++;
			imageIndicator = imageIndicator % imagesName.size();
			Image tmpimage = new Image("file:images/" + imagesName.get(imageIndicator));
			imageView.setImage(tmpimage);
		});
		// Add button.
		addBtn.setOnAction((ActionEvent e) -> {
			this.addWindow(primaryStage);
			enableButtons();
		});
		// Delete button.
		deleteButton.setOnAction(event -> {
			ObservableList<Integer> list = tableView.getSelectionModel().getSelectedIndices();
			ObservableList<OneRecord> tmp = FXCollections.observableArrayList();
			for (Integer e : list) {
				this.database.remove(this.tableList.get(e).getFarmID(), this.tableList.get(e).getDate());
				tmp.add(this.tableList.get(e));
			}
			for (OneRecord e : tmp) {
				this.tableList.remove(e);
			}
			refreshStats();
			if (list.size() > 0) {
				this.historyManager(true, false, false);
			}
			enableButtons();
		});
		// DeleteAll button.
		deleteAllButton.setOnAction(event -> {
			files.clear();
			searchBoxTextField.clear();
			this.database.clear();
			this.refreshTable();
			refreshStats();
			this.historyManager(true, false, false);
			enableButtons();
		});
		// Undo button.
		unDoButton.setOnAction((ActionEvent e) -> {
			this.historyManager(false, true, false);
			refreshStats();
			enableButtons();
		});
		// Redo button.
		reDoButton.setOnAction((ActionEvent e) -> {
			this.historyManager(false, false, true);
			refreshStats();
			enableButtons();
		});
		// Exit Button.
		exitBtn.setOnAction((ActionEvent e) -> {
			primaryStage.close();
			System.exit(0);
		});
		// Next Button
		nextBtn.setOnAction((ActionEvent e) -> {
			this.DataDisplayScene(primaryStage);
		});
		fileChooserBtn.requestFocus();
	}

	/*
	 * Refresh tableView according to current database in the first page.
	 */
	private void refreshTable() {
		this.tableList.clear();
		for (OneRecord e : this.database.getAllRecords()) {
			this.tableList.add(new OneRecord(e.getFarmID(), e.getDate(), e.getWeight()));
		}
	}

	/*
	 * Refresh Statistics panel according to current database in the first page.
	 */
	private void refreshStats() {
		List<Integer> allDates = this.database.getAllDates();
		if (allDates.size() != 0) {
			recordsCount.setText("Total Records:\n" + this.database.size());
			farmCount.setText("Total Farms:\n" + this.database.getFarmIDList().size());
			weightCount.setText("Total Weight:\n" + this.database.totalWeight());
			daysCount.setText("Total Days:\n" + allDates.size());
			earliestDate.setText("Earliset Date:\n" + allDates.get(0));
			latestDate.setText("Lateset Date:\n" + allDates.get(allDates.size() - 1));
		} else {
			recordsCount.setText("Total Records:\n0");
			farmCount.setText("Total Farms:\n0");
			weightCount.setText("Total Weight:\n0");
			daysCount.setText("Total Days:\n0");
			earliestDate.setText("Earliset Date:\nUnknown");
			latestDate.setText("Lateset Date:\nUnknown");
		}
	}

	/*
	 * Pop up window for manually add records.
	 */
	private void addWindow(Stage primaryStage) {
		// HBox panel.
		HBox hBox = new HBox(15);
		hBox.setMinHeight(ADDWIDOW_HEIGHT);
		hBox.setPadding(new Insets(5));
		// Farm ID.
		Label farmID = new Label("Farm ID:");
		farmID.setAlignment(Pos.CENTER);
		farmID.setMinHeight(ADDWIDOW_HEIGHT - 5);
		TextField farmIDText = new TextField();
		// Date.
		Label date = new Label("Date:");
		date.setAlignment(Pos.CENTER);
		date.setMinHeight(ADDWIDOW_HEIGHT - 5);
		TextField dateText = new TextField();
		dateText.setPromptText("Example: 2020-4-30");
		// Weight.
		Label weight = new Label("Weight:");
		weight.setAlignment(Pos.CENTER);
		weight.setMinHeight(ADDWIDOW_HEIGHT - 5);
		TextField weightText = new TextField();
		// Add button.
		Button addBtn = new Button("Add");
		addBtn.setMinWidth(75);
		// Cancel button.
		Button cancelBtn = new Button("Cancel");
		cancelBtn.setMinWidth(75);
		// Add to scene and stage.
		hBox.getChildren().addAll(farmID, farmIDText, date, dateText, weight, weightText, addBtn, cancelBtn);
		Scene mainScene = new Scene(hBox, ADDWIDOW_WIDTH, ADDWIDOW_HEIGHT);
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(mainScene);
		stage.setTitle(ADDER_TITLE);
		stage.setResizable(false);
		stage.setX(primaryStage.getX());
		stage.setY(primaryStage.getY() + WINDOW_HEIGHT + 30);
		stage.show();

		// Add button operation.
		addBtn.setOnAction((ActionEvent e) -> {
			String[] data = { dateText.getText(), farmIDText.getText(), weightText.getText() };
			if (this.fileManager.checkData(data)) {
				OneRecord record = this.fileManager.generateRecordUsingData(data);
				this.database.add(record);
				this.tableList.add(0, record);
				historyManager(true, false, false);
			} else {
				this.errorWindow(
						"Incorrect data format or size, please check.\nSample input:farm x,2020-4-30,12345");
			}
			refreshStats();
			enableButtons();
		});
		// Cancel button operation.
		cancelBtn.setOnAction((ActionEvent e) -> {
			stage.close();
		});
	}

	/*
	 * Popup window to display errors and messages.
	 */
	private void popUpWindow(String title, String message) {
		VBox vBox = new VBox(10);
		vBox.setAlignment(Pos.TOP_CENTER);
		// Text area.
		TextArea textArea = new TextArea(message);
		textArea.setPrefSize(POPWIDOW_WIDTH, POPWIDOW_HEIGHT - 45);
		textArea.setWrapText(true);
		textArea.setEditable(false);
		// OK button.
		Button okBtn = new Button("OK");
		okBtn.setAlignment(Pos.CENTER);
		okBtn.setMinWidth(75);
		Stage errorWindowStage = new Stage();
		vBox.getChildren().addAll(textArea, okBtn);
		// Add to scene and stage.
		Scene mainScene = new Scene(vBox, POPWIDOW_WIDTH, POPWIDOW_HEIGHT);
		errorWindowStage.initModality(Modality.APPLICATION_MODAL);
		errorWindowStage.setScene(mainScene);
		errorWindowStage.setTitle(title);
		errorWindowStage.setResizable(false);
		errorWindowStage.show();
		// Return button operation.
		okBtn.setOnAction((ActionEvent e) -> {
			errorWindowStage.close();
		});
	}

	/*
	 * Popup window to display errors.
	 */
	private void errorWindow(String message) {
		popUpWindow(ERROR_TITLE, message);
	}

	/*
	 * Progress bar on top of the window.
	 */
	private HBox ProgrressBar(int page) {
		// HBox Panel.
		HBox result = new HBox();
		// Background color.
		result.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
		result.setMinHeight(PROGRESSBAR_Height);
		// First step.
		Label label1 = new Label();
		label1.setText("1.Input");
		label1.setPrefSize(100, PROGRESSBAR_Height);
		label1.setAlignment(Pos.CENTER);
		// Second step.
		Label label2 = new Label();
		label2.setText("2.Display & Output");
		label2.setPrefSize(150, PROGRESSBAR_Height);
		label2.setAlignment(Pos.CENTER);
		// Step selection.
		if (page == 1) {
			label1.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
			label1.setTextFill(Color.WHITE);
		} else if (page == 2) {
			label2.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
			label2.setTextFill(Color.WHITE);
		}
		// Add to panel.
		result.getChildren().addAll(label1, label2);
		return result;
	}

	/*
	 * Create a label to show status of the database in the first page.
	 */
	private Label setALabel(String init) {
		Label label = new Label(init);
		label.setPrefSize(90, 60);
		label.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
		label.setTextFill(Color.WHITE);
		label.setFont(new Font("Arial", 12));
		label.setTextAlignment(TextAlignment.CENTER);
		label.setAlignment(Pos.CENTER);
		return label;
	}

	/*
	 * Manage historical version of the program.
	 */
	private void historyManager(boolean addHistory, boolean undo, boolean redo) {
		// Add history.
		if (addHistory) {
			while (this.dataBaseHistory.size() > this.historyIndicator + 1) {
				this.dataBaseHistory.remove(this.dataBaseHistory.size() - 1);
				this.tableListHistory.remove(this.tableListHistory.size() - 1);
			}
			Database tmpDatabase = new Database();
			List<OneRecord> tmpList = new ArrayList<OneRecord>();
			for (OneRecord r : this.database.getAllRecords()) {
				tmpDatabase.add(new OneRecord(r.getFarmID(), r.getDate(), r.getWeight()));
			}
			for (OneRecord r : this.tableList) {
				tmpList.add(new OneRecord(r.getFarmID(), r.getDate(), r.getWeight()));
			}
			this.historyIndicator++;
			this.dataBaseHistory.add(tmpDatabase);
			this.tableListHistory.add(tmpList);
			enableButtons();
		}
		// Undo.
		else if (undo) {
			this.historyIndicator--;
			matchVersion();
			enableButtons();
		}
		// Redo.
		else if (redo) {
			this.historyIndicator++;
			matchVersion();
			enableButtons();
		}
	}

	/*
	 * Match current database and tableList version with the historical version
	 * indicated by the pointer.
	 */
	private void matchVersion() {
		this.database.clear();
		for (OneRecord r : this.dataBaseHistory.get(this.historyIndicator).getAllRecords()) {
			this.database.add(new OneRecord(r.getFarmID(), r.getDate(), r.getWeight()));
		}
		this.tableList.clear();
		for (OneRecord r : this.tableListHistory.get(this.historyIndicator)) {
			this.tableList.add(new OneRecord(r.getFarmID(), r.getDate(), r.getWeight()));
		}
	}

	/*
	 * Enable or disable buttons according to current status.
	 */
	private void enableButtons() {
		if (this.tableView.getSelectionModel().getSelectedIndices().size() > 0) {
			this.deleteButton.setDisable(false);
		} else {
			this.deleteButton.setDisable(true);
		}
		if (this.database.size() == 0) {
			this.deleteAllButton.setDisable(true);
			this.nextBtn.setDisable(true);
		} else {
			this.deleteAllButton.setDisable(false);
			this.nextBtn.setDisable(false);
		}
		if (this.historyIndicator == 0) {
			this.unDoButton.setDisable(true);
		} else {
			this.unDoButton.setDisable(false);
		}
		if (this.historyIndicator == this.dataBaseHistory.size() - 1) {
			this.reDoButton.setDisable(true);
		} else {
			this.reDoButton.setDisable(false);
		}
		if (this.searchBoxTextField.getText().equals("")) {
			this.fileOpenBtn.setDisable(true);
			this.fileClearBtn.setDisable(true);
		} else {
			this.fileOpenBtn.setDisable(false);
			this.fileClearBtn.setDisable(false);
		}
	}

	/*
	 * Data display and output scene.
	 */
	private void DataDisplayScene(Stage primaryStage) {
		// Layers.
		VBox mainPane = new VBox();
		mainPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		HBox upperPane = new HBox();
		upperPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT - 100);
		VBox bigLeftVBox = new VBox();
		bigLeftVBox.setPrefSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT - 100);
		VBox leftVBox = new VBox(15);
		leftVBox.setPadding(new Insets(10, 0, 0, 0));
		leftVBox.setPrefSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT - 150);
		bigLeftVBox.getChildren().add(leftVBox);
		textArea = new TextArea();
		textArea.setPromptText("Ready to display...");
		textArea.setMinHeight(WINDOW_HEIGHT - 100);
		textArea.setEditable(false);
		upperPane.getChildren().addAll(bigLeftVBox,
				new Line(GUI.WINDOW_WIDTH / 2 + 3, 0, GUI.WINDOW_WIDTH / 2 + 3, WINDOW_HEIGHT - 100), textArea);
		HBox buttomHBox = new HBox(15);
		buttomHBox.setPrefSize(WINDOW_WIDTH / 2, 100);
		// Left operation pane.
		// Line 1.
		HBox line1 = new HBox(15);
		line1.setPadding(new Insets(0, 0, 0, 10));
		Label label1 = new Label("FARM REPORT -- By month for specified farm and year:");
		label1.setPadding(new Insets(0, 0, 0, 10));
		// Farm ID selection.
		ComboBox<String> comboBoxFarm1 = new ComboBox<String>();
		comboBoxFarm1.getItems().addAll(this.database.getFarmIDList());
		comboBoxFarm1.setPromptText("Farm ID");
		comboBoxFarm1.setPrefSize(125, 30);
		// Enable and set next comboBox according to selection.
		comboBoxFarm1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				comboBoxYear1.getItems().clear();
				comboBoxYear1.getItems().addAll(database.getYearListOfAFarm(newValue.toString()));
				comboBoxYear1.setDisable(false);
				getBtn1.setDisable(true);
				outputBtn1.setDisable(true);
			}
		});
		// Year selection.
		comboBoxYear1 = new ComboBox<Integer>();
		comboBoxYear1.setPromptText("Year");
		comboBoxYear1.setPrefSize(125, 30);
		comboBoxYear1.setDisable(true);
		comboBoxYear1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				getBtn1.setDisable(false);
				outputBtn1.setDisable(true);
			}
		});
		// Get button.
		getBtn1 = new Button("Get");
		getBtn1.setPrefSize(50, 30);
		getBtn1.setDisable(true);
		outputBtn1 = new Button("Output");
		outputBtn1.setPrefSize(80, 30);
		outputBtn1.setDisable(true);
		line1.getChildren().addAll(comboBoxFarm1, comboBoxYear1, getBtn1, outputBtn1);
		leftVBox.getChildren().addAll(label1, line1, new Line(0, 30, GUI.WINDOW_WIDTH / 2, 30));
		// Line 2.
		HBox line2 = new HBox(15);
		line2.setPadding(new Insets(0, 0, 0, 10));
		Label label2 = new Label("MONTHLY FARM REPORT -- By farms for specified month and year:");
		label2.setPadding(new Insets(0, 0, 0, 10));
		// Year selection.
		ComboBox<Integer> comboBoxYear2 = new ComboBox<Integer>();
		comboBoxYear2.getItems().addAll(this.database.getYearList());
		comboBoxYear2.setPromptText("Year");
		comboBoxYear2.setPrefSize(125, 30);
		comboBoxYear2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				comboBoxMonth2.getItems().clear();
				comboBoxMonth2.getItems().addAll(database.getMonthListOfAYear(Integer.parseInt(newValue.toString())));
				comboBoxMonth2.setDisable(false);
				getBtn2.setDisable(true);
				outputBtn2.setDisable(true);
			}
		});
		// Month selection.
		comboBoxMonth2 = new ComboBox<Integer>();
		comboBoxMonth2.setPromptText("Month");
		comboBoxMonth2.setPrefSize(125, 30);
		comboBoxMonth2.setDisable(true);
		comboBoxMonth2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				getBtn2.setDisable(false);
				outputBtn2.setDisable(true);
			}
		});
		// Get button.
		getBtn2 = new Button("Get");
		getBtn2.setPrefSize(50, 30);
		getBtn2.setDisable(true);
		outputBtn2 = new Button("Output");
		outputBtn2.setPrefSize(80, 30);
		outputBtn2.setDisable(true);
		line2.getChildren().addAll(comboBoxYear2, comboBoxMonth2, getBtn2, outputBtn2);
		leftVBox.getChildren().addAll(label2, line2, new Line(0, 30, GUI.WINDOW_WIDTH / 2, 30));
		// Line 3.
		HBox line3 = new HBox(15);
		line3.setPadding(new Insets(0, 0, 0, 10));
		Label label3 = new Label("ANNUAL/MONTHLY REPORT -- Each farm's share of total(Month optional): ");
		label3.setPadding(new Insets(0, 0, 0, 10));
		// Year selection.
		ComboBox<Integer> comboBoxYear3 = new ComboBox<Integer>();
		comboBoxYear3.getItems().addAll(this.database.getYearList());
		comboBoxYear3.setPromptText("Year");
		comboBoxYear3.setPrefSize(125, 30);
		comboBoxYear3.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				comboBoxMonth3.getItems().clear();
				comboBoxMonth3.getItems().addAll(database.getMonthListOfAYear(Integer.parseInt(newValue.toString())));
				comboBoxMonth3.setDisable(false);
				getBtn3.setDisable(false);
				outputBtn3.setDisable(true);
			}
		});
		// Month selection.
		comboBoxMonth3 = new ComboBox<Integer>();
		comboBoxMonth3.setPromptText("Month");
		comboBoxMonth3.setPrefSize(125, 30);
		comboBoxMonth3.setDisable(true);
		// Get button.
		getBtn3 = new Button("Get");
		getBtn3.setPrefSize(50, 30);
		getBtn3.setDisable(true);
		outputBtn3 = new Button("Output");
		outputBtn3.setPrefSize(80, 30);
		outputBtn3.setDisable(true);
		line3.getChildren().addAll(comboBoxYear3, comboBoxMonth3, getBtn3, outputBtn3);
		leftVBox.getChildren().addAll(label3, line3, new Line(0, 30, GUI.WINDOW_WIDTH / 2, 30));
		// Line 4.
		HBox line4 = new HBox(15);
		line4.setPadding(new Insets(0, 0, 0, 10));
		Label label4 = new Label("DATE RANGE REPORT -- Total weight and the percentage for each farm:");
		label4.setPadding(new Insets(0, 0, 0, 10));
		// Start date selection.
		comboBoxRange1 = new ComboBox<Integer>();
		List<Integer> allDates = this.database.getAllDates();
		List<Integer> notAllDates = new ArrayList<Integer>();
		comboBoxRange1.getItems().addAll(allDates);
		comboBoxRange1.setPromptText("Start Date");
		comboBoxRange1.setPrefSize(125, 30);
		// End date selection.
		comboBoxRange2 = new ComboBox<Integer>();
		comboBoxRange2.setPromptText("End Date");
		comboBoxRange2.setPrefSize(125, 30);
		comboBoxRange2.setDisable(true);
		comboBoxRange1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				comboBoxRange2.getItems().clear();
				notAllDates.clear();
				for (int e : allDates) {
					if (e >= Integer.parseInt(newValue.toString())) {
						notAllDates.add(e);
					}
				}
				comboBoxRange2.getItems().addAll(notAllDates);
				comboBoxRange2.setDisable(false);
				getBtn4.setDisable(true);
				outputBtn4.setDisable(true);
			}
		});
		comboBoxRange2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				getBtn4.setDisable(false);
				outputBtn4.setDisable(true);
			}
		});
		// Get button.
		getBtn4 = new Button("Get");
		getBtn4.setPrefSize(50, 30);
		getBtn4.setDisable(true);
		outputBtn4 = new Button("Output");
		outputBtn4.setPrefSize(80, 30);
		outputBtn4.setDisable(true);
		line4.getChildren().addAll(comboBoxRange1, comboBoxRange2, getBtn4, outputBtn4);
		leftVBox.getChildren().addAll(label4, line4, new Line(0, 30, GUI.WINDOW_WIDTH / 2, 30));
		// Clear Button
		HBox clearBox = new HBox();
		clearBox.setPrefSize(WINDOW_WIDTH / 2, 50);
		clearBox.setPadding(new Insets(10, 0, 10, 0));
		clearBox.setAlignment(Pos.TOP_CENTER);
		Button clearBtn = new Button("Clear All");
		clearBtn.setPrefSize(BUTTONORMAL_WIDTH, BUTTONORMAL_HEIGHT);
		clearBox.getChildren().add(clearBtn);
		bigLeftVBox.getChildren().add(clearBox);
		// Buttom pane.
		buttomHBox.setPrefSize(WINDOW_WIDTH, 100);
		buttomHBox.setPadding(new Insets(0, 10, 10, 0));
		Button previousBtn = new Button("Previous");
		previousBtn.setPrefSize(BUTTONORMAL_WIDTH, BUTTONORMAL_HEIGHT);
		Button exitBtn = new Button("Exit");
		exitBtn.setPrefSize(BUTTONORMAL_WIDTH, BUTTONORMAL_HEIGHT);
		TEAMLABEL.setPadding(new Insets(0, 390, 0, 0));
		TEAMLABEL.setFont(new Font("Arial", 10));
		buttomHBox.getChildren().addAll(TEAMLABEL, previousBtn, exitBtn);
		buttomHBox.setAlignment(Pos.BOTTOM_RIGHT);
		// Display.
		mainPane.getChildren().addAll(ProgrressBar(2), upperPane,
				new Line(0, WINDOW_HEIGHT - 100, WINDOW_WIDTH, WINDOW_HEIGHT - 100), buttomHBox);
		primaryStage.setScene(new Scene(mainPane, WINDOW_WIDTH, WINDOW_HEIGHT));

		// Button Operations
		// getButton1
		getBtn1.setOnAction((ActionEvent e) -> {
			String farmID = comboBoxFarm1.getSelectionModel().getSelectedItem();
			int year = comboBoxYear1.getSelectionModel().getSelectedItem();
			this.output1Content = outputer.farmReport(farmID, year);
			this.outputBtn1.setDisable(false);
			this.textArea.setText(output1Content);
			output1Name = "Farm report-" + farmID + "-" + year + ".txt";
		});
		// getButton2
		getBtn2.setOnAction((ActionEvent e) -> {
			int year = comboBoxYear2.getSelectionModel().getSelectedItem();
			int month = comboBoxMonth2.getSelectionModel().getSelectedItem() % 100;
			this.output2String = outputer.monthlyFarmReport(year, month);
			this.textArea.setText(output2String);
			this.output2Name = "One month farm report-" + year + "-" + month + ".txt";
			this.outputBtn2.setDisable(false);
		});
		// getButton3
		getBtn3.setOnAction((ActionEvent e) -> {
			if (comboBoxMonth3.getValue() != null) {
				int year = comboBoxYear3.getSelectionModel().getSelectedItem();
				int month = comboBoxMonth3.getSelectionModel().getSelectedItem() % 100;
				this.output3String = outputer.monthlyReportYM(year, month);
				this.textArea.setText(this.output3String);
				this.output3Name = "Monthly report-" + year + "-" + month + ".txt";
				this.outputBtn3.setDisable(false);
			} else {
				int year = comboBoxYear3.getSelectionModel().getSelectedItem();
				this.output3String = outputer.annualReportY(year);
				this.textArea.setText(this.output3String);
				this.output3Name = "Annual report-" + year + ".txt";
				this.outputBtn3.setDisable(false);
			}
		});
		// getButton4
		getBtn4.setOnAction((ActionEvent e) -> {
			int startDate = comboBoxRange1.getSelectionModel().getSelectedItem();
			int endDate = comboBoxRange2.getSelectionModel().getSelectedItem();
			this.output4String = outputer.dateRangeReport(startDate, endDate);
			this.textArea.setText(this.output4String);
			this.output4Name = "Date range report-" + startDate + "-" + endDate + ".txt";
			this.outputBtn4.setDisable(false);
		});
		// Output1 Button
		outputBtn1.setOnAction((ActionEvent e) -> {
			this.outputFile(primaryStage, this.output1Name, output1Content);
		});
		// Output2 Button
		outputBtn2.setOnAction((ActionEvent e) -> {
			this.outputFile(primaryStage, this.output2Name, output2String);
		});
		// Output3 Button
		outputBtn3.setOnAction((ActionEvent e) -> {
			this.outputFile(primaryStage, this.output3Name, output3String);
		});
		// Output4 Button
		outputBtn4.setOnAction((ActionEvent e) -> {
			this.outputFile(primaryStage, this.output4Name, output4String);
		});
		// Clear Button
		clearBtn.setOnAction((ActionEvent e) -> {
			this.DataDisplayScene(primaryStage);
		});
		// Previous Button
		previousBtn.setOnAction((ActionEvent e) -> {
			this.FileManagerScene(primaryStage);
		});
		// Exit Button
		exitBtn.setOnAction((ActionEvent e) -> {
			primaryStage.close();
			System.exit(0);
		});
		getBtn1.requestFocus();
	}

	/*
	 * Choose and create a file and output content into it.
	 */
	private void outputFile(Stage primaryStage, String name, String content) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File file = directoryChooser.showDialog(primaryStage);
		if (file != null && !file.getPath().equals("")) {
			// Check system name and set path.
			String osName = System.getProperty("os.name");
			String path = "";
			if (osName.startsWith("Windows")) {
				path = file.getPath() + "\\" + name;
			} else {
				path = file.getPath() + "/" + name;
			}
			try {
				PrintWriter out = new PrintWriter(new FileWriter(new File(path)));
				out.print(content);
				out.close();
				popUpWindow("SUCCESS", "Successfully output data into\n" + path);
			} catch (IOException e) {
				errorWindow("Error: cannot write data into " + path);
			}
		}
	}

}
