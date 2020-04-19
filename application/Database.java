/**
 *
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
	 * Add a record to the database.
	 *
	 * @param record
	 *
	 */
	public void add(OneRecord record) {
		if (!this.databaseUsingID.containsKey(record.getFarmID().toUpperCase())) {
			this.databaseUsingID.put(record.getFarmID(), new ArrayList<OneRecord>());
		}
		if (!this.databaseUsingMonth.containsKey(record.getDate() / 100)) {
			this.databaseUsingMonth.put(record.getDate() / 100, new ArrayList<OneRecord>());
		}
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
		for (int i = 0; i < this.databaseUsingID.get(record.getFarmID()).size(); i++) {
			if (this.databaseUsingID.get(record.getFarmID()).get(i).getDate() == record.getDate()) {
				this.databaseUsingID.get(record.getFarmID()).remove(i);
				break;
			}
		}
		for (int i = 0; i < this.databaseUsingMonth.get(record.getDate() / 100).size(); i++) {
			if (this.databaseUsingMonth.get(record.getDate() / 100).get(i).getFarmID() == record.getFarmID()
					.toUpperCase()) {
				this.databaseUsingMonth.get(record.getDate() / 100).remove(i);
				break;
			}
		}
		this.size--;
		return true;
	}

	/*
	 * Remove all records of a farm in the database.
	 *
	 * @param farmID Name of the farm.
	 *
	 * @returns True if the record is successfully deleted from the database. False
	 * otherwise.
	 */
	public boolean removeAFarm(String farmID) {
		if (!this.databaseUsingID.containsKey(farmID)) {
			return false;
		} else {
			boolean exist = true;
			Set<String> keys = this.databaseUsingID.keySet();
			for (String e : keys) {
				if (e == farmID) {
					exist = true;
				}
			}
			if (!exist) {
				return false;
			}

		}
		for (int i = 0; i < this.databaseUsingID.get(farmID).size(); i++) {
			if (this.databaseUsingID.get(farmID).get(i).getFarmID() == farmID) {
				this.databaseUsingID.get(farmID).remove(i);
				break;
			}
		}

		this.size--;
		return true;
	}

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
	 * Return a list of all dates for specified id and weight. Return empty list if
	 * no record.
	 *
	 */
	public List<Integer> getDateForAFarmUsingWeight(String farmID, int weight) {
		List<Integer> dateList = new ArrayList<Integer>();
		for (OneRecord e : this.databaseUsingID.get(farmID)) {
			if (e.getWeight() == weight)
				dateList.add(e.getDate());
		}
		return dateList;
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
			if (e / 100 == yearMonth) {
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
	public List<OneRecord> getAllRecordsInAYear(int year) {
		List<OneRecord> result = new ArrayList<OneRecord>();
		Set<Integer> keys = this.databaseUsingMonth.keySet();
		for (int e : keys) {
			if (e / 10000 == year) {
				for (OneRecord r : this.databaseUsingMonth.get(e)) {
					result.add(r);
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
		Set<Integer> keys = this.databaseUsingMonth.keySet();
		for (int e : keys) {
			if (e >= startDate && e <= endDate) {
				for (OneRecord r : this.databaseUsingMonth.get(e)) {
					result.add(r);
				}
			}
		}
		return result;
	}

	/*
	 * Return a list of all records of a farm. If farm does not exist, return an
	 * empty list. Return empty list if no record.
	 *
	 */
	public List<OneRecord> getAllRecordsOfAFarm(String farmID) {
		List<OneRecord> result = new ArrayList<OneRecord>();
		Set<String> keys = this.databaseUsingID.keySet();
		for (String e : keys) {
			if (e == farmID) {
				for (OneRecord r : this.databaseUsingID.get(e)) {
					result.add(r);
				}
			}
		}
		return result;
	}

	/*
	 * Return average weight of all months in a year. Return 0 if no record in that
	 * year.
	 *
	 */
	public long getMonthlyAverageOfAYear(int year) {
		long sum = 0;
		int numOfData = 0;
		for (int i = 1; i < 13; i++) {
			for (OneRecord e : this.databaseUsingMonth.get(i)) {
				if (e.getDate() / 10000 == year) {
					sum += e.getWeight();
					numOfData++;
				}
			}
		}
		return sum / numOfData;
	}

	/*
	 * Return smallest weight of all months in a year. Return (0,0) if no record in
	 * that year. Format:("",202004,10000).
	 *
	 */
	public OneRecord getMonthlyMinOfAYear(int year) {
		OneRecord minMonth = new OneRecord("", 0, 0);
		int sum = 0;
		for (int i = 1; i < 13; i++) {
			for (OneRecord e : this.databaseUsingMonth.get(i)) {
				if ((int) (e.getDate() / 10000) == year)
					sum += e.getWeight();
			}
			if (sum < minMonth.getWeight()) {
				minMonth = new OneRecord("", year * 100 + i, sum);
			}
			sum = 0;
		}
		return minMonth;
	}

	/*
	 * Return largest weight of all months in a year. Return (0,0) if no record in
	 * that year. Format:("",202004,10000).
	 *
	 */
	public OneRecord getMonthlyMaxOfAYear(int year) {
		OneRecord maxMonth = new OneRecord("", 0, 0);
		int sum = 0;
		for (int i = 1; i < 13; i++) {
			for (OneRecord e : this.databaseUsingMonth.get(i)) {
				if ((int) (e.getDate() / 10000) == year)
					sum += e.getWeight();
			}
			if (sum > maxMonth.getWeight()) {
				maxMonth = new OneRecord("", year * 100 + i, sum);
			}
			sum = 0;
		}
		return maxMonth;
	}

	/*
	 * Return average weight of all records of a farm.
	 *
	 */
	public long getAverageOfAFarm(String farmID) {
		long sum = 0;
		int numOfData = 0;
		for (OneRecord e : this.databaseUsingID.get(farmID)) {
			sum += e.getWeight();
			numOfData++;
		}
		return sum / numOfData;
	}

	/*
	 * Return the record with smallest weight of all records of a farm. Return
	 * ("null","0","0") if no record of that farm.
	 *
	 */
	public OneRecord getMinOfAFarm(String farmID) {
		OneRecord minRecord = new OneRecord("null", 0, 0);
		for (OneRecord e : this.databaseUsingID.get(farmID)) {
			if (e.getWeight() < minRecord.getWeight())
				minRecord = e;
		}
		return minRecord;
	}

	/*
	 * Return the record with largest weight of all records of a farm. Return
	 * ("null","0","0") if no record of that farm.
	 *
	 */
	public OneRecord getMaxOfAFarm(String farmID) {
		OneRecord maxRecord = new OneRecord("null", 0, 0);
		for (OneRecord e : this.databaseUsingID.get(farmID)) {
			if (e.getWeight() > maxRecord.getWeight())
				maxRecord = e;
		}
		return maxRecord;
	}

	/*
	 * Return average weight of all records in a date range.
	 *
	 */
	public int getAverageInDateRange(int startDate, int endDate) {
		List<OneRecord> givenRangeRecords = getAllRecordsInDateRange(startDate, endDate);
		int sum = 0;
		for (OneRecord e : givenRangeRecords) {
			sum += e.getWeight();
		}
		return sum / givenRangeRecords.size();
	}

	/*
	 * Return the record with smallest weight of all records in a date range. Return
	 * ("null","0","0") if no record in that date range. that year.
	 *
	 */
	public OneRecord getMinInDateRange(int startDate, int endDate) {
		List<OneRecord> givenRangeRecords = getAllRecordsInDateRange(startDate, endDate);
		OneRecord minRecord = new OneRecord("", 0, 0);
		for (OneRecord e : givenRangeRecords) {
			if (e.getWeight() < minRecord.getWeight())
				minRecord = e;
		}
		return minRecord;
	}

	/*
	 * Return the record with largest weight of all records in a date range. Return
	 * ("null","0","0") if no record in that date range.
	 *
	 */
	public OneRecord getMaxInDateRange(int startDate, int endDate) {
		List<OneRecord> givenRangeRecords = getAllRecordsInDateRange(startDate, endDate);
		OneRecord maxRecord = new OneRecord("", 0, 0);
		for (OneRecord e : givenRangeRecords) {
			if (e.getWeight() > maxRecord.getWeight())
				maxRecord = e;
		}
		return maxRecord;
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
		this.totalWeight=0;
	}
}
