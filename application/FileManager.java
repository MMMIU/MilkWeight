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
	Pattern pattern = Pattern.compile("[0-9]*");
	for (File file : files) {
	    try {
		boolean valid = true;
		List<OneRecord> records = new ArrayList<>();
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
		    String line = scanner.nextLine().trim();
		    String[] data = line.split(",");
		    if (data.length != 3) {
			errorFiles.add(file);
			valid = false;
			break;
		    }
		    // || pattern.matcher(data[1]).matches() || pattern.matcher(data[2]).matches()
		    String[] dateString = data[0].trim().split("-");
		    if (dateString.length != 3) {
			errorFiles.add(file);
			valid = false;
			break;
		    }
		}
		if (valid) {
		    for (OneRecord record : records) {
		    }
		}
	    } catch (FileNotFoundException e) {
		errorFiles.add(file);
	    }
	}
	return errorFiles;
    }
}
