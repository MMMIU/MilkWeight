/**
 *
 */
package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yifei Miao
 *
 *         Defines operations on data manipulations and forming the required
 *         data for the visualizer.
 *
 *         Date formats:
 *         date: yyyymmdd (20200415)
 *         yearMonth: yyyymm (202004)
 *         year: yyyy (2020)
 *
 *         order==1-->farm id small-large
 *         2-->farm id Reverse order
 *         3-->date small-large
 *         4-->date Reverse order
 *         5-->weight small-large
 *         6-->weight Reverse order
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

    // Private class.
    private class Pair {
	int month;
	int weight;

	public Pair(int month, int weight) {
	    this.month = month;
	    this.weight = weight;
	}
    }

    // No-argument constructor.
    public Database() {
	this.databaseUsingID = new HashMap<>();
	this.databaseUsingMonth = new HashMap<>();
	this.size = 0;
    }

    /*
     * Add a record to the database.
     *
     * @param farmID
     * Name of the farm.
     *
     * @param date
     * Date of the record.
     *
     * @param weight
     * Weight of the record.
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
	record.setID(record.getID().toUpperCase());
	if (!this.contains(record.getID())) {
	    List<OneRecord> tmp = new ArrayList<OneRecord>();
	    tmp.add(record);
	    this.databaseUsingID.put(record.getID(), tmp);
	    this.databaseUsingMonth.put(record.getDate() / 100, tmp);
	} else {
	    if (this.containsSameIDAndDate(record)) {
		List<OneRecord> tmp = this.databaseUsingID.get(record.getID());
		for (OneRecord e : tmp) {
		    if (e.getDate() == record.getDate()) {
			e.setWeight(e.getWeight() + record.getWeight());
			break;
		    }
		}
	    } else {
		this.databaseUsingID.get(record.getID()).add(record);
		this.databaseUsingMonth.get(record.getDate() / 100).add(record);
		this.size++;
	    }
	}
    }

    /*
     * Return true if farmID already exists in the database.
     */
    public boolean contains(String farmID) {
	return this.databaseUsingID.containsKey(farmID.toUpperCase());
    }

    /*
     * Return true if record date already exists in the database.
     */
    public boolean containsSameIDAndDate(OneRecord record) {
	for (OneRecord r : this.databaseUsingID.get(record.getID().toUpperCase())) {
	    if (r.getDate() == record.getDate()) {
		return true;
	    }
	}
	return false;
    }

    /*
     * Remove a record in the database.
     *
     * @param farmID
     * Name of the farm.
     *
     * @param date
     * Date of the record.
     *
     * @returns
     * True if the record is successfully inserted into the database.
     * False otherwise.
     *
     */
    public boolean remove(String farmID, int date) {
	OneRecord tmp = new OneRecord(farmID.toUpperCase(), date, 0);
	if (!this.containsSameIDAndDate(tmp)) {
	    return false;
	}
	for (int i = 0; i < this.databaseUsingID.get(farmID.toUpperCase()).size(); i++) {
	    if (this.databaseUsingID.get(farmID.toUpperCase()).get(i).getDate() == date) {
		this.databaseUsingID.get(farmID.toUpperCase()).remove(i);
		break;
	    }
	}
	for (int i = 0; i < this.databaseUsingMonth.get(date).size(); i++) {
	    if (this.databaseUsingMonth.get(date).get(i).getDate() == date) {
		this.databaseUsingMonth.get(date).remove(i);
		break;
	    }
	}
	this.size--;
	return true;
    }

    /*
     * Remove all records of a farm in the database.
     *
     * @param farmID
     * Name of the farm.
     *
     * @returns
     * True if the record is successfully inserted into the database.
     * False otherwise.
     */
    public boolean removeAFarm(String farmID) {
	return false;
    }

    /*
     * Return weight for specified id and date.
     *
     */
    public int getWeightForAFarmUsingDate(String farmID, int date) {
	// TODO
	return 0;
    }

    /*
     * Return a list of all dates for specified id and weight. Return empty list if
     * no record.
     *
     */
    public List<Integer> getDateForAFarmUsingWeight(String farmID, int weight) {
	// TODO
	return null;
    }

    /*
     * Return a list of all records in a month. If data does not exist,
     * return an empty list. Return empty list if no record.
     *
     */
    public List<OneRecord> getAllRecordsInAMonth(int yearMonth) {
	// TODO
	return null;
    }

    /*
     * Return a sorted list of all records in a month. If data does not exist,
     * return an empty list. Return empty list if no record.
     *
     */
    public List<OneRecord> getAllRecordsInAMonth(int yearMonth, int order) {
	// TODO
	return null;
    }

    /*
     * Return a list of all records in a year. If data does not exist,
     * return an empty list. Return empty list if no record.
     *
     */
    public List<OneRecord> getAllRecordsInAYear(int year) {
	// TODO
	return null;
    }

    /*
     * Return a sorted list of all records in a year. If data does not exist,
     * return an empty list. Return empty list if no record.
     *
     */
    public List<OneRecord> getAllRecordsInAYear(int year, int order) {
	// TODO
	return null;
    }

    /*
     * Return a list of all records in a date range. If data does not exist,
     * return an empty list. Return empty list if no record.
     *
     */
    public List<OneRecord> getAllRecordsInDateRange(int startDate, int endDate) {
	// TODO
	return null;
    }

    /*
     * Return a sorted list of all records in a date range. If data does not exist,
     * return an empty list. Return empty list if no record.
     *
     */
    public List<OneRecord> getAllRecordsInDateRange(int startDate, int endDate, int order) {
	// TODO
	return null;
    }

    /*
     * Return a list of all records of a farm. If farm does not exist, return an
     * empty list. Return empty list if no record.
     *
     */
    public List<OneRecord> getAllRecordsOfAFarm(String farmID) {
	// TODO
	return null;
    }

    /*
     * Return a sorted list of all records of a farm.If farm does not exist, return
     * an empty list. Return empty list if no record.
     *
     */
    public List<OneRecord> getAllRecordsOfAFarm(String farmID, int order) {
	// TODO
	return null;
    }

    /*
     * Given a list of records. Sort the list according to specified order
     * order==1-->farm id small-large
     * 2-->farm id large-small
     * 3-->date small-large
     * 4-->date large-small
     * 5-->weight small-large
     * 6-->weight large-small
     */
    public void sort(List<OneRecord> list, int order) {

    }

    /*
     * Return average weight of all months in a year. Return 0 if no record in
     * that year.
     *
     */
    public int getMonthlyAverageOfAYear(int year) {
	return 0;
    }

    /*
     * Return smallest weight of all months in a year. Return (0,0) if no record in
     * that year.
     *
     */
    public Pair getMonthlyMinOfAYear(int year) {
	return null;
    }

    /*
     * Return largest weight of all months in a year. Return (0,0) if no record in
     * that year.
     *
     */
    public Pair getMonthlyMaxOfAYear(int year) {
	return null;
    }

    /*
     * Return average weight of all records of a farm.
     *
     */
    public int getAverageOfAFarm(String farmID) {
	return 0;
    }

    /*
     * Return the record with smallest weight of all records of a farm. Return
     * ("null","0","0") if no record of that farm.
     *
     */
    public OneRecord getMinOfAFarm(String farmID) {
	return null;
    }

    /*
     * Return the record with largest weight of all records of a farm. Return
     * ("null","0","0") if no record of that farm.
     *
     */
    public OneRecord getMaxOfAFarm(String farmID) {
	return null;
    }

    /*
     * Return average weight of all records in a date range.
     *
     */
    public int getAverageInDateRange(int srartDate, int endDate) {
	return 0;
    }

    /*
     * Return the record with smallest weight of all records in a date range. Return
     * ("null","0","0") if no record in that date range.
     * that year.
     *
     */
    public OneRecord getMinInDateRange(int srartDate, int endDate) {
	return null;
    }

    /*
     * Return the record with largest weight of all records in a date range. Return
     * ("null","0","0") if no record in that date range.
     *
     */
    public OneRecord getMaxInDateRange(int srartDate, int endDate) {
	return null;
    }

    /*
     * Return the size of the database.
     */
    public int size() {
	return this.size();
    }

    /*
     * Clear the database.
     */
    public void clear() {
	this.databaseUsingID = new HashMap<>();
	this.databaseUsingMonth = new HashMap<>();
	this.size = 0;
    }
}
