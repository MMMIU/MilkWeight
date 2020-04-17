/**
 *
 */
package application;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
    	outFile.println("Farm ID, Weight, Percent of total");
    	for(OneRecord record:list) {
    		outFile.println(record.getFarmID()+", "+record.getWeight()+", "+100.0*record.getWeight()/totalWeight);
    	}
    }

}
