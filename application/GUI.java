/**
 *
 */
package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
import javafx.scene.control.ScrollPane;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
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
	private static final int ADDWIDOW_WIDTH = 900;
	private static final int ERRORWIDOW_WIDTH = 250;
	// Heights.
	private static final int WINDOW_HEIGHT = 500;
	private static final int PROGRESSBAR_Height = 30;
	private static final int TOPBOX_HEIGHT = 40;
	private static final int CENTERBOX_HEIGHT = 360;
	private static final int BUTTOMBOX_HEIGHT = 70;
	private static final int ADDWIDOW_HEIGHT = 35;
	private static final int ERRORWIDOW_HEIGHT = 200;
	// Private members.
	private static final String APP_TITLE = "Milk Weight Manager-Ateam 37";
	private static final String ADDER_TITLE = "Multiple Records Adder";
	private static final String ERROR_TITLE = "ERROR";
	private static final Label TEAMLABEL = new Label("Authors: Yifei Miao, Chenxi Gao, Chen Wang, Tianyi Zhao");
	private Database database;
	private List<Database> dataBaseHistory;
	private ObservableList<OneRecord> tableList;
	private List<List<OneRecord>> tableListHistory;
	private FileManager fileManager;
	private FileOutputer fileOutputer;
	private Label recordsCount, farmCount, weightCount, daysCount, earliestDate, latestDate;
	private int historyIndicator;
	private Button fileOpenBtn, fileClearBtn, deleteButton, deleteAllButton, unDoButton, reDoButton, nextBtn,
			getButton1, getButton2, getButton3, getButton4, output1, output2, output3, output4;
	private TableView<OneRecord> tableView;
	private TextField searchBoxTextField;
	private TextArea textArea;
	private ComboBox<Integer> comboBoxYear1, comboBoxMonth2, comboBoxMonth3, comboBoxRange1, comboBoxRange2;

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
		this.fileOutputer = new FileOutputer(this.database);
		this.FileManagerScene(primaryStage);
		primaryStage.setTitle(APP_TITLE);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

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
		// Button to open fileChooser.
		Button fileChooserBtn = new Button("Choose");
		fileChooserBtn.setMinWidth(SEARCHBARBUTTON_WIDTH);
		fileClearBtn = new Button("Clear");
		fileClearBtn.setMinWidth(SEARCHBARBUTTON_WIDTH);
		// Button to open fileChooser.
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
		TableColumn<OneRecord, String> nameCol = new TableColumn<>("Farm ID");
		nameCol.setMinWidth(200);
		nameCol.setCellValueFactory(new PropertyValueFactory<>("farmID"));

		TableColumn<OneRecord, Integer> dateCol = new TableColumn<>("Date");
		dateCol.setMinWidth(200);
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

		TableColumn<OneRecord, Integer> weightCol = new TableColumn<>("Weight");
		weightCol.setMinWidth(200);
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
		VBox rightTopSpace = new VBox();
		rightTopSpace.setMinHeight(100);
		rightTopSpace.setMinWidth(100);
		rightTopSpace.setAlignment(Pos.TOP_CENTER);
		Image image = new Image("file:images/Rin.jpg");
		ImageView imageView = new ImageView(image);
		rightTopSpace.getChildren().addAll(imageView);
		imageView.setFitWidth(100);
		imageView.setFitHeight(100);
		Button addBtn = new Button("Add");
		addBtn.setMinHeight(30);
		addBtn.setMinWidth(100);
		deleteButton = new Button("Delete");
		deleteButton.setMinHeight(30);
		deleteButton.setMinWidth(100);
		deleteAllButton = new Button("Delete All");
		deleteAllButton.setMinHeight(30);
		deleteAllButton.setMinWidth(100);
		unDoButton = new Button("Undo");
		unDoButton.setMinHeight(30);
		unDoButton.setMinWidth(100);
		reDoButton = new Button("Redo");
		reDoButton.setMinHeight(30);
		reDoButton.setMinWidth(100);
		rightVBox.getChildren().addAll(rightTopSpace, addBtn, deleteButton, deleteAllButton, unDoButton, reDoButton);
		// Add rightVBox to the pane.
		pane.setRight(rightVBox);

		// Bottom Panel.
		HBox buttomHBox = new HBox(15);
		buttomHBox.setMinHeight(BUTTOMBOX_HEIGHT);
		buttomHBox.setPadding(new Insets(0, 10, 10, 0));
		Button exitBtn = new Button("Exit");
		exitBtn.setMinHeight(30);
		exitBtn.setMinWidth(100);
		nextBtn = new Button("Next");
		nextBtn.setMinHeight(30);
		nextBtn.setMinWidth(100);
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
				if (errorFiles.size() == 0) {
					this.historyManager(true, false, false);
				}
				if (errorFiles.size() > 0) {
					String errorText = "";
					for (int i = 0; i < errorFiles.size(); i++) {
						errorText += "\n" + (i + 1) + ". " + errorFiles.get(i).getPath().toString();
					}
					this.errorWindow("Error Reading from " + errorFiles.size() + " file(s): " + errorText);
				}
				files.clear();
			}
			enableButtons();
			this.deleteButton.setDisable(true);
		});
		// Add button.
		addBtn.setOnAction((ActionEvent e) -> {
			this.addBtnWindow(primaryStage);
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

	private void addBtnWindow(Stage primaryStage) {
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
			} else {
				this.errorWindow("Incorrect format of data, please check.");
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
		// Scroll Pane
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(ERRORWIDOW_WIDTH, ERRORWIDOW_HEIGHT - 45);
		// Top space.
		VBox top = new VBox();
		top.setMinHeight(ERRORWIDOW_HEIGHT / 6);
		// Message label.
		Label messageLabel = new Label(message);
		messageLabel.setAlignment(Pos.CENTER);
		// OK button.
		Button okBtn = new Button("OK");
		okBtn.setAlignment(Pos.CENTER);
		okBtn.setMinWidth(75);
		Stage errorWindowStage = new Stage();
		scrollPane.setContent(messageLabel);
		vBox.getChildren().addAll(scrollPane, okBtn);
		// Add to scene and stage.
		Scene mainScene = new Scene(vBox, ERRORWIDOW_WIDTH, ERRORWIDOW_HEIGHT);
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
		label1.setMinWidth(100);
		label1.setMinHeight(PROGRESSBAR_Height);
		label1.setAlignment(Pos.CENTER);
		// Second step.
		Label label2 = new Label();
		label2.setText("2.Display");
		label2.setMinWidth(100);
		label2.setMinHeight(PROGRESSBAR_Height);
		label2.setAlignment(Pos.CENTER);
		// Third step.
		Label label3 = new Label();
		label3.setText("3.Output");
		label3.setMinWidth(100);
		label3.setMinHeight(PROGRESSBAR_Height);
		label3.setAlignment(Pos.CENTER);
		// Step selection.
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
		// Add to panel.
		result.getChildren().addAll(label1, label2, label3);
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
		} else if (undo) {
			this.historyIndicator--;
			matchVersion();
			enableButtons();
		} else if (redo) {
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

	private void DataDisplayScene(Stage primaryStage) {
		// Layer.
		VBox mainPane = new VBox();
		mainPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		HBox upperPane = new HBox();
		upperPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT - 100);
		VBox leftVBox = new VBox(15);
		leftVBox.setPadding(new Insets(10, 0, 0, 0));
		leftVBox.setPrefSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT - 100);
		textArea = new TextArea();
		textArea.setPromptText("Ready to display...");
		textArea.setMinHeight(WINDOW_HEIGHT - 100);
		textArea.setEditable(false);
		upperPane.getChildren().addAll(leftVBox,
				new Line(GUI.WINDOW_WIDTH / 2 + 3, 0, GUI.WINDOW_WIDTH / 2 + 3, WINDOW_HEIGHT - 100), textArea);
		HBox buttomHBox = new HBox(15);
		buttomHBox.setPrefSize(WINDOW_WIDTH / 2, 100);
		// Left operation pane.
		// Line 1.
		HBox line1 = new HBox(15);
		line1.setPadding(new Insets(0, 0, 0, 10));
		Label label1 = new Label("FARM REPORT -- By month for specified farm and year:");
		label1.setPadding(new Insets(0, 0, 0, 10));
		ComboBox<String> comboBoxFarm1 = new ComboBox<String>();
		comboBoxFarm1.getItems().addAll(this.database.getFarmIDList());
		comboBoxFarm1.setPromptText("Farm ID");
		comboBoxFarm1.setPrefSize(125, 30);
		comboBoxFarm1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				comboBoxYear1.setDisable(false);
			}
		});
		comboBoxYear1 = new ComboBox<Integer>();
		comboBoxYear1.getItems().addAll(this.database.getYearList());
		comboBoxYear1.setPromptText("Year");
		comboBoxYear1.setPrefSize(125, 30);
		comboBoxYear1.setDisable(true);
		comboBoxYear1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				getButton1.setDisable(false);
			}
		});
		getButton1 = new Button("Get");
		getButton1.setPrefSize(50, 30);
		getButton1.setDisable(true);
		output1 = new Button("Output");
		output1.setPrefSize(80, 30);
		output1.setDisable(true);
		line1.getChildren().addAll(comboBoxFarm1, comboBoxYear1, getButton1, output1);
		leftVBox.getChildren().addAll(label1, line1, new Line(0, 30, GUI.WINDOW_WIDTH / 2, 30));
		// Line 2.
		HBox line2 = new HBox(15);
		line2.setPadding(new Insets(0, 0, 0, 10));
		Label label2 = new Label("MONTHLY REPORT -- By farms for specified month and year:");
		label2.setPadding(new Insets(0, 0, 0, 10));
		ComboBox<Integer> comboBoxYear2 = new ComboBox<Integer>();
		comboBoxYear2.getItems().addAll(this.database.getYearList());
		comboBoxYear2.setPromptText("Year");
		comboBoxYear2.setPrefSize(125, 30);
		comboBoxYear2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				comboBoxMonth2.setDisable(false);
			}
		});
		comboBoxMonth2 = new ComboBox<Integer>();
		comboBoxMonth2.getItems().addAll(this.database.getMonthList());
		comboBoxMonth2.setPromptText("Month");
		comboBoxMonth2.setPrefSize(125, 30);
		comboBoxMonth2.setDisable(true);
		comboBoxMonth2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				getButton2.setDisable(false);
			}
		});
		getButton2 = new Button("Get");
		getButton2.setPrefSize(50, 30);
		getButton2.setDisable(true);
		output2 = new Button("Output");
		output2.setPrefSize(80, 30);
		output2.setDisable(true);
		line2.getChildren().addAll(comboBoxYear2, comboBoxMonth2, getButton2, output2);
		leftVBox.getChildren().addAll(label2, line2, new Line(0, 30, GUI.WINDOW_WIDTH / 2, 30));
		// Line 3.
		HBox line3 = new HBox(15);
		line3.setPadding(new Insets(0, 0, 0, 10));
		Label label3 = new Label("NET SALES REPORT -- Each farm's share of net sales: ");
		label3.setPadding(new Insets(0, 0, 0, 10));
		ComboBox<Integer> comboBoxYear3 = new ComboBox<Integer>();
		comboBoxYear3.getItems().addAll(this.database.getYearList());
		comboBoxYear3.setPromptText("Year");
		comboBoxYear3.setPrefSize(125, 30);
		comboBoxYear3.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				comboBoxMonth3.setDisable(false);
				getButton3.setDisable(false);
			}
		});
		comboBoxMonth3 = new ComboBox<Integer>();
		comboBoxMonth3.getItems().addAll(this.database.getMonthList());
		comboBoxMonth3.setPromptText("Month");
		comboBoxMonth3.setPrefSize(125, 30);
		comboBoxMonth3.setDisable(true);
		getButton3 = new Button("Get");
		getButton3.setPrefSize(50, 30);
		getButton3.setDisable(true);
		output3 = new Button("Output");
		output3.setPrefSize(80, 30);
		output3.setDisable(true);
		line3.getChildren().addAll(comboBoxYear3, comboBoxMonth3, getButton3, output3);
		leftVBox.getChildren().addAll(label3, line3, new Line(0, 30, GUI.WINDOW_WIDTH / 2, 30));
		// Line 4.
		HBox line4 = new HBox(15);
		line4.setPadding(new Insets(0, 0, 0, 10));
		Label label4 = new Label("DATE RANGE REPORT -- Total weight and the percentage for each farm:");
		label4.setPadding(new Insets(0, 0, 0, 10));
		comboBoxRange1 = new ComboBox<Integer>();
		List<Integer> allDates = this.database.getAllDates();
		List<Integer> notAllDates = new ArrayList<Integer>();
		comboBoxRange1.getItems().addAll(allDates);
		comboBoxRange1.setPromptText("Start Date");
		comboBoxRange1.setPrefSize(125, 30);
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
			}
		});
		comboBoxRange2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				getButton4.setDisable(false);
			}
		});
		getButton4 = new Button("Get");
		getButton4.setPrefSize(50, 30);
		getButton4.setDisable(true);
		output4 = new Button("Output");
		output4.setPrefSize(80, 30);
		output4.setDisable(true);
		line4.getChildren().addAll(comboBoxRange1, comboBoxRange2, getButton4, output4);
		leftVBox.getChildren().addAll(label4, line4, new Line(0, 30, GUI.WINDOW_WIDTH / 2, 30));
		// Clear Button
		HBox clearBox = new HBox();
		clearBox.setPrefSize(WINDOW_WIDTH / 2, 30);
		clearBox.setPadding(new Insets(0, 10, 0, 0));
		clearBox.setAlignment(Pos.TOP_RIGHT);
		Button clearBtn = new Button("Clear All");
		clearBtn.setPrefSize(100, 20);
		clearBox.getChildren().add(clearBtn);
		leftVBox.getChildren().add(clearBox);
		// Buttom pane.
		buttomHBox.setPrefSize(WINDOW_WIDTH, 100);
		buttomHBox.setPadding(new Insets(0, 10, 10, 0));
		Button previousBtn = new Button("Previous");
		previousBtn.setMinHeight(30);
		previousBtn.setMinWidth(100);
		Button exitBtn = new Button("Exit");
		exitBtn.setMinHeight(30);
		exitBtn.setMinWidth(100);
		TEAMLABEL.setPadding(new Insets(0, 390, 0, 0));
		TEAMLABEL.setFont(new Font("Arial", 10));
		buttomHBox.getChildren().addAll(TEAMLABEL, previousBtn, exitBtn);
		buttomHBox.setAlignment(Pos.BOTTOM_RIGHT);
		// Display.
		mainPane.getChildren().addAll(ProgrressBar(2), upperPane,
				new Line(0, WINDOW_HEIGHT - 100, WINDOW_WIDTH, WINDOW_HEIGHT - 100), buttomHBox);
		primaryStage.setScene(new Scene(mainPane, WINDOW_WIDTH, WINDOW_HEIGHT));

		// Button Operations
//		  comboBox.getSelectionModel().selectedItemProperty()
//          .addListener(new ChangeListener<Object>() {
//
//              @Override
//              public void changed(ObservableValue observable,
//                      Object oldValue, Object newValue) {
//                  Image tmpimage = new Image(
//                          "file:images/" + newValue.toString());
//                  imageView.setImage(tmpimage);
//                  imageView.setFitWidth(200);
//                  imageView.setFitHeight(200);
//              }
//          });
		// getButton1
		getButton1.setOnAction((ActionEvent e) -> {
			if ((!comboBoxFarm1.getValue().equals("")) && comboBoxYear1.getValue() != null) {
				String farmID = comboBoxFarm1.getSelectionModel().getSelectedItem();
				int year = comboBoxYear1.getSelectionModel().getSelectedItem();
				List<List<OneRecord>> list = this.database.getAllRecordsInAYearOfAFarm(farmID, year);
				boolean empty = true;
				for (List<OneRecord> x : list) {
					if (x.size() > 0) {
						empty = false;
						break;
					}
				}
				if (!empty) {
					this.textArea.clear();
					this.textArea.setText("Min, max, average weight by month for " + farmID + " in " + year
							+ ":\nMonth, min(percent), max(percent), average");
					for (int i = 0; i < 12; i++) {
						List<OneRecord> records = list.get(i);
						int min = -1;
						int max = -1;
						double average = 0;
						for (OneRecord r : records) {
							if (min == -1 || r.getWeight() < min) {
								min = r.getWeight();
							}
							if (max == -1 || r.getWeight() > max) {
								max = r.getWeight();
							}
							average += r.getWeight();
						}
						if (min == -1) {
							max = min = 0;
						}
						double minPercent = 100;
						double maxPercent = 100;
						if (average != 0) {
							minPercent = 100 * min / average;
							maxPercent = 100 * max / average;
							average = average / records.size();
						}
						this.textArea
								.setText(this.textArea.getText() + String.format("\n%3s, %d(%.3f), %d(%.3f), %.3f\n",
										(i + 1), min, minPercent, max, maxPercent, average));
					}
				} else {
					errorWindow("Error: no record exists.");
				}
			} else {
				errorWindow("Error: invalid input.");
			}
		});
		// getButton2
		getButton2.setOnAction((ActionEvent e) -> {
			if (comboBoxYear2.getValue() != null && comboBoxMonth2.getValue() != null) {
				int year = comboBoxYear2.getSelectionModel().getSelectedItem();
				int month = comboBoxMonth2.getSelectionModel().getSelectedItem() % 100;
				List<OneRecord> list = this.database.getAllRecordsInAYear(year).get(month - 1);
				if (list.size() >= 0) {
					this.textArea.clear();
					this.textArea.setText("Min, max, average weight for all farms in " + monthTrans(month) + ", " + year
							+ ":\nFarm ID, min(date)(percent), max(date)(percent), average");
					this.sortUsingIDUp(list);
					while (list.size() > 0) {
						OneRecord record = list.get(0);
						list.remove(0);
						int min = record.getWeight();
						int minDate = record.getDate();
						int max = record.getWeight();
						int maxDate = record.getDate();
						double average = record.getWeight();
						List<OneRecord> tmp = new ArrayList<OneRecord>();
						for (OneRecord r : list) {
							if (r.getFarmID().equals(record.getFarmID())) {
								if (r.getWeight() < min) {
									min = r.getWeight();
									minDate = r.getDate();
								}
								if (r.getWeight() > max) {
									max = r.getWeight();
									maxDate = r.getDate();
								}
								average += r.getWeight();
								tmp.add(r);
							}
						}
						double minPercent = 100;
						double maxPercent = 100;
						if (average != 0) {
							minPercent = 100 * min / average;
							maxPercent = 100 * max / average;
						}
						average = average / (tmp.size() + 1);
						for (OneRecord r : tmp) {
							list.remove(r);
						}
						this.textArea.setText(this.textArea.getText()
								+ String.format("\n%10s, %6d(%d)(%.3f), %6d(%d)(%.3f), %9.3f\n", record.getFarmID(),
										min, minDate, minPercent, max, maxDate, maxPercent, average));
					}
				} else {
					errorWindow("Error: no record exists.");
				}
			} else {
				errorWindow("Error: invalid input.");
			}
		});
		// getButton3
		getButton3.setOnAction((ActionEvent e) -> {
			if (comboBoxYear3.getValue() != null && comboBoxMonth3.getValue() != null) {
				int year = comboBoxYear3.getSelectionModel().getSelectedItem();
				int month = comboBoxMonth3.getSelectionModel().getSelectedItem() % 100;
				List<OneRecord> list = this.database.getAllRecordsInAYear(year).get(month - 1);
				if (list.size() >= 0) {
					this.textArea.clear();
					this.textArea.setText("Each farm's share (% of total weight of the month) of net sales in "
							+ monthTrans(month) + ", " + year + ":\nFarm ID, total weight, share(%)");
					this.sortUsingIDUp(list);
					long totalWeight = 0;
					for (OneRecord r : list) {
						totalWeight += r.getWeight();
					}
					while (list.size() > 0) {
						OneRecord record = list.get(0);
						list.remove(0);
						long farmTotal = record.getWeight();
						List<OneRecord> tmp = new ArrayList<OneRecord>();
						for (OneRecord r : list) {
							if (r.getFarmID().equals(record.getFarmID())) {
								farmTotal += r.getWeight();
								tmp.add(r);
							}
						}
						for (OneRecord r : tmp) {
							list.remove(r);
						}
						double share = 100;
						if (totalWeight != 0) {
							share = farmTotal * 100.0 / totalWeight;
						}
						this.textArea.setText(this.textArea.getText()
								+ String.format("\n%10s, %8d, %.3f\n", record.getFarmID(), farmTotal, share));
					}
				} else {
					errorWindow("Error: no record exists.");
				}
			} else if (comboBoxYear3.getValue() != null) {
				int year = comboBoxYear3.getSelectionModel().getSelectedItem();
				List<List<OneRecord>> inlist = this.database.getAllRecordsInAYear(year);
				List<OneRecord> list = new ArrayList<OneRecord>();
				for (List<OneRecord> li : inlist) {
					for (OneRecord r : li) {
						list.add(r);
					}
				}
				sortUsingIDUp(list);
				if (list.size() >= 0) {
					this.textArea.clear();
					this.textArea.setText("Each farm's share (% of total weight of the year) of net sales in " + year
							+ ":\nFarm ID, total weight, share(%)");
					long totalWeight = 0;
					for (OneRecord r : list) {
						totalWeight += r.getWeight();
					}
					while (list.size() > 0) {
						OneRecord record = list.get(0);
						list.remove(0);
						long farmTotal = record.getWeight();
						List<OneRecord> tmp = new ArrayList<OneRecord>();
						for (OneRecord r : list) {
							if (r.getFarmID().equals(record.getFarmID())) {
								farmTotal += r.getWeight();
								tmp.add(r);
							}
						}
						for (OneRecord r : tmp) {
							list.remove(r);
						}
						double share = 100;
						if (totalWeight != 0) {
							share = farmTotal * 100.0 / totalWeight;
						}
						this.textArea.setText(this.textArea.getText()
								+ String.format("\n%10s, %8d, %.3f\n", record.getFarmID(), farmTotal, share));
					}
				} else {
					errorWindow("Error: no record exists.");
				}
			} else {
				errorWindow("Error: invalid input.");
			}
		});
		// getButton4
		getButton4.setOnAction((ActionEvent e) -> {
			int startDate = comboBoxRange1.getSelectionModel().getSelectedItem();
			int endDate = comboBoxRange2.getSelectionModel().getSelectedItem();
			List<OneRecord> list = this.database.getAllRecordsInDateRange(startDate, endDate);
			System.out.println(list.size());
			sortUsingIDUp(list);
			if (list.size() >= 0) {
				this.textArea.clear();
				this.textArea
						.setText("Total milk weight per farm and the percentage of the total for each farm between "
								+ startDate + " and " + endDate + ":\nFarm ID, total weight, share(%)");
				long totalWeight = 0;
				for (OneRecord r : list) {
					totalWeight += r.getWeight();
				}
				while (list.size() > 0) {
					OneRecord record = list.get(0);
					list.remove(0);
					long farmTotal = record.getWeight();
					List<OneRecord> tmp = new ArrayList<OneRecord>();
					for (OneRecord r : list) {
						if (r.getFarmID().equals(record.getFarmID())) {
							farmTotal += r.getWeight();
							tmp.add(r);
						}
					}
					for (OneRecord r : tmp) {
						list.remove(r);
					}
					double share = 100;
					if (totalWeight != 0) {
						share = farmTotal * 100.0 / totalWeight;
					}
					this.textArea.setText(this.textArea.getText()
							+ String.format("\n%10s, %8d, %.3f\n", record.getFarmID(), farmTotal, share));
				}
			} else {
				errorWindow("Error: no record exists.");
			}
		});
		// Output1 Button

		// Output2 Button
		// Output3 Button
		// Output4 Button
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
		getButton1.requestFocus();
	}

	private String monthTrans(int month) {
		switch (month) {
		case 1:
			return "Januarary";
		case 2:
			return "Feburary";
		case 3:
			return "March";
		case 4:
			return "April";
		case 5:
			return "May";
		case 6:
			return "June";
		case 7:
			return "July";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "October";
		case 11:
			return "November";
		case 12:
			return "December";

		default:
			return "Error";
		}
	}

	private void sortUsingIDUp(List<OneRecord> list) {
		List<OneRecord> tmp = new ArrayList<OneRecord>();
		List<String> nameList = new ArrayList<String>();
		for (OneRecord r : list) {
			if (!nameList.contains(r.getFarmID())) {
				nameList.add(r.getFarmID());
			}
		}
		Collections.sort(nameList);
		for (String e : nameList) {
			for (OneRecord r : list) {
				if (r.getFarmID().equals(e)) {
					tmp.add(r);
				}
			}
		}
		list.clear();
		list.addAll(tmp);
	}

	private void OutputScene(Stage primaryStage) {
	}

}
