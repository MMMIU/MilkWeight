/*
 * FileManager.java created by Yifei Miao in Milk Weight project.
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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author Yifei Miao
 *
 */
public class FileManager {

	// Private members.
	private Database databse;
	private List<Integer> positions;
	private int lineCounter;

	// Constructor.
	public FileManager(Database database) {
		this.databse = database;
		this.positions = new ArrayList<Integer>();
		this.lineCounter = 1;
	}

	/*
	 * Add records in multiple files to the database. Return list of files that
	 * cannot be read or some data in them is not valid. Data in error files will
	 * not be added to the database.
	 */
	public List<File> addFiles(List<File> files) {
		// List of files with error.
		List<File> errorFiles = new ArrayList<>();
		this.positions.clear();
		;
		// Iterator.
		for (File file : files) {
			try {
				this.lineCounter = 1;
				// Scanner.
				Scanner scanner = new Scanner(file);
				boolean valid = true;
				// Ignore first title line.
				if (scanner.hasNextLine()) {
					scanner.nextLine();
					lineCounter++;
				}
				// Temporary list to store records in a file.
				List<OneRecord> records = new ArrayList<>();
				while (scanner.hasNextLine()) {
					// Get a line.
					String line = scanner.nextLine().trim();
					if (line.equals("")) {
						continue;
					}
					// Get data parts in the line.
					String[] data = line.split(",");
					// Check data.
					if (!this.checkData(data)) {
						errorFiles.add(file);
						valid = false;
						positions.add(lineCounter);
						break;
					} else {
						OneRecord record = this.generateRecordUsingData(data);
						// Add the record to the temporary list
						records.add(record);
					}
					lineCounter++;
				}
				// If no error in the file, add records to database.
				if (valid) {
					for (OneRecord record : records) {
						this.databse.add(record);
					}
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				errorFiles.add(file);
				positions.add(0);
			}
		}
		return errorFiles;
	}

	/*
	 * Combine three parts in data set together and return a record.
	 */
	public OneRecord generateRecordUsingData(String[] data) {
		// Create a record.
		OneRecord record = new OneRecord(data[1].trim().toUpperCase(), 0, Integer.parseInt(data[2].trim()));
		// Add date in suitable format to the record.
		String date = data[0];
		if (this.isPositiveInteger(date)) {
			record.setDate(Integer.parseInt(date));
		} else {
			String[] dateParts = data[0].trim().split("-");
			record.setDate(Integer.parseInt(dateParts[0]) * 10000 + Integer.parseInt(dateParts[1]) * 100
					+ Integer.parseInt(dateParts[2]));
		}
		return record;
	}

	/*
	 * Add a record to the database.
	 */
	public boolean addARecord(String farmID, int date, int weight) {
		OneRecord record = new OneRecord(farmID.toUpperCase(), date, weight);
		return this.addARecord(record);
	}

	/*
	 * Add a record to the database.
	 */
	public boolean addARecord(OneRecord record) {
		if (!this.checkRecordValid(record)) {
			return false;
		}
		this.databse.add(record);
		return true;
	}

	/*
	 * Add a record to the database.
	 */
	public boolean deleteARecord(String farmID, int date) {
		OneRecord record = new OneRecord(farmID, date, 0);
		return this.deleteARecord(record);
	}

	/*
	 * Delete a record in the database.
	 */
	public boolean deleteARecord(OneRecord record) {
		if (!this.checkRecordValid(record)) {
			return false;
		}
		this.databse.remove(record);
		return true;
	}

	/*
	 * Check data extracted from a line is valid.
	 */
	public boolean checkData(String[] data) {
		OneRecord record = new OneRecord("x", 0, 0);

		// Check 3 parts exist.
		if (data.length != 3) {
			return false;
		}
		// Check date.
		String date = data[0];
		// Check date is in Integer format.
		if (this.isPositiveInteger(date)) {
			record.setDate(Integer.parseInt(date));
		} else {
			// Check 3 parts exist.
			String[] dateParts = data[0].trim().split("-");
			if (dateParts.length != 3) {
				return false;
			}
			// Check 3 parts are all digit and add date to the temporary record.
			if (this.isPositiveInteger(dateParts[0]) && this.isPositiveInteger(dateParts[1])
					&& this.isPositiveInteger(dateParts[2])) {
				record.setDate(Integer.parseInt(dateParts[0]) * 10000 + Integer.parseInt(dateParts[1]) * 100
						+ Integer.parseInt(dateParts[2]));
			} else {
				return false;
			}
		}
		// Set ID.
		record.setFarmID(data[1].toUpperCase());
		// Set Weight.
		if (this.isPositiveInteger(data[2])) {
			record.setWeight(Integer.parseInt(data[2]));
		} else {
			return false;
		}
		return this.checkRecordValid(record);
	}

	/*
	 * Check content in record is valid.
	 */
	public boolean checkRecordValid(OneRecord record) {
		// Check ID.
		if (record.getFarmID() == null || record.getFarmID().equals("")) {
			return false;
		}
		// Check Date.
		if (record.getDate() < 0) {
			return false;
		}
		// Get year, month, and day.
		int year = record.getDate() / 10000;
		int month = (record.getDate() % 10000) / 100;
		int day = record.getDate() % 100;
		if (year < 1 || month < 1 || month > 12 || day < 1 || day > 31) {
			return false;
		}
		// Check leap year.
		int daysInFebruary = 28;
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			daysInFebruary = 29;
		}
		// Check month.
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			if (day > 30) {
				return false;
			}
		}
		// Check February.
		if (month == 2) {
			if (day > daysInFebruary) {
				return false;
			}
		}
		// Check Weight.
		if (record.getWeight() < 0) {
			return false;
		}
		return true;
	}

	/*
	 * Check if input string is an integer.
	 */
	public boolean isPositiveInteger(String s) {
		if (s == null || s.equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(s).matches() && s.length() <= 10 && Long.parseLong(s) <= Integer.MAX_VALUE;
	}

	/*
	 * Return list of positions where errors occurred in error files.
	 */
	public List<Integer> getErrorPositions() {
		return this.positions;
	}
}
