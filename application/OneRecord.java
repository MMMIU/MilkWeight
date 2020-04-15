/**
 *
 */
package application;

/**
 * @author Yifei Miao
 *
 *         date format: 20200414
 */
public class OneRecord {

    private String farmID;
    private int date;
    private int weight;

    public OneRecord(String farmID, int date, int weight) {
        this.farmID = farmID;
        this.date = date;
        this.weight = weight;
    }

    public void setID(String newFarmID) {
        this.farmID = newFarmID;
    }

    public void setDate(int newDate) {
        this.date = newDate;
    }

    public void setWeight(int newWeight) {
        this.weight = newWeight;
    }

    public String getID() {
        return this.farmID;
    }

    public int getDate() {
        return this.date;
    }

    public int getWeight() {
        return this.weight;
    }
}
