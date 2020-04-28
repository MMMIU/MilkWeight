/*
 * GUI.java created by Yifei Miao in Milk Weight project.
 *
 * Author: Yifei Miao (ymiao29@wisc.edu) Date: 2020/04/21 Version : 1.0.0
 * 
 * Date 2020/4/28 Version: 1.1.0
 *
 * Course: COMPSCI 400 Lecture Number: 001 Semester: Spring 2020
 *
 * Collaborators: Tianyi Zhao, Chen Wang
 */
package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Yifei Miao
 *
 *         Defines operations on data manipulations and forming the required
 *         data for the visualizer.
 *
 *         Date formats: date: yyyymmdd (20200415) yearMonth: yyyymm (202004)
 *         year: yyyy (2020)
 *
 */
public class Database {

	// Public members.
	public static final int IDSMALLTOLARGE = 1;// FarmID, small to large.
	public static final int IDLARGETOSMALL = 2;// FarmID, large to small
	public static final int DATESMALLTOLARGE = 3;// Date, small to large.
	public static final int DATELARGETOSMALL = 4;// Date, large to small
	public static final int WEIGHTSMALLTOLARGE = 5;// Weight, small to large.
	public static final int WEIGHTLARGETOSMALL = 6;// Weight, large to small

	// Private members.
	private Map<String, List<OneRecord>> databaseUsingID;
	private Map<Integer, List<OneRecord>> databaseUsingMonth;
	private int size;
	private long totalWeight;

	// No-argument constructor.
	public Database() {
		this.databaseUsingID = new HashMap<>();
		this.databaseUsingMonth = new HashMap<>();
		this.size = 0;
	}

	/*
	 * Add a record to the database.
	 *
	 * @param farmID Name of the farm.
	 *
	 * @param date Date of the record.
	 *
	 * @param weight Weight of the record.
	 *
	 */
	public void add(String farmID, int date, int weight) {
		OneRecord record = new OneRecord(farmID.toUpperCase(), date, weight);
		this.add(record);
	}

	/*
	 * Add a record to the database. If duplicate record, add weight together.
	 *
	 * @param record
	 *
	 */
	public void add(OneRecord record) {
		// check existence.
		if (!this.databaseUsingID.containsKey(record.getFarmID().toUpperCase())) {
			this.databaseUsingID.put(record.getFarmID(), new ArrayList<OneRecord>());
		}
		if (!this.databaseUsingMonth.containsKey(record.getDate() / 100)) {
			this.databaseUsingMonth.put(record.getDate() / 100, new ArrayList<OneRecord>());
		}
		// Add to database.
		for (OneRecord r : this.databaseUsingID.get(record.getFarmID().toUpperCase())) {
			if (r.getDate() == record.getDate()) {
				this.totalWeight += record.getWeight();
				r.setWeight(r.getWeight() + record.getWeight());
				return;
			}
		}
		this.databaseUsingID.get(record.getFarmID()).add(record);
		this.databaseUsingMonth.get(record.getDate() / 100).add(record);
		this.totalWeight += record.getWeight();
		this.size++;
	}

	/*
	 * Remove a record in the database.
	 *
	 * @param farmID Name of the farm.
	 *
	 * @param date Date of the record.
	 *
	 * @returns True if the record is successfully inserted into the database. False
	 * otherwise.
	 *
	 */
	public boolean remove(String farmID, int date) {
		OneRecord record = new OneRecord(farmID.toUpperCase(), date, 0);
		return this.remove(record);
	}

	/*
	 * Remove a record in the database.
	 *
	 * @param farmID Name of the farm.
	 *
	 * @param date Date of the record.
	 *
	 * @returns True if the record is successfully inserted into the database. False
	 * otherwise.
	 *
	 */
	public boolean remove(OneRecord record) {
		// Check existence.
		record.setFarmID(record.getFarmID().toUpperCase());
		if (!this.databaseUsingID.containsKey(record.getFarmID())) {
			return false;
		} else {
			boolean exist = true;
			for (OneRecord e : this.databaseUsingID.get(record.getFarmID())) {
				if (e.getDate() == record.getDate()) {
					this.totalWeight -= e.getWeight();
					exist = true;
				}
			}
			if (!exist) {
				return false;
			}
		}
		// remove record from to maps.
		for (int i = 0; i < this.databaseUsingID.get(record.getFarmID()).size(); i++) {
			if (this.databaseUsingID.get(record.getFarmID()).get(i).getDate() == record.getDate()) {
				this.databaseUsingID.get(record.getFarmID()).remove(i);
				break;
			}
		}
		for (int i = 0; i < this.databaseUsingMonth.get(record.getDate() / 100).size(); i++) {
			if (this.databaseUsingMonth.get(record.getDate() / 100).get(i).getFarmID() == record.getFarmID()) {
				this.databaseUsingMonth.get(record.getDate() / 100).remove(i);
				break;
			}
		}
		this.size--;
		return true;
	}

	/*
	 * Check if record exist in the database.
	 */
	public boolean contains(OneRecord record) {
		if (this.databaseUsingID.containsKey(record.getFarmID())) {
			for (OneRecord e : this.databaseUsingID.get(record.getFarmID())) {
				if (e.getDate() == record.getDate()) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * Return weight for specified id and date.
	 *
	 */
	public int getWeightForAFarmUsingDate(String farmID, int date) {
		for (OneRecord e : this.databaseUsingID.get(farmID)) {
			if (e.getDate() == date)
				return e.getWeight();
		}
		return 0;
	}

	/*
	 * Return a list of all records. If data does not exist, return an empty list.
	 * Return empty list if no record.
	 *
	 */
	public List<OneRecord> getAllRecords() {
		List<OneRecord> result = new ArrayList<OneRecord>();
		Set<Entry<String, List<OneRecord>>> keys = this.databaseUsingID.entrySet();
		for (Entry<String, List<OneRecord>> e : keys) {
			result.addAll(e.getValue());
		}
		return result;
	}

	/*
	 * Return a list of all records in a month. If data does not exist, return an
	 * empty list. Return empty list if no record.
	 *
	 */
	public List<OneRecord> getAllRecordsInAMonth(int yearMonth) {
		List<OneRecord> result = new ArrayList<OneRecord>();
		Set<Integer> keys = this.databaseUsingMonth.keySet();
		for (int e : keys) {
			if (e == yearMonth) {
				for (OneRecord r : this.databaseUsingMonth.get(e)) {
					result.add(r);
				}
			}
		}
		return result;
	}

	/*
	 * Return a list of all records in a year. If data does not exist, return an
	 * empty list. Return empty list if no record.
	 *
	 */
	public List<List<OneRecord>> getAllRecordsInAYear(int year) {
		List<List<OneRecord>> result = new ArrayList<List<OneRecord>>();
		for (int i = 0; i < 12; i++) {
			result.add(new ArrayList<OneRecord>());
		}
		for (int yearMonth : this.databaseUsingMonth.keySet()) {
			if (yearMonth / 100 == year) {
				for (OneRecord r : this.databaseUsingMonth.get(yearMonth)) {
					result.get(yearMonth % 100 - 1).add(r);
				}
			}
		}
		return result;
	}

	/*
	 * Return a list of all records in a date range. If data does not exist, return
	 * an empty list. Return empty list if no record.
	 *
	 */
	public List<OneRecord> getAllRecordsInDateRange(int startDate, int endDate) {
		List<OneRecord> result = new ArrayList<OneRecord>();
		for (OneRecord r : this.getAllRecords()) {
			if (r.getDate() >= startDate && r.getDate() <= endDate) {
				result.add(r);
			}
		}
		return result;
	}

	/*
	 * Return a list of all records of a farm in a year hashed by month. Return list
	 * with 12 empty sublist if no record.
	 *
	 */
	public List<List<OneRecord>> getAllRecordsInAYearOfAFarm(String farmID, int year) {
		List<List<OneRecord>> result = new ArrayList<List<OneRecord>>();
		for (int i = 0; i < 12; i++) {
			result.add(new ArrayList<OneRecord>());
		}
		for (int yearMonth : this.databaseUsingMonth.keySet()) {
			if (yearMonth / 100 == year) {
				for (OneRecord r : this.databaseUsingMonth.get(yearMonth)) {
					if (r.getFarmID().equals(farmID)) {
						result.get(yearMonth % 100 - 1).add(r);
					}
				}
			}
		}
		return result;
	}

	/*
	 * Return a list contains all farmIDs in ascending order.
	 */
	public List<String> getFarmIDList() {
		List<String> result = new ArrayList<>();
		for (String e : this.databaseUsingID.keySet()) {
			result.add(e);
		}
		Collections.sort(result);
		return result;
	}

	/*
	 * Return a list contains all farmIDs in ascending order.
	 */
	public List<Integer> getMonthList() {
		List<Integer> result = new ArrayList<>();
		for (int e : this.databaseUsingMonth.keySet()) {
			result.add(e);
		}
		Collections.sort(result);
		return result;
	}

	/*
	 * Return a list contains all farmIDs in ascending order.
	 */
	public List<Integer> getMonthListOfAYear(int year) {
		List<Integer> result = new ArrayList<>();
		for (int e : this.databaseUsingMonth.keySet()) {
			if (e / 100 == year) {
				result.add(e % 100);
			}
		}
		Collections.sort(result);
		return result;
	}

	/*
	 * Return a list contains all years in ascending order.
	 */
	public List<Integer> getYearList() {
		List<Integer> result = new ArrayList<>();
		for (int e : this.databaseUsingMonth.keySet()) {
			if (!result.contains(e / 100)) {
				result.add(e / 100);
			}
		}
		Collections.sort(result);
		return result;
	}

	/*
	 * Return a list contains all years of a farm in ascending order.
	 */
	public List<Integer> getYearListOfAFarm(String farmID) {
		List<Integer> result = new ArrayList<>();
		for (int e : this.databaseUsingMonth.keySet()) {
			if (!result.contains(e / 100)) {
				for (OneRecord r : this.databaseUsingMonth.get(e)) {
					if (r.getFarmID().equals(farmID)) {
						result.add(e / 100);
						break;
					}
				}
			}
		}
		Collections.sort(result);
		return result;
	}

	/*
	 * Return a list contains all dates in the database in ascending order.
	 */
	public List<Integer> getAllDates() {
		List<Integer> result = new ArrayList<Integer>();
		for (OneRecord e : this.getAllRecords()) {
			if (!result.contains(e.getDate())) {
				result.add(e.getDate());
			}
		}
		Collections.sort(result);
		return result;
	}

	/*
	 * Return the total weight of all records in the database.
	 */
	public long totalWeight() {
		return this.totalWeight;
	}

	/*
	 * Return the size of the database.
	 */
	public int size() {
		return this.size;
	}

	/*
	 * Clear the database.
	 */
	public void clear() {
		this.databaseUsingID = new HashMap<>();
		this.databaseUsingMonth = new HashMap<>();
		this.size = 0;
		this.totalWeight = 0;
	}
}
