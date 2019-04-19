package ParalleClassification;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.validator.PublicClassValidator;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.ArrayList;
import java.util.List;


public class AtomicNode {
	private List<AtomicInteger> known = null;
	private List<AtomicInteger> possible = null;// -1 tested concept; -2 equivalent concept; -3 disjoint concept;
	//private List<Integer> impossible = null;
	private List<AtomicInteger> equivalent = null;
	private List<AtomicInteger> disjoint = null;
	private String value;
	private int index;
	private AtomicInteger flag;//record which group has been added. equal to -1 means waiting...
	private boolean unsat;
	private OWLClass classname;

	public AtomicNode(String value, int index, OWLClass class1) 
	{
		this.known = new ArrayList<AtomicInteger>();
				//new ArrayList<Integer>();
		this.possible = new ArrayList<AtomicInteger>();
		this.equivalent = new ArrayList<AtomicInteger>();
		this.disjoint = new ArrayList<AtomicInteger>();
		//this.impossible = new ArrayList<Integer>();
		this.value = value;
		this.index = index;
		this.flag = new AtomicInteger(-1);
		this.unsat = false;
		this.classname = class1;
	}

	public void setFlag(int groupIndex) 
	{
		flag.set(groupIndex);
	}
	
	public void setSatisfiability() 
	{
		unsat = true;
	}
	
	public void setOWLClassName(OWLClass class1)
	{
		classname = class1;
	}
	//public void setImpossible(List<Integer> list) {
		//impossible = list;
	//}

	public String getValue() 
	{
		return value;
	}

	public int getIndex() 
	{
		return index;
	}

	public int getFlag() 
	{
		return flag.intValue();
	}
	
	public OWLClass getOWLClassName()
	{
		return classname;
	}
	
	//public List<Integer> getImpossibleList() {
		//return impossible;
	//}
	
	public boolean getUnSatisfiability() 
	{
		return unsat;
	}

	//*******************************Possible List **********************	
	
	public void setPossible(List<AtomicInteger> list) 
	{
		possible = list;
	}
	
	public List<AtomicInteger> getPossibleList() 
	{
		return possible;
	}
	
	public void addPossible(int addNodeIndex) 
	{
		AtomicInteger value = new AtomicInteger(addNodeIndex);
		
		possible.add(value);
	} 
	
	public int getPossibleSize()
	{
		int size = 0;
		for(int i=0; i<possible.size(); i++)
		{
			if(checkPossible(i))
			{
				size ++;
			}
		}
		
		return size;
	}
	
	// Traverse all the nodes left in the possible list for each concept
	public List<AtomicInteger> traversePossibleList() 
	{
		List<AtomicInteger> leftPossibleList = new ArrayList<AtomicInteger>();
		//if (!isEmptyPossible()) 
		//{
		for (int i = 0; i < possible.size(); i++) 
		{  
			if(checkPossible(i))
			{
				leftPossibleList.add(possible.get(i)); 
			}
		}
		//}

		return leftPossibleList;
	}

	// Remove the nodes which has been tested the relationships with the concept
	public boolean removePossible(int removeNodeIndex) 
	{
		AtomicInteger value = new AtomicInteger(-1);
		//for (int i = 0; i < possible.size(); i++) 
		//{
		int pos = removeNodeIndex - index -1;
		//System.out.println(pos + "aaaaaaaa" + removeNodeIndex + "bbbbbbb" + index);
		if ( possible.get(pos).intValue() == removeNodeIndex ) 
		{
			//System.out.println("The current number of possible nodes is " + possible.size());
			possible.set(pos, value);
			//System.out.println(removeNodeIndex + "The node has been removed from possible.");
			return true;
		}
		//}
		return false;
	}
	
	public AtomicInteger removeAllPossible() 
	{
		AtomicInteger counter = new AtomicInteger(0);
		AtomicInteger value = new AtomicInteger(-1);
		for (int i = 0; i < possible.size(); i++) 
		{
			if (checkPossible(i)) 
			{
				possible.set(i, value);
				counter.set(counter.incrementAndGet());
			}
		}
		return counter;
	}
	
	
	// Test the possible list of each concept is empty or not
	public boolean isEmptyPossible() 
	{
		if(possible.size() == 0)
			return false;
		for(int i =0; i<possible.size(); i++)
		{
			if(checkPossible(i))
			{	return false;   }
		}
		return true;
	}
	
	
	public boolean checkPossible(int curIndex)
	{
		
		if(possible.get(curIndex).intValue() != -1 && 
				possible.get(curIndex).intValue() != -2 && possible.get(curIndex).intValue() != -3)
			return true;
		return false;
	}
	//*******************************Known List **********************
	public void setKnown(List<AtomicInteger> list) 
	{
		known = list;
	}
	
	public List<AtomicInteger> getKnownList() 
	{
		return known;
	}
	
	public boolean isEmptyKnown()
	{
		if(known.size() == 0)
			return true;
		return false;
	}
	
	// Add the node to Knownlist of the concept
	public void addKnown(int knownIndex) 
	{
		AtomicInteger value = new AtomicInteger(knownIndex);
       
		//if(findKnownList() != -2)
		//{
			//avoid duplicated elements in known
			for (int i = 0; i < known.size(); i++) 
			{
				if (known.get(i).intValue() == knownIndex) 
				{    return;    }
			}
			known.add(value);
		//}
		//else 
		//{
			//addKnown(getPosKnown());
		//}
	}
	
	/*public void mergeKnown(List<AtomicInteger> list)
	{
		int tag =0;
		for(int i=0; i<list.size(); i++)
		{
			tag = 0;
			for(int j=0; j<known.size();j++)
			{
				if(list.get(i) == known.get(j))//Before merging two lists, reduce the replicated concepts
				{
					tag = 1;
					break;
				}
			}
			
			if(tag == 0)
			{
				known.add(list.get(i));
				if(list.get(i).intValue() > index && checkPossible(list.get(i).intValue()))
				{
					possible.remove(list.get(i));
				}
			}
		}
	}*/
	
	public int findKnownList()
	{
		return known.get(0).intValue();
	}
	
	public int getPosKnown()
	{
		return known.get(1).intValue();
	}
	
	public void emptyKnown(int knownIndex)
	{
		known.clear();
		AtomicInteger value0 = new AtomicInteger(-2);
		AtomicInteger value1 = new AtomicInteger(knownIndex);
		known.add(0, value0);
		known.add(1, value1);
	}

	// Remove node from Knownlist of the concept
	public void removeKnown(int removeNodeIndex) 
	{
		for (int i = 0; i < known.size(); i++) 
		{
			if (known.get(i).intValue() == removeNodeIndex) 
			{
				known.remove(i);
				//System.out.println(removeNodeIndex + "The node has been removed from known.");
				break;
			}
		}
	}
	
	//*******************************Disjoint List **********************
	public void setDisjoint(List<AtomicInteger> list)
	{
		disjoint = list;
	}
	
	public List<AtomicInteger> getDisjointList()
	{
		return disjoint;
	}
	
	public void addDisjoint(int addNodeIndex)
	{
		AtomicInteger value = new AtomicInteger(addNodeIndex);
		
		disjoint.add(value);
		//disjoint.sort(null);
	}
	 public boolean isEmptyDisjoint()
	 {
		 if(disjoint.size() == 0)
		 {
			 //System.out.println("666666666666");
			 return true;
		 }
		 //System.out.println(disjoint.size());
		 return false;
	 }
	 
	 public void mergeDisjoint(List<AtomicInteger> list)
	 {
		 disjoint.addAll(list);
	 }
	 
	 public int findDisjointValue()
	 {
		 return disjoint.get(0).intValue();//equal to -3 or not
	 }
	 
	 public int getPosDisjoint()
	 {
		 return disjoint.get(1).intValue();
	 }
	
	 public void emptyDisjoint(int disIndex)
	 {
		 disjoint.clear();
		 AtomicInteger value0 = new AtomicInteger(-3);
		 AtomicInteger value1 = new AtomicInteger(disIndex);
		 disjoint.add(0, value0);
		 disjoint.add(1, value1);
	 }
	 
	//For the disjoint concepts, it will be marked -3 in the possible list of the smalller one
	public void removeDisjointConcept(int removeNodeIndex)
	{
		AtomicInteger value = new AtomicInteger(-3);
		
		int pos = removeNodeIndex - index - 1;
		possible.set(pos, value);
	}
	
	//*******************************Equivalent List **********************
	public void setEquivalent(List<AtomicInteger> list) 
	{
		equivalent = list;
	}
	
	public List<AtomicInteger> getEquivalentList()
	{
		return equivalent;
	}
	
	public void addEquivalent(int addNodeIndex)
	{
		AtomicInteger value = new AtomicInteger(addNodeIndex);
		equivalent.add(value);
		//Sort the current equivalent list here, if there is an index smaller than current concept,
		//then copy all the equivalent concepts to the smalllest one's equivalent list?
		/*int equivalentIndex = sortAtomicIntegerArray(equivalent);
		if(equivalentIndex != 0 && equivalentIndex < index)
		{
			//copy current equivalent list to the smallest concept
			
			//Empty current equivalent list
			
		}*/	
	}
	public void printEquivalent()
	{
		for(int i=0; i<equivalent.size(); i++)
			System.out.println(equivalent.size());
	}
	
	//Test the equivalent list is empty or not
	public boolean isEmptyEquivalent()
	{
		if(equivalent.size() == 0)
		{
			//System.out.println("11111111111111111");
			return true;
		}
			//System.out.println(equivalent.size());
	    return false;
	}
	
	//Merge Euqivalent List
	public void mergeEquivalent(List<AtomicInteger> list){
		equivalent.addAll(list);	
	}
	
	public int findEquivalentList(){
		//for(int i=0; i<equivalent.size(); i++)
			//System.out.println("2222222222");
		//System.out.println(equivalent.get(0));
		//if(equivalent.get(0) != null)
			return equivalent.get(0).intValue();
		//return 0;
	}
	
	public int getPosEquivalent()
	{
		return equivalent.get(1).intValue();
	}
	
	//Empty the equivalent list
	public void emptyEquivalent(int equiIndex){
		
		//if(equivalent.get(0).get() != -2)//current concept does not find equivalent concept yet
		//{
			equivalent.clear();
			//After empty the euivalent list of current concept,
			//make the value of position 0 is -2 to indicate the upper equivalent
			//save the upper index value of position 1
			AtomicInteger value = new AtomicInteger(-2);
			AtomicInteger value1 = new AtomicInteger(equiIndex);
			equivalent.add(0, value); //-2
			equivalent.add(1, value1); // uppper equivalent index concept
			//return -1;
		//}
		//return equivalent.get(1).get();
			
	}
	
    //For the equivalent concepts, it will be marked -2 in the possible list of the smaller one.
	public void removeEquivalentConcept(int removeNodeIndex)
	{
		AtomicInteger value = new AtomicInteger(-2);
		int pos = removeNodeIndex - index -1;
		possible.set(pos,value);
	}
	

	
	/*public void addImpossible(int impossibleIndex) {
		for (int i = 0; i < impossible.size(); i++) {
			if (impossible.get(i) == impossibleIndex)
				return;
		}
		impossible.add(impossibleIndex);
	}*/

}

