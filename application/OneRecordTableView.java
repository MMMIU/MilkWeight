/**
 *
 */
package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author Yifei Miao
 *
 *         date format: 20200414
 */
public class OneRecordTableView {

    private SimpleStringProperty farmID;
    private SimpleIntegerProperty date;
    private SimpleIntegerProperty weight;

    public OneRecordTableView(String farmID, int date, int weight) {
	this.farmID = new SimpleStringProperty(farmID);
	this.date = new SimpleIntegerProperty(date);
	this.weight = new SimpleIntegerProperty(weight);
    }

    public SimpleStringProperty getID() {
	return this.farmID;
    }

    public void setID(String newFarmID) {
	this.farmID.set(newFarmID);
	;
    }

    public SimpleIntegerProperty getDate() {
	return this.date;
    }

    public void setDate(int newDate) {
	this.date.set(newDate);
	;
    }

    public SimpleIntegerProperty getWeight() {
	return this.weight;
    }

    public void setWeight(int newWeight) {
	this.weight.set(newWeight);
	;
    }

}
