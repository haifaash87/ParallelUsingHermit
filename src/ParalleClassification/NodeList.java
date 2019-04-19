package ParalleClassification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NodeList {

	protected List<String> elements;
	protected List<Node> elementsNode;
	protected static int counter=0;//record the total number of possible nodes

	public NodeList(List<String> elements) {
		this.elements = elements;
		elementsNode = generateNodeList(elements);
		//generatePossibleList();
		generateHalfPossibleList();
	}

	public int getTotalnum() {
		return elementsNode.size();
	}
	
	public void setCounter()
	{
		counter++;
	}
	
	public int getCounter()
	{
		return counter;
	}

	// Transfer all the concepts into Node elements with index i
	public List<Node> generateNodeList(List<String> elements) {
		List<Node> elementsNode = new ArrayList<Node>();
		// List<Integer> possibleList = new ArrayList<>();

		for (int i = 0; i < elements.size(); i++) {
			Node node = new Node(elements.get(i), i);
			elementsNode.add(node);
		}
		return elementsNode;
	}

	
	public List<Node> getElementsNode() {
		return elementsNode;
	}
	
	
	public void resetFlag() {
		for (int i = 0; i < elementsNode.size(); i++) {
			elementsNode.get(i).setFlag(-1);
		}
	}
	
	// Initialize the possible list with all nodes
	/*public void generatePossibleList() {
		int index=-1;
		for (int i = 0; i < elementsNode.size(); i++) 
		{
			int j = 0;
			while (j < elementsNode.size()) {
				if (i != j) {
					index = elementsNode.get(j).getIndex();
					if(index == -1){
						System.out.println("The index equals to -1.");}
					elementsNode.get(i).addPossible(index);
				}
				j++;
			}
		}
	}*/

	
	//Generate the Half_Matrix
	public void generateHalfPossibleList() {
		int j = 1;
		for (int i = 0; i < elementsNode.size() - 1; i++) {
			for (j = i + 1; j < elementsNode.size(); j++) {
				elementsNode.get(i).addPossible(j);
			}
		}
	}
	
	
	public int getTotalNumofPossible() {
		int numPossibleNode = 0;
		for (int i = 0; i < elementsNode.size(); i++) {
			int num = elementsNode.get(i).getPossibleList().size();	
			numPossibleNode = numPossibleNode + num;
		}
		return numPossibleNode;
	}
	
	
	public void printPossibleNodes() {
		System.out.println("printPossibleNodes");
		for (int i = 0; i < elementsNode.size(); i++) {
			System.out.println("i: " + i + " possibleList: ");
			Node node = elementsNode.get(i);
			List<Integer> possibleList = node.getPossibleList();
			for (int possible : possibleList) {
				System.out.println(possible + ", ");
			}
		}
	}
	
	
	public void printKnownNodes() {
		System.out.println("printKnownNodes");
		for (int i = 0; i < elementsNode.size(); i++) {
			System.out.println("i: " + i + " knownList: ");
			Node node = elementsNode.get(i);
			List<Integer> knownList = node.getKnownList();
			Collections.sort(knownList);
			for (int known : knownList) {
				System.out.println(known + ", ");
			}
		}
	}
	
	
	/*public void printImpossibleNodes() {
		System.out.println("printImpossibleNodes");
		for (int i = 0; i < elementsNode.size(); i++) {
			System.out.println("i: " + i + " impossibleList: ");
			Node node = elementsNode.get(i);
			List<Integer> impossibleList = node.getImpossibleList();
			Collections.sort(impossibleList);
			for (int impossible : impossibleList) {
				System.out.println(impossible + ", ");
			}
		}
	}*/
	
	
	// Return -1 which means the node does not exist.
	public int getIndex(String value) {
		for (int i = 0; i < elementsNode.size(); i++) {
			//String nodeValue = elementsNode.get(i).getValue();
			// System.out.println( nodeValue + "+" +value);
			if (value.equals(elementsNode.get(i).getValue())) {
				return i;
			}
		}
		return -1;
	}

	
	// Get the String value for each divided group, in order to find OWLClass
	public List<String> getValue(ArrayList<Integer> index) {
		List<String> groupElements = new ArrayList<String>();

		for (int i = 0; i < index.size(); i++) {
			groupElements.add(elementsNode.get(index.get(i)).getValue());
		}
		return groupElements;
	}

	public String getValue(int index) {
		return elementsNode.get(index).getValue();
	}
	
	public List<Node> generateDisorderNodelist(int[] order) {
		List<Node> disorderNodelist = new ArrayList<Node>();
		String value = null;
		int index = -1;

		for (int i = 0; i < order.length; i++) {
			index = order[i];
			value = elementsNode.get(index).getValue();
			Node node = new Node(value, index);
			disorderNodelist.add(node);
		}
		return disorderNodelist;
	}

	//Find the relationships between two concepts
	public void addInfoList(String parent, String child) {
		int indexParent = getIndex(parent);
		int indexChildren = getIndex(child);

		elementsNode.get(indexParent).addKnown(indexChildren);
		if (indexParent < indexChildren && elementsNode.get(indexParent).removePossible(indexChildren)) {
				setCounter();
		} else {
			if(elementsNode.get(indexChildren).removePossible(indexParent))
				setCounter();
		}
	}
	
	public void addInfoList(int indexParent, int indexChild) {
		elementsNode.get(indexParent).addKnown(indexChild);
		if (indexParent < indexChild && elementsNode.get(indexParent).removePossible(indexChild)) {
				setCounter();
		} else {
			if(elementsNode.get(indexChild).removePossible(indexParent))
				setCounter();
		}
	}
	
	
	//Find no relationships between two concepts
	public void deleteInfoList(String parent, String child) {
		int indexParent = getIndex(parent);
		int indexChild = getIndex(child);

		//elementsNode.get(indexChild).addImpossible(indexParent);
		//deleteImpossibleFromKnown(indexChild, indexParent);
		if (indexParent < indexChild && elementsNode.get(indexParent).removePossible(indexChild)) {
			setCounter();
		} else {
			if(elementsNode.get(indexChild).removePossible(indexParent))
				setCounter();
		}
	}
	
	
	//public void deleteUnsatInfoList()
	
	//Empty possible list of unsatisfiable concept
	public void emptyPossibleList(String concept) {
		int index = getIndex(concept);
		int num =0;

		elementsNode.get(index).setSatisfiability();
		num = elementsNode.get(index).removeAllPossible();
		counter = counter + num;
	}

	//Empty possible list of equivalent concept with big index
	public void removeEquivalentConceptPossibleList(String concept1, String concept2) {
		int index1 = getIndex(concept1);
		int index2 = getIndex(concept2);
        int num =0;
		
		if (index1 < index2) {
			num = elementsNode.get(index2).removeAllPossible();
			counter = counter + num;
		} else {
			num = elementsNode.get(index1).removeAllPossible();
			counter = counter + num;
		}
	}
	
	//Delete equivalent concepts from possible lists of smaller index concepts
	public void deleteEquivalentfromPossibleList(String concept) {
		int index = getIndex(concept);	
		for (int i = 0; i < index; i++) {
			if(elementsNode.get(i).removePossible(index))
				setCounter();
		}
	}
	
	public List<Integer> convertIndextoString(String parent, String child) {
		int parentIndex = getIndex(parent);
		int childIndex = getIndex(child);
	
		List<Integer> groupTwo = new ArrayList<Integer>();
		groupTwo.add(parentIndex);
		groupTwo.add(childIndex);
		
		return groupTwo;
	}
	
	//????parameters change to string
	//???? use subsumption test here!!
	public void compareKnownList(int parentIndex, int childIndex) {
		List<Integer> knownParent = elementsNode.get(parentIndex).getKnownList();
		int counter =0;

		if (knownParent.size() == 0) {
			addInfoList(parentIndex, childIndex);
		} else {
			for (int i = 0; i < knownParent.size(); i++) {
				if (elementsNode.get(knownParent.get(i)).getPossibleList().get(childIndex - knownParent.get(i) - 1) == -1) 
				{// the concept in parent.known has compared with current concept
					if (checkExistingKnown(elementsNode.get(knownParent.get(i)).getKnownList(), childIndex)) 
					{// the current concept exist in its knownlist, it does not need to add to parent.known
						return;
					}
					else
						counter++;
				} else 
				{// current concept needs to compare with the concept in known
					if (counter ==0) {
						compareKnownList(knownParent.get(i), childIndex);
						elementsNode.get(knownParent.get(i)).removePossible(childIndex);
						// elementsNode.get(knownParent.get(i)).addKnown(childIndex);
					} 
					else{
						counter++;
						continue;
					}
				}
			}
		}
		if(counter == knownParent.size())
		{
			addInfoList(parentIndex, childIndex);
		}
	}
	
	/*public void deleteImpossibleFromKnown(int index, int impossibleIndex) {
		List<Integer> knownlist = elementsNode.get(index).getKnownList();
		for (int j = 0; j < knownlist.size(); j++) {
			int knownindex = knownlist.get(j);
			List<Integer> possiblelist = elementsNode.get(knownindex).getPossibleList();
			for (int i = 0; i < possiblelist.size(); i++) {
				if (possiblelist.get(i) == impossibleIndex && elementsNode.get(index).removePossible(impossibleIndex)) {
					setCounter();
					return;
				}
			}
		}
	}*/
	
	
	public boolean checkExistingPossible(String parent, String child) {
		boolean exist = true;
		int indexParent = getIndex(parent);
		int indexChild = getIndex(child);
		if (indexParent < indexChild
				&& elementsNode.get(indexParent).getPossibleList().get(indexChild - indexParent - 1) == -1)
			exist = false;
		else if (indexParent > indexChild
				&& elementsNode.get(indexChild).getPossibleList().get(indexParent - indexChild - 1) == -1)
			exist = false;
		return exist;
	}
	
	public boolean checkExistingKnown(List<Integer> knownParent, int childIndex)
	{
		boolean exist = false;
		
		for(int i=0; i<knownParent.size(); i++)
		{
			if(knownParent.get(i) == childIndex)
				return true;
		}
		return exist;
	}
	
	//One concept is subsumed by the other
	public void addOneSubsumInfoList(String subsumer, String subsumee) {
		int indexParent = getIndex(subsumer);
		int indexChild = getIndex(subsumee);

		List<Integer> Bknown = elementsNode.get(indexChild).getKnownList();
		List<Integer> Apossible = elementsNode.get(indexParent).getPossibleList();
		List<Integer> Aknown = elementsNode.get(indexParent).getKnownList();

		for (int i = 0; i < Apossible.size(); i++) {
			for (int j = 0; j < Bknown.size(); j++) {
				// delete B.known from A.possible add to A.impossible
				if (Bknown.get(j) == Apossible.get(i) /* && possible.get(i) != -1 */) {
					if (elementsNode.get(indexParent).removePossible(Bknown.get(j)))
						setCounter();
					// elementsNode.get(indexParent).addImpossible(Bknown.get(j));
				}
				// all nodes of B.known delete A from possible to impossible
				if (elementsNode.get(Bknown.get(j)).removePossible(indexParent))
					setCounter();
				// elementsNode.get(Bknown.get(j)).addImpossible(indexParent);

				// all A.known delete from B.known
				for (int k = 0; k < Aknown.size(); k++) {
					if (Bknown.get(j) == Aknown.get(k)) {
						elementsNode.get(indexChild).removeKnown(Aknown.get(k));
						break;
					}
				}
			}
		}
	}
	
}
