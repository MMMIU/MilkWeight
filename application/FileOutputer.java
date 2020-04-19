/**
 *
 */
package application;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Yifei Miao
 *
 */
public class FileOutputer {

	private Database database;
	
    public FileOutputer(Database database) {
    	this.database=database;
    }
    
    public void annualReport(int year,PrintWriter outFile) {
    	List<String> nameList=this.database.getFarmIDList();
    	long totalWeight=0;
    	List<OneRecord> list=new ArrayList<>();
    	for(String e:nameList) {
    		OneRecord oneFarm=new OneRecord(e, 0, 0);
    		for(OneRecord r:this.database.getAllRecordsOfAFarm(e)) {
    			if(r.getDate()/10000==year) {
    				oneFarm.setWeight(oneFarm.getWeight()+r.getWeight());
    				totalWeight+=r.getWeight();
    			}
    		}
    		list.add(oneFarm);
    	}
    	outFile.println("Annual Report for year "+year+".");
    	outFile.println("Farm ID, Weight, Percent of total");
    	for(OneRecord record:list) {
    		outFile.println(record.getFarmID()+", "+record.getWeight()+", "+100.0*record.getWeight()/totalWeight);
    	}
    }
	
    public void farmReport(String farmID,int year,PrintWriter outFile) {
    	List<Integer> monthList=this.database.getMonthList();
    	List<Integer> tmpMonthList=new ArrayList<Integer>();
    	for(int e:monthList) {
    		if(e/100==year) {
    			tmpMonthList.add(e);
    		}
    	}
    	monthList=tmpMonthList;
    	long totalWeight[]=new long[12];
    	long farmWeight[]=new long[12];
    	for(int e:monthList) {
    		long farmTotal=0;
    		long monthTotal=0;
    		List<OneRecord> aMonth=this.database.getAllRecordsInAMonth(e);
    		for(OneRecord r:aMonth) {
    			if(r.getFarmID().equals(farmID)) {
    				farmTotal+=r.getWeight();
    			}
    			monthTotal+=r.getWeight();
    		}
    		farmWeight[e%100]=farmTotal;
    		totalWeight[e%100]=monthTotal;
    	}
    	outFile.println("Farm Report for "+farmID+" in year "+year+".");
    	outFile.println("Month, Weight, Percent of total in the month");
    	for(int i=0;i<12;i++) {
    		if(totalWeight[i]!=0) {
    			outFile.println((i+1)+", "+farmWeight[i]+", "+100.0*farmWeight[i]/totalWeight[i]);
    		}
    	}
    }

    public void monthlyReport(int yearMonth,PrintWriter outFile) {
    	List<OneRecord> aMonth=this.database.getAllRecordsInAMonth(yearMonth);
    	long totalWeight=0;
    	Map<String, Long> farmWeightMap=new HashMap<>();
    	for(OneRecord r:aMonth) {
    		totalWeight+=r.getWeight();
    		if(farmWeightMap.containsKey(r.getFarmID())) {
    			farmWeightMap.replace(r.getFarmID(), farmWeightMap.get(r.getFarmID())+r.getWeight());
    		}else {
    			farmWeightMap.put(r.getFarmID(),(long)r.getWeight());
    		}
    	}
    	List<String>nameList=new ArrayList<String>();
    	for(String e:farmWeightMap.keySet()) {
    		nameList.add(e);
    	}
    	Collections.sort(nameList);
    	outFile.println("Monthly report for "+yearMonth*1.0/100);
    	outFile.println("Farm ID, Weight, Percent of total");
    	for(String e:nameList) {
    		outFile.println(e+", "+farmWeightMap.get(e)+", "+farmWeightMap.get(e)*100.0/totalWeight);
    	}
    }
    
    public void dateRangeReport(int startDate,int endDate,PrintWriter outFile) {
    	List<OneRecord> recordList=this.database.getAllRecordsInDateRange(startDate, endDate);
    	long totalWeight=0;
    	Map<String, Long> farmWeightMap=new HashMap<>();
    	for(OneRecord r:recordList) {
    		totalWeight+=r.getWeight();
    		if(farmWeightMap.containsKey(r.getFarmID())) {
    			farmWeightMap.replace(r.getFarmID(), farmWeightMap.get(r.getFarmID())+r.getWeight());
    		}else {
    			farmWeightMap.put(r.getFarmID(),(long)r.getWeight());
    		}
    	}
    	List<String>nameList=new ArrayList<String>();
    	for(String e:farmWeightMap.keySet()) {
    		nameList.add(e);
    	}
    	Collections.sort(nameList);
    	outFile.println("Date range report for dates between "+startDate+" and "+endDate);
    	outFile.println("Farm ID, Weight, Percent of total");
    	for(String e:nameList) {
    		outFile.println(e+", "+farmWeightMap.get(e)+", "+farmWeightMap.get(e)*100.0/totalWeight);
    	}
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
}
