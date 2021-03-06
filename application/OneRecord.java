/*
 * OneRecord.java created by Yifei Miao in Milk Weight project.
 *
 * Author: Yifei Miao (ymiao29@wisc.edu) Date: 2020/04/21 Version : 1.0.0
 *
 * Course: COMPSCI 400 Lecture Number: 001 Semester: Spring 2020
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
public class OneRecord {

	private SimpleStringProperty farmID;
	private SimpleIntegerProperty date;
	private SimpleIntegerProperty weight;

	public OneRecord(String farmID, int date, int weight) {
		this.farmID = new SimpleStringProperty(farmID);
		this.date = new SimpleIntegerProperty(date);
		this.weight = new SimpleIntegerProperty(weight);
	}

	public void setFarmID(String FarmID) {
		this.farmID.set(FarmID);
	}

	public void setDate(int date) {
		this.date.set(date);
	}

	public void setWeight(int weight) {
		this.weight.set(weight);
	}

	public String getFarmID() {
		return this.farmID.get();
	}

	public int getDate() {
		return this.date.get();
	}

	public int getWeight() {
		return this.weight.get();
	}
}
