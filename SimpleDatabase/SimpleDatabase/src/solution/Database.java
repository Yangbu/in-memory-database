package solution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/*
 * Complexity:O(log N)
 * 
 * LinkedList :
 * Simulate the order of the incremental state changes over each transaction scope. 
 * When a new transaction scope starts, a block is created and added to the end of the linked list. 
 * 
 * Two kinds of HashMap:
 * pairMap saves the variable name and the corresponding value;  
 * countMap saves the variable name and appearing times .
 * 
 */
public class Database {
	private LinkedList<Transaction> blocks;

	// Initialization
	public Database(){
		blocks = new LinkedList<Transaction>();
		blocks.add(new Transaction());
	} 
	
	/**
	 *  Set the variable name
	 */
	public void set(String name, Integer value){
		blocks.getLast().set(name, value);
	}

	
	/**
	 *  Get the value of the variable name
	 * 	NULL if that variable is not set.
	 */
	public Integer get(String name){
		return blocks.getLast().get(name);
	}

	/**
	 * 	Number of variables that are currently set to value
	 * 	0 if no variables are set
	 */
	public Integer numEqualTo(Integer value){
		return blocks.getLast().numEqualTo(value);
	}

	/**
	 *  Open a new transaction block
	 */
	public void begin(){
		Transaction block = new Transaction();
		block.setPrev(blocks.getLast());
		blocks.add(block);
	}

	/**
	 *  Close all open transaction blocks, permanently applying the changes made in them
	 */
	public boolean commit() {
		if (blocks.size() <= 1) return false;

		HashMap<String, Integer> pairMap = new HashMap<String, Integer>();
		HashMap<Integer, Integer> countMap = new HashMap<Integer, Integer>();

		ListIterator<Transaction> iterator = blocks.listIterator();
		while (iterator.hasNext()) {
			Transaction block = iterator.next();
			pairMap.putAll((Map<? extends String, ? extends Integer>) block.getNameValue());
		}

		for (Entry<String, Integer> entry : pairMap.entrySet()) {
			Integer value = entry.getValue();
			if(countMap.get(value) == null){
				countMap.put(value, new Integer(1));
			}
			else{
				countMap.put(value, new Integer(countMap.get(value) + 1));
			}
			pairMap.put(entry.getKey(),entry.getValue());
		}		

		blocks = new LinkedList<Transaction>();
		blocks.add(new Transaction(pairMap, countMap));

		return true;
	}

	/**
	 * Undo the recent transaction
	 */
	public boolean rollBack(){
		if (blocks.size() <= 1) return false;
		blocks.removeLast();
		return true;
	}

	public static void main(String[] args) {
		Database db = new Database();
		Scanner sc = new Scanner(System.in);
		
		sc.useDelimiter("\\s+"); 
		String command; 
		while (sc.hasNextLine()) {
			command = sc.nextLine();
			String[] tokens = command.split("\\s+");
			String cmd = tokens[0];
			String name;
			Integer value;
			try {
				switch (cmd) {
				case "GET":
					name = tokens[1];
					System.out.println(db.get(name) != null ? db.get(name):"NULL");
					break;
				case "SET":
					name = tokens[1];
					value = Integer.parseInt(tokens[2]);
					db.set(name, value);
					break;
				case "UNSET":
					name = tokens[1];
					db.set(name, null);
					break;
				case "NUMEQUALTO":
					value = Integer.parseInt(tokens[1]);
					System.out.println(db.numEqualTo(value));
					break;
				case "BEGIN":
					db.begin();
					break;
				case "ROLLBACK":
					if (!db.rollBack()) System.out.println("NO TRANSACTION");
					break;
				case "COMMIT":
					if (!db.commit()) System.out.println("NO TRANSACTION");
					break;					
				case "END":
					return;
				case "":
					break;
				default:
					System.out.println("Invalid Command: " + cmd );
				}
			} catch (NumberFormatException e) {			
				System.out.println("Number Format Exception : " + command );
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Missing Operand: " + command );
			}
		}
		sc.close();
	}
}
