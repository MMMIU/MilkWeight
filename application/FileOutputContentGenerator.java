/*
 * FileOutputContentGenerator.java created by Yifei Miao in Milk Weight project.
 *
 * Author: Yifei Miao (ymiao29@wisc.edu) Date: 2020/04/30 Version : 1.1.0
 * 
 * Date 2020/4/28 Version: 1.1.0
 *
 * Course: COMPSCI 400 Lecture Number: 001 Semester: Spring 2020
 * 
 * Collaborator: Chenxi Gao
 *
 */
package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Yifei Miao
 *
 */
public class FileOutputContentGenerator {
	Database database;

	public FileOutputContentGenerator(Database database) {
		this.database = database;
	}

	/*
	 * Prompt user for a farm id and year (or use all available data)
	 * 
	 * Then, return a list of the total milk weight and percent of the total of all
	 * farm for
	 * each month.
	 * 
	 * Sort, the list by month number 1-12, show total weight, then that farm's
	 * percent of the total milk received for each month.
	 */
	public String farmReport(String farmID, int year) {
		String result = "";
		List<List<OneRecord>> list = this.database.getAllRecordsInAYearOfAFarm(farmID, year);
		boolean empty = true;
		for (List<OneRecord> x : list) {
			if (x.size() > 0) {
				empty = false;
				break;
			}
		}
		if (!empty) {
			// Get total weight of a year.
			long yearTotalWeight = 0;
			long farmTotalWeight = 0;
			for (List<OneRecord> months : this.database.getAllRecordsInAYear(year)) {
				for (OneRecord record : months) {
					yearTotalWeight += record.getWeight();
				}
			}
			for (int i = 0; i < 12; i++) {
				List<OneRecord> records = list.get(i);
				int min = -1;
				int max = -1;
				long total = 0;
				double average = 0;
				for (OneRecord r : records) {
					if (min == -1 || r.getWeight() < min) {
						min = r.getWeight();
					}
					if (max == -1 || r.getWeight() > max) {
						max = r.getWeight();
					}
					total += r.getWeight();
					farmTotalWeight += r.getWeight();
				}
				if (min == -1) {
					max = min = 0;
				}
				double monthPercent = 0;
				double minPercent = 100;
				double maxPercent = 100;
				if (total != 0) {
					long monthTotal = 0;
					for (OneRecord record : this.database.getAllRecordsInAYear(year).get(i)) {
						monthTotal += record.getWeight();
					}
					monthPercent = 100.0 * monthTotal / yearTotalWeight;
					minPercent = 100.0 * min / total;
					maxPercent = 100.0 * max / total;
					average = 1.0 * total / records.size();
				}
				result += String.format("\n%3s, %9d(%.3f), %d(%.3f), %d(%.3f), %.3f", (i + 1), total, monthPercent, min,
						minPercent, max, maxPercent, average);
			}
			result = "Farm report: Min, max, average weight by month for " + farmID + " in " + year + ":"
					+ "\n\nAll farms year total weight, farm total weight, percent:\n" + yearTotalWeight + ", "
					+ farmTotalWeight + ", " + String.format("%.3f", 100.0 * farmTotalWeight / yearTotalWeight)
					+ "\n\nMonth, month total(percent of the year total(%)), min(percent of the month total(%)), max(percent of the month total(%)), average:"
					+ result;
		}
		return result;
	}

	/*
	 * Ask for year and month.
	 * 
	 * Then, display a list of min, max, average weight, farm by farm.
	 * 
	 * The list is sorted by Farm ID.
	 */
	public String monthlyFarmReport(int year, int month) {
		String result = "";
		List<OneRecord> list = this.database.getAllRecordsInAMonth(year * 100 + month);
		result = "Monthly farm report: Min, max, average weight for all farms in " + monthTrans(month) + ", " + year
				+ ":\n\nFarm ID, min(date)(percent of the month total(%)), max(date)(percent of the month total(%)), average:";
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
			result += String.format("\n%10s, %6d(%d)(%.3f), %6d(%d)(%.3f), %9.3f", record.getFarmID(), min, minDate,
					minPercent, max, maxDate, maxPercent, average);
		}
		return result;
	}

	/*
	 * Ask for year and month.
	 * 
	 * Then, display a list of totals and percent of total by farm.
	 * 
	 * The list must be sorted by Farm ID, or you can prompt for ascending or
	 * descending by weight.
	 */
	public String monthlyReportYM(int year, int month) {
		String result = "";
		List<OneRecord> list = this.database.getAllRecordsInAYear(year).get(month - 1);
		result = "Monthly report: Each farm's total weight and percent of the month toal in " + monthTrans(month) + ", "
				+ year + ":";
		this.sortUsingIDUp(list);
		long totalWeight = 0;
		for (OneRecord r : list) {
			totalWeight += r.getWeight();
		}
		result += "\n\nTotal Weight:\n" + totalWeight
				+ "\n\nFarm ID, total weight in the month, percent of month total(%):";
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
			result += String.format("\n%10s, %8d, %.3f", record.getFarmID(), farmTotal, share);

		}
		return result;
	}

	/*
	 * Ask for year.
	 * 
	 * Then display list of total weight and percent of total weight of all farms by
	 * farm for the year.
	 * 
	 * Sort by Farm ID.
	 */
	public String annualReportY(int year) {
		String result = "";
		List<List<OneRecord>> inlist = this.database.getAllRecordsInAYear(year);
		List<OneRecord> list = new ArrayList<OneRecord>();
		for (List<OneRecord> li : inlist) {
			for (OneRecord r : li) {
				list.add(r);
			}
		}
		sortUsingIDUp(list);
		long totalWeight = 0;
		for (OneRecord r : list) {
			totalWeight += r.getWeight();
		}
		result = "Annual report: Each farm's total weight and percent of the month toal in " + year + ":"
				+ "\n\nTotal weight:\n" + totalWeight
				+ "\n\nFarm ID, total weight in the year, percent of year total(%):";
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
			result += String.format("\n%10s, %8d, %.3f", record.getFarmID(), farmTotal, share);
		}
		return result;
	}

	/*
	 * Prompt user for start date (year-month-day) and end month-day,
	 * 
	 * Then display the total milk weight per farm and the percentage of the total
	 * for each farm over that date range.
	 * 
	 * The list is sorted by Farm ID.
	 */
	public String dateRangeReport(int startDate, int endDate) {
		String result = "";
		List<OneRecord> list = this.database.getAllRecordsInDateRange(startDate, endDate);
		sortUsingIDUp(list);
		long totalWeight = 0;
		for (OneRecord r : list) {
			totalWeight += r.getWeight();
		}
		result = "Date range report: Total milk weight per farm and the percentage of the total for each farm between "
				+ startDate + " and " + endDate + "\n\nTotal weight:\n" + totalWeight
				+ "\n\nFarm ID, total weight in the date range, percent of total weight in the date range(%):";
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
			result += String.format("\n%10s, %8d, %.3f", record.getFarmID(), farmTotal, share);
		}
		return result;
	}

	/*
	 * Return correspondent string according to given month
	 */
	private String monthTrans(int month) {
		switch (month) {
		case 1:
			return "January";
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

	/*
	 * Given a list of records, sort the list according to farmID dictionary order.
	 */
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
}
