/**
 *
 */
package application;

import java.util.ArrayList;
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
	if (!this.databaseUsingID.containsKey(record.getID().toUpperCase())) {
	    this.databaseUsingID.put(record.getID(), new ArrayList<OneRecord>());
	}
	if (!this.databaseUsingMonth.containsKey(record.getDate() / 100)) {
	    this.databaseUsingMonth.put(record.getDate() / 100, new ArrayList<OneRecord>());
	}
	for (OneRecord r : this.databaseUsingID.get(record.getID().toUpperCase())) {
	    if (r.getDate() == record.getDate()) {
		r.setWeight(r.getWeight() + record.getWeight());
		return;
	    }
	}
	this.databaseUsingID.get(record.getID()).add(record);
	this.databaseUsingMonth.get(record.getDate() / 100).add(record);
	this.size++;
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
	if (!this.databaseUsingID.containsKey(tmp.getID().toUpperCase())) {
	    return false;
	}
	for (int i = 0; i < this.databaseUsingID.get(farmID.toUpperCase()).size(); i++) {
	    if (this.databaseUsingID.get(farmID.toUpperCase()).get(i).getDate() == date) {
		this.databaseUsingID.get(farmID.toUpperCase()).remove(i);
		break;
	    }
	}
	for (int i = 0; i < this.databaseUsingMonth.get(date / 100).size(); i++) {
	    if (this.databaseUsingMonth.get(date / 100).get(i).getDate() == date) {
		this.databaseUsingMonth.get(date / 100).remove(i);
		break;
	    }
	}
	this.size--;
	return true;
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
    public boolean remove(OneRecord record) {
	record.setID(record.getID().toUpperCase());
	if (!this.databaseUsingID.containsKey(record.getID())) {
	    return false;
	}
	for (int i = 0; i < this.databaseUsingID.get(record.getID()).size(); i++) {
	    if (this.databaseUsingID.get(record.getID()).get(i).getDate() == record.getDate()) {
		this.databaseUsingID.get(record.getID()).remove(i);
		break;
	    }
	}
	for (int i = 0; i < this.databaseUsingMonth.get(record.getDate() / 100).size(); i++) {
	    if (this.databaseUsingMonth.get(record.getDate() / 100).get(i).getID() == record.getID().toUpperCase()) {
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
     * Return a list of all records. If data does not exist,
     * return an empty list. Return empty list if no record.
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
     * Return a sorted list of all records. If data does not exist,
     * return an empty list. Return empty list if no record.
     *
     */
    public List<OneRecord> getAllRecords(int order) {
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
     * that year. Format:("",202004,10000).
     *
     */
    public OneRecord getMonthlyMinOfAYear(int year) {
	return null;
    }

    /*
     * Return largest weight of all months in a year. Return (0,0) if no record in
     * that year. Format:("",202004,10000).
     *
     */
    public OneRecord getMonthlyMaxOfAYear(int year) {
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
	return this.size;
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
