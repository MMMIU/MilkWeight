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

    Database databse;

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

		    }
		}
		if (valid) {
		    for (OneRecord record : records) {
			this.databse.add(record);
		    }
		}
	    } catch (FileNotFoundException e) {
		errorFiles.add(file);
	    }
	}
	return errorFiles;
    }

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
	    dateParts[0] = dateParts[0].trim();
	    dateParts[1] = dateParts[1].trim();
	    dateParts[2] = dateParts[2].trim();
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

    public boolean checkRecordValid(OneRecord record) {
	// Check ID.
	if (record.getID() == null || record.getID().equals("")) {
	    return false;
	}
	// Check Date.
	if (record.getDate() < 0) {
	    return false;
	}
	// Check Weight.
	if (record.getWeight() < 0) {
	    return false;
	}
	return true;
    }

    public boolean isDigit(String s) {
	Pattern pattern = Pattern.compile("[0-9]*");
	return pattern.matcher(s).matches();
    }
}
