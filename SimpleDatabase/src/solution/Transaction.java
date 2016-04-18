package solution;

import java.util.HashMap;

public class Transaction {	
	private Transaction prev;	
	
	//pairMap stores the variable name and its corresponding value
	private HashMap<String, Integer> pairMap = new HashMap<String, Integer>();
	
	//countMap stores the variable' value and total times
	private HashMap<Integer, Integer> countMap = new HashMap<Integer, Integer>(); 
 
	public Transaction(){}
 
	public void setPrev(Transaction block) {
		//pointer to the previous transaction
		prev = block;
	}
 
	public Transaction(HashMap<String, Integer>nameValue, HashMap<Integer, Integer>valueCounter){
		pairMap = nameValue;
		countMap = valueCounter;
	}
 
	public HashMap<String, Integer> getNameValue(){
		return pairMap;
	}
 
	
	/**
	 *  Set the variable name
	 *  maintain the counter
	 */
	public void set(String name, Integer currentValue){
 
		// decrease counter of previous 'name' value
		Integer prevValue = get(name);
		if (prevValue != null){
			Integer prevValueCounter = numEqualTo(prevValue);
			countMap.put(prevValue, --prevValueCounter);
		}
 
		// increase counter of current 'name' value
		Integer currentValueCounter = numEqualTo(currentValue);
		if (currentValue != null) {
			if (currentValueCounter != null) {
				countMap.put(currentValue, ++currentValueCounter);
			} else {
				countMap.put(currentValue, new Integer(1));
			}
		}
 
		pairMap.put(name, currentValue);
	}
 
	/**
	 *  Get the value
	 */
	public Integer get(String name) {
		Transaction block = this;
		Integer value = block.pairMap.get(name);
		//If current transaction has no operation on (name), check the previous one
		while(!block.pairMap.containsKey(name) && block.prev != null){
			block = block.prev;
			value = block.pairMap.get(name);
		}
		return value;
	}
 
	/**
	 * 	Number of variables that are currently set to value
	 * 	0 if no variables are set
	 */
	public Integer numEqualTo(Integer value){
		if (value == null) return 0;
 
		Transaction block = this;
		Integer counter = block.countMap.get(value);
		while(counter == null && block.prev != null){
			block = block.prev;
			counter = block.countMap.get(value);
		}
 
		if (counter == null)
			return 0;
		else{
			return counter;	
		}
	}
}
