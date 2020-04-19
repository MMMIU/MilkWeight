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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
	private static final int ERRORWIDOW_HEIGHT = 150;
	// Private members.
	private static final String APP_TITLE = "Milk Weight Manager-Ateam 37";
	private static final String ADDER_TITLE = "Multiple Records Adder";
	private static final String ERROR_TITLE = "ERROR";
	private Database database;
	private List<Database> dataBaseHistory;
	private ObservableList<OneRecord> tableList;
	private List<List<OneRecord>> tableListHistory;
	private FileManager fileManager;
	private FileOutputer fileOutputer;
	private Label recordsCount, farmCount, weightCount, daysCount, earliestDate, latestDate;
	private int historyIndicator;
	private Button deleteButton, deleteAllButton, unDoButton, reDoButton;

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
		// Fist pane.
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
		TextField searchBoxTextField = new TextField();
		searchBoxTextField.setPromptText("Use \"Choose\" button to select files from the system...");
		searchBoxTextField.setMinWidth(CENTERBOX_WIDTH - 3 * SEARCHBARBUTTON_WIDTH);
		searchBoxTextField.setEditable(false);
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
		searchBarHBox.getChildren().addAll(searchBoxTextField, fileChooserBtn, fileClearBtn, fileOpenBtn);
		// TableView.
		TableView<OneRecord> tableView = new TableView<>();
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
		deleteButton.setDisable(true);
		deleteAllButton = new Button("Delete All");
		deleteAllButton.setMinHeight(30);
		deleteAllButton.setMinWidth(100);
		deleteAllButton.setDisable(true);
		unDoButton = new Button("Undo");
		unDoButton.setMinHeight(30);
		unDoButton.setMinWidth(100);
		unDoButton.setDisable(true);
		reDoButton = new Button("Redo");
		reDoButton.setMinHeight(30);
		reDoButton.setMinWidth(100);
		reDoButton.setDisable(true);
		rightVBox.getChildren().addAll(rightTopSpace, addBtn, deleteButton, deleteAllButton, unDoButton, reDoButton);
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

		// DropShadow shadow = new DropShadow();
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
		// FileChooser button.
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
		});
		// Clear button.
		fileClearBtn.setOnAction(event -> {
			files.clear();
			searchBoxTextField.clear();
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

	}

	private void refreshTable() {
		this.tableList.clear();
		for (OneRecord e : this.database.getAllRecords()) {
			this.tableList.add(new OneRecord(e.getFarmID(), e.getDate(), e.getWeight()));
		}
	}

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

	private void DataDisplayScene(Stage primaryStage) {
		VBox vbox = new VBox();
		// Display.
		primaryStage.setScene(new Scene(vbox, WINDOW_WIDTH, WINDOW_HEIGHT));
	}

	private void OutputScene(Stage primaryStage) {
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
			farmIDText.clear();
			dateText.clear();
			weightText.clear();
			if (this.fileManager.checkData(data)) {
				OneRecord record = this.fileManager.generateRecordUsingData(data);
				this.database.add(record);
				this.tableList.add(0, record);
				;
			} else {
				this.errorWindow("Incorrect format of data, please check.");
			}
			List<Integer> allDates = this.database.getAllDates();
			recordsCount.setText("Total Records:\n" + this.database.size());
			farmCount.setText("Total Farms:\n" + this.database.getFarmIDList().size());
			weightCount.setText("Total Weight:\n" + this.database.totalWeight());
			daysCount.setText("Total Days:\n" + allDates.size());
			earliestDate.setText("Earliset Date:\n" + allDates.get(0));
			latestDate.setText("Lateset Date:\n" + allDates.get(allDates.size() - 1));
			this.historyManager(true, false, false);
		});
		// Cancel button operation.
		cancelBtn.setOnAction((ActionEvent e) -> {
			stage.close();
		});
	}

	private void popUpWindow(String title, String message) {
		VBox vBox = new VBox(20);
		vBox.setAlignment(Pos.TOP_CENTER);
		// Top space.
		VBox top = new VBox();
		top.setMinHeight(ERRORWIDOW_HEIGHT / 6);
		// Message label.
		Label center = new Label(message);
		center.setMinHeight(ERRORWIDOW_HEIGHT / 3);
		center.setMinWidth(ERRORWIDOW_WIDTH);
		center.setAlignment(Pos.CENTER);
		// OK button.
		Button okBtn = new Button("OK");
		okBtn.setAlignment(Pos.CENTER);
		okBtn.setMinWidth(75);
		vBox.getChildren().addAll(top, center, okBtn);
		// Add to scene and stage.
		Stage errorWindowStage = new Stage();
		Scene mainScene = new Scene(vBox, ERRORWIDOW_WIDTH, ERRORWIDOW_HEIGHT);
		errorWindowStage.initModality(Modality.APPLICATION_MODAL);
		errorWindowStage.setScene(mainScene);
		errorWindowStage.setTitle(title);
		errorWindowStage.show();
		// Return button operation.
		okBtn.setOnAction((ActionEvent e) -> {
			errorWindowStage.close();
		});
	}

	private void errorWindow(String message) {
		popUpWindow(ERROR_TITLE, message);

	}

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

	private void enableButtons() {
		if (this.database.size() == 0) {
			this.deleteButton.setDisable(true);
			this.deleteAllButton.setDisable(true);
		} else {
			this.deleteButton.setDisable(false);
			this.deleteAllButton.setDisable(false);
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
	}

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
}
