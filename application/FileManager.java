/**
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

    // Constructor.
    public FileManager(Database database) {
	this.databse = database;
    }

    public List<File> addFiles(List<File> files) {
	List<File> errorFiles = new ArrayList<>();

	for (File file : files) {
	    try {
		Scanner scanner = new Scanner(file);
		boolean valid = true;
		List<OneRecord> records = new ArrayList<>();
		while (scanner.hasNextLine()) {
		    String line = scanner.nextLine().trim();
		    String[] data = line.split(",");
		    if (!this.checkData(data)) {
			errorFiles.add(file);
			valid = false;
			break;
		    } else {
			OneRecord record = new OneRecord(data[1].trim().toUpperCase(), 0,
				Integer.parseInt(data[2].trim()));
			String date = data[0];
			if (this.isDigit(date)) {
			    record.setDate(Integer.parseInt(date));
			} else {
			    String[] dateParts = data[0].trim().split("-");
			    record.setDate(Integer.parseInt(dateParts[0] + dateParts[1] + dateParts[2]));
			}
			records.add(record);
		    }
		}
		if (valid) {
		    for (OneRecord record : records) {
			this.databse.add(record);
		    }
		}
		scanner.close();
	    } catch (FileNotFoundException e) {
		errorFiles.add(file);
	    }
	}
	return errorFiles;
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
	OneRecord record = new OneRecord("", 0, 0);

	// Check 3 parts exist.
	if (data.length != 3) {
	    return false;
	}
	// Check date.
	String date = data[0];
	if (this.isDigit(date)) {
	    record.setDate(Integer.parseInt(date));
	} else {
	    String[] dateParts = data[0].trim().split("-");
	    if (dateParts.length != 3) {
		return false;
	    }
	    if (this.isDigit(dateParts[0]) && this.isDigit(dateParts[1]) && this.isDigit(dateParts[2])) {
		record.setDate(Integer.parseInt(dateParts[0] + dateParts[1] + dateParts[2]));
	    } else {
		return false;
	    }
	}
	// Set ID.
	record.setID(data[1].toUpperCase());
	// Set Weight.
	if (this.isDigit(data[2])) {
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
	if (record.getID() == null || record.getID().equals("")) {
	    return false;
	}
	// Check Date.
	if (record.getDate() < 0) {
	    return false;
	}
	int year = record.getDate() / 10000;
	int month = (record.getDate() % 10000) / 100;
	int day = record.getDate() % 100;
	if (year < 1 || month < 1 || month > 12 || day < 1 || day > 31) {
	    return false;
	}
	int Feburary = 28;
	if ((year / 4 == 0 && year / 100 != 0) || (year / 100 == 0 && year / 400 == 0)) {
	    Feburary = 29;
	}
	if (month == 4 || month == 6 || month == 9 || month == 11) {
	    if (day > 30) {
		return false;
	    }
	}
	if (month == 2) {
	    if (day > Feburary) {
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
    public boolean isDigit(String s) {
	Pattern pattern = Pattern.compile("[0-9]*");
	return pattern.matcher(s).matches();
    }
}
