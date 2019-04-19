package ParalleClassification;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Node {
	private List<Integer> known = null;
	private List<Integer> possible = null;
	//private List<Integer> impossible = null;
	private String value;
	private int index;
	private int flag;//record which group has been added. equal to -1 means waiting...
	private boolean unsat;
	private ReadWriteLock lock = new ReentrantReadWriteLock();

	public Node(String value, int index ) {
		this.known = new ArrayList<Integer>();
		this.possible = new ArrayList<Integer>();
		//this.impossible = new ArrayList<Integer>();
		this.value = value;
		this.index = index;
		this.flag = -1;
		this.unsat = false;
	}

	public void setFlag(int groupIndex) {
		flag = groupIndex;
	}
	
	public void setSatisfiability() {
		unsat = true;
	}

	public void setKnown(List<Integer> list) {
		known = list;
	}

	public void setPossible(List<Integer> list) {
		possible = list;
	}
	
	//public void setImpossible(List<Integer> list) {
		//impossible = list;
	//}

	public String getValue() {
		return value;
	}

	public int getIndex() {
		return index;
	}

	public int getFlag() {
		return flag;
	}

	public List<Integer> getKnownList() 
	{
		lock.readLock().lock();
		try{
			return known;
		}finally{
			lock.readLock().unlock();
		}
	}
	
	public List<Integer> getPossibleList() 
	{
		lock.readLock().lock();
		try{
			return possible;
		}finally{
			lock.readLock().unlock();
		}
	}
	
	//public List<Integer> getImpossibleList() {
		//return impossible;
	//}
	
	public boolean getUnSatisfiability() 
	{
		return unsat;
	}


	public void addPossible(Integer addNodeIndex) 
	{
		lock.writeLock().lock();
		
		try{
			possible.add(addNodeIndex);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	
	// Traverse all the nodes left in the possible list for each concept
	public List<Integer> traversePossibleList() {
		List<Integer> leftPossibleList = new ArrayList<Integer>();
		if (!isEmptyPossible()) 
		{
			lock.readLock().lock();
			try{
				for (int i = 0; i < possible.size(); i++) 
				{  leftPossibleList.add(possible.get(i)); }
			}finally{
				lock.readLock().unlock();
			}
		}

		return leftPossibleList;
	}

	// Remove the nodes which has been tested the relationships with the concept
	public boolean removePossible(int removeNodeIndex) {
		for (int i = 0; i < possible.size(); i++) {
			if (possible.get(i) == removeNodeIndex ) {
				lock.writeLock().lock();
				//System.out.println("The current number of possible nodes is " + possible.size());
				//possible.remove(i);
				try{
					possible.set(i, -1);
				}finally{
					lock.writeLock().unlock();
				}
				//System.out.println(removeNodeIndex + "The node has been removed from possible.");
				return true;
			}
		}
		return false;
	}
	
	public int removeAllPossible() {
		int counter = 0;
		for (int i = 0; i < possible.size(); i++) {
			if (possible.get(i) != -1) 
			{
				lock.writeLock().lock();
				try{
					possible.set(i, -1);
				}finally{
					lock.writeLock().unlock();
				}
				counter++;
			}
		}
		return counter;
	}
	

	// Test the possible list of each concept is empty or not
	public boolean isEmptyPossible() {
		if (possible == null)
			return true;
		return false;
	}

	// Add the node to Knownlist of the concept
	public void addKnown(Integer knownIndex) {
        //avoid duplicated elements in known
		for (int i = 0; i < known.size(); i++) 
		{
			if (known.get(i) == knownIndex) {
				return;
			}
		}
		lock.writeLock().lock();
		try{
			known.add(knownIndex);
		}finally{
			lock.writeLock().unlock();
		}
	}

	// Remove node from Knownlist of the concept
	public void removeKnown(int removeNodeIndex) 
	{
		for (int i = 0; i < known.size(); i++) 
		{
			if (known.get(i) == removeNodeIndex) 
			{
				lock.writeLock().lock();
				try{
					known.remove(i);
				//System.out.println(removeNodeIndex + "The node has been removed from known.");
				}finally{
					lock.writeLock().unlock();
				}
				break;
			}
		}
	}
	
	/*public void addImpossible(int impossibleIndex) {
		for (int i = 0; i < impossible.size(); i++) {
			if (impossible.get(i) == impossibleIndex)
				return;
		}
		impossible.add(impossibleIndex);
	}*/
}
