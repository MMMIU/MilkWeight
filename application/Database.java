/**
 *
 */
package application;

import java.util.ArrayList;
import java.util.List;

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
    private List<OneRecord> database;

    // No-argument constructor.
    public Database() {
	this.database = new ArrayList<>();
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
     * @returns
     * True if the record is successfully inserted into the database.
     * False otherwise.
     *
     * @throws IDNotValidException
     * ID is null or empty.
     *
     * @throws DateNotValidException
     * Date is earlier than 0001-01-01.
     *
     * @throws WeightNotValidException
     * Weight is smaller than 0.
     *
     * @throws DuplicateDateException
     * There is already a record with same id and date in the database.
     */
    public boolean add(String farmID, int date, int weight)
	    throws IDNotValidException, DateNotValidException, WeightNotValidException, DuplicateDateException {
	if (farmID == null || farmID.equals("")) {
	    throw new IDNotValidException();
	}
	if (date <= 10101) {
	    throw new DateNotValidException();
	}
	if (weight <= 0) {
	    throw new WeightNotValidException();
	}
	for (OneRecord e : this.getAllRecordsOfAFarm(farmID)) {
	    if (e.getDate() == date) {
		throw new DuplicateDateException();
	    }
	}
	this.database.add(new OneRecord(farmID, date, weight));
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
     * @throws IDNotValidException
     * ID is null or empty or farm does not exist.
     *
     * @throws DateNotValidException
     * Date is earlier than 0001-01-01 or does not exist.
     */
    public boolean remove(String farmID, int date) throws IDNotValidException, DateNotValidException {
	return false;
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
     *
     * @throws IDNotValidException
     * ID is null or empty or farm does not exist.
     */
    public boolean removeAFarm(String farmID) throws IDNotValidException {
	return false;
    }

    /*
     * Return weight for specified id and date.
     *
     * @param farmID
     * Name of the farm.
     *
     * @param date
     * Date of the record.
     *
     * @throws IDNotValidException
     * ID is null or empty.
     *
     * @throws DateNotValidException
     * Date is earlier than 0001-01-01 or Farm does not have a record on that date.
     */
    public int getWeightForAFarmUsingDate(String farmID, int date) throws IDNotValidException, DateNotValidException {
	// TODO
	return 0;
    }

    /*
     * Return a list of all dates for specified id and weight.
     *
     * @throws IDNotValidException
     * ID is null or empty.
     *
     * @throws DateNotValidException
     * If no date of that id and weight is find in the database.
     *
     * @throws WeightNotValidException
     * Weight is smaller than 0.
     */
    public List<Integer> getDateForAFarmUsingWeight(String farmID, int weight)
	    throws IDNotValidException, DateNotValidException, WeightNotValidException {
	// TODO
	return null;
    }

    /*
     * Return a list of all records in a month.
     *
     * @throws DateNotValidException
     * If no record exists in that month.
     *
     */
    public List<OneRecord> getAllRecordsInAMonth(int yearMonth) throws DateNotValidException {
	// TODO
	return null;
    }

    /*
     * Return a sorted list of all records in a month.
     *
     * @throws DateNotValidException
     * If no record exists in that month.
     *
     */
    public List<OneRecord> getAllRecordsInAMonth(int yearMonth, int order) throws DateNotValidException {
	// TODO
	return null;
    }

    /*
     * Return a list of all records in a year.
     *
     * @throws DateNotValidException
     * If no record exists in that year.
     *
     */
    public List<OneRecord> getAllRecordsInAYear(int year) throws DateNotValidException {
	// TODO
	return null;
    }

    /*
     * Return a sorted list of all records in a year.
     *
     * @throws DateNotValidException
     * If year is smaller than 1 or no record exists in that year.
     *
     */
    public List<OneRecord> getAllRecordsInAYear(int year, int order) throws DateNotValidException {
	// TODO
	return null;
    }

    /*
     * Return a list of all records in a date range.
     *
     * @throws DateNotValidException
     * If either of the date is smaller than 10101 or endDate is smaller than
     * startDate or no record exists between two dates.
     */
    public List<OneRecord> getAllRecordsInDateRange(int startDate, int endDate) throws DateNotValidException {
	// TODO
	return null;
    }

    /*
     * Return a sorted list of all records in a date range.
     *
     * @throws DateNotValidException
     * If either of the date is smaller than 10101 or endDate is smaller than
     * startDate or no record exists between two dates.
     */
    public List<OneRecord> getAllRecordsInDateRange(int startDate, int endDate, int order)
	    throws DateNotValidException {
	// TODO
	return null;
    }

    /*
     * Return a list of all records of a farm.
     *
     * @throws IDNotValidException
     * ID is null or empty or no farm with that ID in the database.
     */
    public List<OneRecord> getAllRecordsOfAFarm(String farmID) throws IDNotValidException {
	// TODO
	return null;
    }

    /*
     * Return a sorted list of all records of a farm.
     *
     * @throws IDNotValidException
     * ID is null or empty or no farm with that ID in the database.
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
     * Return average weight of all months in a year.
     *
     * @throws DateNotValidException
     * If year is smaller than 1 or no records exists in that year.
     */
    public int getMonthlyAverageOfAYear(int year) throws DateNotValidException {
	return 0;
    }

    /*
     * Return smallest weight of all months in a year.
     * List[0]=month, List[1]=weight;
     *
     * @throws DateNotValidException
     * If year is smaller than 1 or no records exists in that year.
     */
    public List<String> getMonthlyMinOfAYear(int year) throws DateNotValidException {
	return null;
    }

    /*
     * Return largest weight of all months in a year.
     * List[0]=month, List[1]=weight;
     *
     * @throws DateNotValidException
     * If year is smaller than 1 or no records exists in that year.
     */
    public List<String> getMonthlyMaxOfAYear(int year) throws DateNotValidException {
	return null;
    }

    /*
     * Return average weight of all records of a farm.
     *
     * @throws IDNotValidException
     * ID is null or empty or no farm with that ID exists in the database.
     */
    public int getAverageOfAFarm(String farmID) throws IDNotValidException {
	return 0;
    }

    /*
     * Return the record with smallest weight of all records of a farm.
     *
     * @throws IDNotValidException
     * ID is null or empty or no farm with that ID in the database.
     */
    public OneRecord getMinOfAFarm(String farmID) throws IDNotValidException {
	return null;
    }

    /*
     * Return the record with largest weight of all records of a farm.
     *
     * @throws IDNotValidException
     * ID is null or empty or no farm with that ID in the database.
     */
    public OneRecord getMaxOfAFarm(String farmID) throws IDNotValidException {
	return null;
    }

    /*
     * Return average weight of all records in a date range.
     *
     * @throws DateNotValidException
     * If either of the date is smaller than 10101 or endDate is smaller than
     * startDate or no record exists between two dates.
     */
    public int getAverageInDateRange(int srartDate, int endDate) throws IDNotValidException {
	return 0;
    }

    /*
     * Return the record with smallest weight of all records in a date range.
     *
     * @throws DateNotValidException
     * If either of the date is smaller than 10101 or endDate is smaller than
     * startDate or no record exists between two dates.
     */
    public OneRecord getMinInDateRange(int srartDate, int endDate) throws IDNotValidException {
	return null;
    }

    /*
     * Return the record with largest weight of all records in a date range.
     *
     * @throws DateNotValidException
     * If either of the date is smaller than 10101 or endDate is smaller than
     * startDate or no record exists between two dates.
     */
    public OneRecord getMaxInDateRange(int srartDate, int endDate) throws IDNotValidException {
	return null;
    }

    /*
     * Return the size of the database.
     */
    public int size() {
	return this.database.size();
    }

    /*
     * Clear the database.
     */
    public void clear() {
	this.database = new ArrayList<>();
    }
}
