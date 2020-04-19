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
	if (!this.databaseUsingID.containsKey(record.getFarmID().toUpperCase())) {
	    this.databaseUsingID.put(record.getFarmID(), new ArrayList<OneRecord>());
	}
	if (!this.databaseUsingMonth.containsKey(record.getDate() / 100)) {
	    this.databaseUsingMonth.put(record.getDate() / 100, new ArrayList<OneRecord>());
	}
	for (OneRecord r : this.databaseUsingID.get(record.getFarmID().toUpperCase())) {
	    if (r.getDate() == record.getDate()) {
		r.setWeight(r.getWeight() + record.getWeight());
		return;
	    }
	}
	this.databaseUsingID.get(record.getFarmID()).add(record);
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
	OneRecord record = new OneRecord(farmID.toUpperCase(), date, 0);
	return this.remove(record);
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
	record.setFarmID(record.getFarmID().toUpperCase());
	if (!this.databaseUsingID.containsKey(record.getFarmID())) {
	    return false;
	} else {
	    boolean exist = true;
	    for (OneRecord e : this.databaseUsingID.get(record.getFarmID())) {
		if (e.getDate() == record.getDate()) {
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
     * Return a list of all records in a month. If data does not exist,
     * return an empty list. Return empty list if no record.
     *
     */
    public List<OneRecord> getAllRecordsInAMonth(int yearMonth) {
    	List<OneRecord> result = new ArrayList<OneRecord>();
    	Set<Integer> keys = this.databaseUsingMonth.keySet();
    	for (int e : keys) {
    	    if(e/100==yearMonth){
    	    	for(OneRecord r:this.databaseUsingMonth.get(e)) {
        			result.add(r);
        		}
    	    }
    	}
    	    return result;
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
     * Return a list of all records in a date range. If data does not exist,
     * return an empty list. Return empty list if no record.
     *
     */
    public List<OneRecord> getAllRecordsInDateRange(int startDate, int endDate) {
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
     * Return a list contains all farmIDs in ascending order.
     */
    public List<String> getFarmIDList(){
    	List<String> result=new ArrayList<>();
    	for(String e:this.databaseUsingID.keySet()) {
    		result.add(e);
    	}
    	Collections.sort(result);
		return result;
    	
    }
    /*
     * Return a list contains all farmIDs in ascending order.
     */
    public List<Integer> getMonthList(){
    	List<Integer> result=new ArrayList<>();
    	for(int e:this.databaseUsingMonth.keySet()) {
    		result.add(e);
    	}
    	Collections.sort(result);
		return result;
    	
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
