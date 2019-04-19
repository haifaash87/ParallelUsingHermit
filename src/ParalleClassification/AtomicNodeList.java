package ParalleClassification;

import java.time.chrono.MinguoChronology;
//import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

//import org.semanticweb.HermiT.Reasoner;
//import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import com.clarkparsia.owlapi.modularity.locality.SemanticLocalityEvaluator;
import com.sun.org.apache.bcel.internal.generic.ReturnaddressType;



public class AtomicNodeList {

	protected List<String> elements;
	protected List<AtomicNode> elementsNode;
	//protected static int counter = 0;//record the total number of possible nodes
	protected AtomicInteger counter;//record the total number of possible nodes
	private static int counterNumofTests = 0;
	protected List<OWLClass> classElements;
	
	//private OWLOntologyManager manager = null;
	//private OWLOntology ontology = null;
	//private OWLReasoner reasoner = null;

	public AtomicNodeList(List<String> elements, List<OWLClass> CElements) 
	{
		this.elements = elements;
		elementsNode = generateNodeList(CElements);
		//generatePossibleList();
		generateHalfPossibleList();
		counter = new AtomicInteger(0);
		this.classElements = CElements;
	}
	
	/*public void startReasoner(String inputFile) throws OWLOntologyCreationException
	{
		File file = new File(inputFile);
		IRI docIRI = IRI.create(file);
		manager = OWLManager.createOWLOntologyManager();
		ontology = manager.loadOntologyFromOntologyDocument(docIRI);
		System.out.println("Loaded: " + ontology.getOntologyID());
		reasoner = new Reasoner( ontology );
	
		
		for (OWLClass cls : ontology.getClassesInSignature()) 
		{
			classElements.add(cls.asOWLClass());
			//System.out.println(clname(cls));
			elements.add(cls.asOWLClass().toString());
		}
		generateNodeList();
		generateHalfPossibleList();
		
		//return OWLelements;
	}*/
	
	public List<String> getElements()
	{
		return elements;
	}
	
	public int getElementsNumber()
	{
		return elements.size();
	}
	
	public List<AtomicNode> getElementsNode() 
	{
		return elementsNode;
	}
	
	public List<OWLClass> getClassElements()
	{
		return classElements;
	}
	
	public int getTotalnum() {
		return elementsNode.size();
	}
	
	public void setCounter()
	{
		counter.set(counter.incrementAndGet());
	}
	
	public int getCounter()
	{
		return counter.intValue();
	}

	public int getCounterNumofTests()
	{
		return ++counterNumofTests;
	}
	
	// Transfer all the concepts into Node elements with index i
	public List<AtomicNode> generateNodeList(List<OWLClass> classElements) 
	{
		List<AtomicNode> elementsNode = new ArrayList<AtomicNode>();
		// List<Integer> possibleList = new ArrayList<>();
		AtomicNode node = null;
		for (int i = 0; i < elements.size(); i++) 
		{
			node = new AtomicNode(elements.get(i), i, classElements.get(i));
			elementsNode.add(node);
			//System.out.println(node.getIndex() + node.getValue());
		}
		return elementsNode;
	}
	
	
	public void resetFlag() 
	{
		for (int i = 0; i < elementsNode.size(); i++) 
		{
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
	public void generateHalfPossibleList() 
	{
		int j = 1;
		for (int i = 0; i < elementsNode.size() - 1; i++) 
		{
			for (j = i + 1; j < elementsNode.size(); j++) 
			{
				elementsNode.get(i).addPossible(j);
			}
		}
	}
	
	
	public int getTotalNumofPossible() 
	{
		AtomicInteger numPossibleNode = new AtomicInteger(0);
		int numPossible = 0;
		for (int i = 0; i < elementsNode.size(); i++) 
		{
			//int num = elementsNode.get(i).getPossibleList().size();	
			int num = elementsNode.get(i).traversePossibleList().size();
			numPossible = numPossibleNode.addAndGet(num);
		}
		return numPossible;
	}
	
	
	public void printPossibleNodes() 
	{
		System.out.println("printPossibleNodes");
		for (int i = 0; i < elementsNode.size(); i++) 
		{
			System.out.println("i: " + i + " possibleList: ");
			AtomicNode node = elementsNode.get(i);
			List<AtomicInteger> possibleList = node.getPossibleList();
			System.out.println("The size of current possible list is " + possibleList.size());
			for (AtomicInteger possible : possibleList) 
			{
				System.out.println(possible + ", ");
			}
		}
	}
	
	
	public void printKnownNodes() {
		System.out.println("printKnownNodes");
		for (int i = 0; i < elementsNode.size(); i++) {
			System.out.println("i: " + i + " knownList: ");
			AtomicNode node = elementsNode.get(i);
			List<AtomicInteger> knownList = node.getKnownList();
			//Collections.sort(knownList);
			for (AtomicInteger known : knownList) {
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
	public int getIndex(String value) 
	{
		for (int i = 0; i < elementsNode.size(); i++) 
		{
			//String nodeValue = elementsNode.get(i).getValue();
			// System.out.println( nodeValue + "+" +value);
			if (value.equals(elementsNode.get(i).getValue())) {
				return i;
			}
		}
		return -1;
	}

	
	// Get the String value for each divided group, in order to find OWLClass
	public List<String> getValue(ArrayList<Integer> index) 
	{
		List<String> groupElements = new ArrayList<String>();

		for (int i = 0; i < index.size(); i++) {
			groupElements.add(elementsNode.get(index.get(i)).getValue());
		}
		return groupElements;
	}

	public String getValue(int index) {
		return elementsNode.get(index).getValue();
	}
	
	
	public List<AtomicNode> generateDisorderNodelist(int[] order) 
	{
		List<AtomicNode> disorderNodelist = new ArrayList<AtomicNode>();
		String value = null;
		int index = -1;
		OWLClass class1 = null;

		for (int i = 0; i < order.length; i++) {
			index = order[i];
			value = elementsNode.get(index).getValue();
			class1 = elementsNode.get(index).getOWLClassName();
			AtomicNode node = new AtomicNode(value, index, class1);
			disorderNodelist.add(node);
		}
		return disorderNodelist;
	}

	
	//Find the relationships between two concepts
	public void addInfoList(String parent, String child) 
	{
		int indexParent = getIndex(parent);
		int indexChildren = getIndex(child);
		
		int knownIndex = indexParent;
		while (!elementsNode.get(knownIndex).isEmptyKnown() && elementsNode.get(knownIndex).findKnownList() == -2)
				knownIndex = elementsNode.get(knownIndex).getPosKnown();
		elementsNode.get(knownIndex).addKnown(indexChildren);
		if (knownIndex < indexChildren && elementsNode.get(knownIndex).removePossible(indexChildren)) 
		{
				setCounter();
		} 
		else if(knownIndex > indexChildren && elementsNode.get(indexChildren).removePossible(knownIndex))
				setCounter();
		
	}
	
	public void addInfoList(int indexParent, int indexChild) {
		int knownIndex = indexParent;
		while (!elementsNode.get(knownIndex).isEmptyKnown() && elementsNode.get(knownIndex).findKnownList() == -2)
				knownIndex = elementsNode.get(knownIndex).getPosKnown();
		elementsNode.get(knownIndex).addKnown(indexChild);
		
		//elementsNode.get(indexParent).addKnown(indexChild);
		if (knownIndex < indexChild && elementsNode.get(knownIndex).removePossible(indexChild)) {
				setCounter();
		} 
		else if(knownIndex > indexChild && elementsNode.get(indexChild).removePossible(knownIndex))
			setCounter();
	}
	
	
	//Find no relationships between two concepts
	public void deleteInfoList(String parent, String child) {
		int indexParent = getIndex(parent);
		int indexChild = getIndex(child);

		//System.out.println("indexChild" + parent + indexChild + "indexParent" + child + indexParent);
		//elementsNode.get(indexChild).addImpossible(indexParent);
		//deleteImpossibleFromKnown(indexChild, indexParent);
		if (indexParent < indexChild )/*&& elementsNode.get(indexParent).getPossible(indexChild, indexParent).intValue()!=-1)*/
		{	
			if(elementsNode.get(indexParent).removePossible(indexChild))
				setCounter();
		} else {
			if(elementsNode.get(indexChild).removePossible(indexParent))/*;elementsNode.get(indexChild).getPossible(indexParent, indexChild).intValue()!= -1)*/
			{
				setCounter();
			}
		}
	}
	
	
	//public void deleteUnsatInfoList()
	
	//Empty possible list of unsatisfiable concept
	public void emptyPossibleList(String concept) {
		int index = getIndex(concept);
		AtomicInteger num = new AtomicInteger(0);

		elementsNode.get(index).setSatisfiability();
		num = elementsNode.get(index).removeAllPossible();
		counter.set(counter.addAndGet(num.intValue()));
		
	}
	
	public int getThreadNum(int num, int totalnum)
	{
		int numThread = num;
		//int totalnum = elements.size();
		   
		while(totalnum/numThread < 5)
		{     
			numThread --;  
		}
		System.out.println("There are " + numThread + " threads used.");
		   
		return numThread;
	}   
	   
	public int getThreadNumforAxioms(int num, ArrayList<OWLClassAxiom> axiom)
	{
		int numThread = num;
		int totalnum = axiom.size();
		
		while(totalnum/numThread < 10)
			numThread --;
		//System.out.println("There are " + numThread + " axiom threads used.");

		return numThread;
	}
	
//***************************Equivalent Concepts *******************************
	//Empty possible list of equivalent concept with bigger index
	public void removeEquivalentConceptPossibleList(String concept1, String concept2) {
		int index1 = getIndex(concept1);
		int index2 = getIndex(concept2);
        AtomicInteger num = new AtomicInteger(0);
		
		if (index1 < index2) {
			num = elementsNode.get(index2).removeAllPossible();
			counter.set(counter.addAndGet(num.intValue()));
			deleteEquivalentfromPossibleList(index1, index2);
		} else if(index1 > index2){
			num = elementsNode.get(index1).removeAllPossible();
			counter.set(counter.addAndGet(num.intValue()));
			deleteEquivalentfromPossibleList(index2, index1);
		}
	}
	
	//Delete equivalent concepts from possible lists of smaller index concepts
	public void deleteEquivalentfromPossibleList(int indexSmaller, int indexBigger) {
		//int index = getIndex(concept);	
		
		/*for (int i = 0; i < index; i++) 
		{
			if(elementsNode.get(i).removePossible(index))
			{
				elementsNode.get(i).removeEquivalentConcept(index);
				setCounter();
			}
		}*/
		//delete the concept with bigger index from smaller one's possible and mark position with -2
		//if(elementsNode.get(indexSmaller).removePossible(indexBigger))
		
		elementsNode.get(indexSmaller).removeEquivalentConcept(indexBigger);
		setCounter();
		//if the bigger index concept's equivalent list is not empty
		//merge the equivalent list the the smaller concept' and empty itself's equivalent
		//AtomicInteger value = new AtomicInteger(-2);
		int value = -2;
		int bigger = indexBigger, smaller = indexSmaller;
		//To find the smallest index of the bigger index concept
			
		//if(!elementsNode.get(indexBigger).isEmptyEquivalent())
		//{
			   //System.out.println("222222222");
		while(!elementsNode.get(bigger).isEmptyEquivalent() && elementsNode.get(bigger).findEquivalentList() == value )
		{
			  //System.out.println("33333333333");
			 bigger = elementsNode.get(bigger).getPosEquivalent();
			  //if(elementsNode.get(bigger).isEmptyEquivalent())
					//break;
		}
		//}
		//To find the smallest index of the smaller index concept
		//if(!elementsNode.get(indexSmaller).isEmptyEquivalent())
		//{
		while (!elementsNode.get(smaller).isEmptyEquivalent() && elementsNode.get(smaller).findEquivalentList() == value)
		{
			smaller = elementsNode.get(smaller).getPosEquivalent();
			//if(elementsNode.get(smaller).isEmptyEquivalent())
					//break;
		}
		//}	
		int knownIndex;
		List<AtomicInteger> knownlist = new ArrayList<AtomicInteger>();
		if(smaller < bigger)
		{
			knownIndex = bigger;
			elementsNode.get(smaller).addEquivalent(bigger);
			elementsNode.get(smaller).mergeEquivalent(elementsNode.get(bigger).getEquivalentList());
			//find the known list and merge the known list for the equivalent concepts
			while(!elementsNode.get(knownIndex).isEmptyKnown() && elementsNode.get(knownIndex).findKnownList() == -2)
				knownIndex = elementsNode.get(knownIndex).getPosKnown();
			knownlist = elementsNode.get(knownIndex).getKnownList();
			for(int k=0; k<knownlist.size(); k++)
			{ 
				addInfoList(smaller, knownlist.get(k).intValue());
			}
			//elementsNode.get(smaller).mergeKnown(elementsNode.get(knownIndex).getKnownList());
			elementsNode.get(bigger).emptyEquivalent(smaller);
		}
		else if(smaller > bigger)
		{
			knownIndex = smaller;
			elementsNode.get(bigger).addEquivalent(smaller);
			elementsNode.get(bigger).mergeEquivalent(elementsNode.get(smaller).getEquivalentList());
			//find the known list and merge the known list for the equivalent concepts
			while(!elementsNode.get(knownIndex).isEmptyKnown() && elementsNode.get(knownIndex).findKnownList() == -2)
				knownIndex = elementsNode.get(knownIndex).getPosKnown();
			knownlist = elementsNode.get(knownIndex).getKnownList();
			for(int k=0; k<knownlist.size(); k++)
			{ 
				addInfoList(smaller, knownlist.get(k).intValue());//add to current concept's known and 
				//remove from the smaller index concept's possible
			}
			//elementsNode.get(bigger).mergeKnown(elementsNode.get(knownIndex).getKnownList());
			elementsNode.get(smaller).emptyEquivalent(bigger);
		}	
		//}
		//else {//the euivalent is empty
			//elementsNode.get(indexSmaller).addEquivalent(indexBigger);
		//}
		
		/*delete the known list of bigger index concept from smaller one's possible
		for(int i=0; i<elementsNode.get(bigger).getKnownList().size(); i++)
		{
			int index = elementsNode.get(bigger).getKnownList().get(i).get();
			elementsNode.get(smaller).addKnown(index); //add to smaller concept's known list
			if(smaller < index)
				elementsNode.get(smaller).removePossible(index);//delete from concept's possible list
			else if(smaller > index)
				elementsNode.get(index).removePossible(smaller);//delete from concept's possible list
			setCounter();
		}*/
	}
	
	
	public void deleteInfoDisjoint(int index1, int index2)
	{
		//find the final known list for both concepts
		while (!elementsNode.get(index1).isEmptyKnown() && elementsNode.get(index1).findKnownList() == -2)
			index1 = elementsNode.get(index1).getPosKnown();
		while (!elementsNode.get(index2).isEmptyKnown() && elementsNode.get(index2).findKnownList() == -2)
			index2 = elementsNode.get(index2).getPosKnown();
		//delete from each other's possible list
		List<AtomicInteger> list1 = elementsNode.get(index1).getKnownList();
		List<AtomicInteger> list2 = elementsNode.get(index2).getKnownList();
		for(int i=0; i< list1.size(); i++)
		{
			if(index2 < list1.get(i).intValue())
				elementsNode.get(index2).removePossible(list1.get(i).intValue());
			else if (index2 > list1.get(i).intValue())
				elementsNode.get(list1.get(i).intValue()).removePossible(index2);
		}
		
		for(int j=0; j<list2.size(); j++)
		{
			if(index1 < list2.get(j).intValue())
				elementsNode.get(index1).removePossible(list2.get(j).intValue());
			else if(index1 > list2.get(j).intValue())
				elementsNode.get(list2.get(j).intValue()).removePossible(index1);
		}
	}
	
	//two concepts are disjoint, which means there are no conjuctions between A and B
		public void deleteDisjointClassInfoList(String concept1, String concept2)
		{
			int index1 = getIndex(concept1);
			int index2 = getIndex(concept2);
			
			if(index1>index2){
				elementsNode.get(index2).removeDisjointConcept(index1);
				setCounter();
			}
			else{
				elementsNode.get(index1).removeDisjointConcept(index2);
				setCounter();
			}
			int value = -3;
			//System.out.println("7777777777777777777");
			
			while(!elementsNode.get(index1).isEmptyDisjoint() && elementsNode.get(index1).findDisjointValue() == value)
			{
				index1 = elementsNode.get(index1).getPosDisjoint();
			}
			
			while(!elementsNode.get(index2).isEmptyDisjoint() && elementsNode.get(index2).findDisjointValue() == value)
			{
				index2 = elementsNode.get(index2).getPosDisjoint();
			}
			if(index1 < index2)
			{
				elementsNode.get(index1).addDisjoint(index2);
				elementsNode.get(index1).mergeDisjoint(elementsNode.get(index2).getDisjointList());
				elementsNode.get(index2).emptyDisjoint(index1);
			}else if(index1 > index2)
			{
				elementsNode.get(index2).addDisjoint(index1);
				elementsNode.get(index2).mergeDisjoint(elementsNode.get(index1).getDisjointList());
				elementsNode.get(index1).emptyDisjoint(index2);
			}
			//For two disjoint concepts, every concept in the known list should be deleted from the other's possible
			deleteInfoDisjoint(index1, index2);
			
		}
		
		
	public List<Integer> convertIndextoString(String parent, String child) 
	{
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
		List<AtomicInteger> knownParent = elementsNode.get(parentIndex).getKnownList();
		int counter =0;

		if (knownParent.size() == 0) {
			addInfoList(parentIndex, childIndex);
		} else {
			for (int i = 0; i < knownParent.size(); i++) {
				if (elementsNode.get(knownParent.get(i).intValue()).getPossibleList().get(childIndex - knownParent.get(i).intValue() - 1).intValue() == -1) 
				{// the concept in parent.known has compared with current concept
					if (checkExistingKnown(elementsNode.get(knownParent.get(i).intValue()).getKnownList(), childIndex)) 
					{// the current concept exist in its knownlist, it does not need to add to parent.known
						return;
					}
					else
						counter++;
				} else 
				{// current concept needs to compare with the concept in known
					if (counter ==0) {
						compareKnownList(knownParent.get(i).intValue(), childIndex);
						elementsNode.get(knownParent.get(i).intValue()).removePossible(childIndex);
						setCounter();
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
				&& elementsNode.get(indexParent).getPossibleList().get(indexChild - indexParent - 1).intValue() == -1)
			exist = false;
		else if (indexParent > indexChild
				&& elementsNode.get(indexChild).getPossibleList().get(indexParent - indexChild - 1).intValue() == -1)
			exist = false;
		return exist;
	}
	
	public boolean checkExistingKnown(List<AtomicInteger> knownParent, int childIndex)
	{
		boolean exist = false;
		
		for(int i=0; i<knownParent.size(); i++)
		{
			if(knownParent.get(i).intValue() == childIndex)
				return true;
		}
		return exist;
	}
	
	//One concept is subsumed by the other
	public void addOneSubsumInfoList(String subsumer, String subsumee) {
		int indexParent = getIndex(subsumer);
		int indexChild = getIndex(subsumee);

		List<AtomicInteger> Bknown = elementsNode.get(indexChild).getKnownList();
		List<AtomicInteger> Apossible = elementsNode.get(indexParent).getPossibleList();
		List<AtomicInteger> Aknown = elementsNode.get(indexParent).getKnownList();

		for (int i = 0; i < Apossible.size(); i++) 
		{
			/*if(elementsNode.get(indexParent).getEquivalentList().get(0).get() == -2)
			{
				Apossible = elementsNode.get(elementsNode.get(indexParent).getEquivalentList().get(0).get()).getPossibleList();
				Aknown = elementsNode.get(elementsNode.get(indexParent).getEquivalentList().get(0).get()).getKnownList();
			}*/
			for (int j = 0; j < Bknown.size(); j++) 
			{
				// delete B.known from A.possible add to A.impossible
				/*if(elementsNode.get(indexChild).getEquivalentList().get(0).get() == -2)
				{
					Bknown = elementsNode.get(elementsNode.get(indexChild).getEquivalentList().get(0).get()).getKnownList();
				}*/
				
				if (Bknown.get(j) == Apossible.get(i) /* && possible.get(i) != -1 */) 
				{
					if (indexParent < Bknown.get(j).intValue() && elementsNode.get(indexParent).removePossible(Bknown.get(j).intValue()))
						setCounter();
					// elementsNode.get(indexParent).addImpossible(Bknown.get(j));
				}
				// all nodes of B.known delete A from possible to impossible
				if (indexParent > Bknown.get(j).intValue() &&elementsNode.get(Bknown.get(j).intValue()).removePossible(indexParent))
					setCounter();
				// elementsNode.get(Bknown.get(j)).addImpossible(indexParent);

				// all A.known delete from B.known
				for (int k = 0; k < Aknown.size(); k++) 
				{
					if (Bknown.get(j) == Aknown.get(k)) 
					{
						elementsNode.get(indexChild).removeKnown(Aknown.get(k).intValue());
						break;
					}
				}
			}
		}
	}
	
	
	
	/*/***************************** Second version improvements****************************
	public void preComputeClass(OWLOntologyManager manager, OWLOntology ontology, OWLReasoner reasoner) 
			throws OWLOntologyCreationException
	{
		//File file = new File(inputfile);
		//OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		//OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
		//OWLReasoner reasoner = new Reasoner(ontology);
		List<String> listOfDisjointClasses = null;
		List<String> listOfEquivalentClasses = null;
		List<OWLClass> listOfSuperClasses = null;
		List<OWLClass> listOfChildClasses = null;
		//OWLClass concept = null;
		//String nameSubsumee = null;
		//int index = 0;
		//OWLClass subsumee;
		
		//boolean result;
		
		for (OWLClass cls : classElements) 
		{
			//concept = cls.asOWLClass();
			
			if(elementsNode.get(getIndex(cls.toString())).getUnSatisfiability() == false
					||getSatisfiability(reasoner, cls.asOWLClass()))//check the satisfiability
			{
				//Test disjoint Classes
				listOfDisjointClasses = getDisjointClass(ontology,cls);
				for(int i=0; i<listOfDisjointClasses.size(); i++)
				{
					//String nameDisjoint = listOfDisjointClasses.get(i);
			    	deleteInfoList(listOfDisjointClasses.get(i), cls.toString());
					System.out.println(listOfDisjointClasses.get(i) +"is disjoin with" + cls.toString());
			    		
				}
				
				//Test Equivalent Classes
				listOfEquivalentClasses = getEquivalentClass(ontology, cls);
				for(int i=0; i<listOfEquivalentClasses.size(); i++)
				{
					removeEquivalentConceptPossibleList(cls.toString(), listOfEquivalentClasses.get(i));	
					System.out.println(listOfEquivalentClasses.get(i) +"is equivalent with" + cls.toString());
				}
				
				//Test Super Classes
				listOfSuperClasses = getSuperClass(ontology, cls);
				for(int i=0; i<listOfSuperClasses.size(); i++)
				{
					OWLClass concept = listOfSuperClasses.get(i).asOWLClass();
			    	String nameSubsumer = concept.toString();
			    	int index = getIndex(nameSubsumer);
					//System.out.println("!!!!!" + index + nameSubsumer);
					
					//System.out.println(index);
					OWLClass subsumer = elementsNode.get(index).getOWLClassName();
					//System.out.println(subsumee);
					
					//addOneSubsumInfoList(concept.toString(), nameSubsumee);
					addInfoList(nameSubsumer, concept.toString());
					addOneSubsumInfoList( nameSubsumer, concept.toString());
					
					//To test whether two concepts are equivalent 
					boolean result = testOWLReasonerSubClass( manager, reasoner, concept, subsumer);
					if(result == true)
					{	
						//addInfoList(concept.toString(), nameSubsumer );
						//addOneSubsumInfoList(concept.toString(), nameSubsumer);
						removeEquivalentConceptPossibleList(concept.toString(), nameSubsumer);
					}
				}
				
				
				//Test Child Classes
			    listOfChildClasses = getChildClass(ontology, cls);
			    for(int i=0; i<listOfChildClasses.size(); i++)
			    {
			    	OWLClass concept = listOfChildClasses.get(i).asOWLClass();
			    	String nameSubsumee = concept.toString();
			    	int index = getIndex(nameSubsumee);
					//System.out.println("!!!!!" + index + nameSubsumee);
					
					//System.out.println(index);
					OWLClass subsumee = elementsNode.get(index).getOWLClassName();
					//System.out.println(subsumee);
					
					//addOneSubsumInfoList(concept.toString(), nameSubsumee);
					addInfoList(concept.toString(), nameSubsumee);
					addOneSubsumInfoList(concept.toString(), nameSubsumee);
					
					//To test whether two concepts are equivalent 
					boolean result = testOWLReasonerSubClass( manager, reasoner, subsumee, concept);
					if(result == true)
					{	
						//addInfoList(nameSubsumee, concept.toString());
						//addOneSubsumInfoList(nameSubsumee, concept.toString());
						removeEquivalentConceptPossibleList(concept.toString(), nameSubsumee);
					}
					/*else if(result == false)
					{
						addOneSubsumInfoList(concept.toString(), nameSubsumee);
						//addInfoList(concept.toString(), nameSubsumee);
					}
			    }
			    //if(concept.toString().equals("Nothing"))
			    //System.out.println("****************" + concept.toString());
			    //System.out.println(clname(cls));
		
				//getEquivalentClass(reasoner, concept);		
				//getDisjointClass(reasoner, concept);
				//getSubClass(manager, reasoner, concept);
			}
		}
		//reasoner.dispose();
	}
	
	
	/*public boolean getSatisfiability(OWLReasoner reasoner, OWLClass concept)
	{
		if(!reasoner.isSatisfiable(concept))
		{
			int index = getIndex(concept.toString());

			System.out.println("!!!!!!!!!!" + concept.toString() + " is unsatisfiable.");
			getElementsNode().get(index).setSatisfiability();
			// Delete all the concepts from subsumers' possible list
			emptyPossibleList(concept.toString());
			//deleteEquivalentfromPossibleList(concept.toString());
			return false;
		}
		return true;
	}
	
	//*************************Disjoint Classes *********************
	
	/*public void preComputeDisjointClass(OWLOntologyManager manager, OWLOntology ontology, OWLReasoner reasoner) 
			throws OWLOntologyCreationException
	{
		List<String> listOfDisjointClasses = null;
				
		for (OWLClass cls : classElements) 
		{
			//concept = cls.asOWLClass();
			if(elementsNode.get(getIndex(cls.toString())).getUnSatisfiability() == false
					||getSatisfiability(reasoner, cls.asOWLClass()))//check the satisfiability
			{
				//Test disjoint Classes
				listOfDisjointClasses = getDisjointClass(ontology,cls);
				for(int i=0; i<listOfDisjointClasses.size(); i++)
				{
					//String nameDisjoint = listOfDisjointClasses.get(i);
			    	deleteInfoList(listOfDisjointClasses.get(i), cls.toString());
					System.out.println(listOfDisjointClasses.get(i) +"is disjoin with" + cls.toString()); 		
				}
				
			}
		}
	}
	
	/*public List<String> getDisjointClass(OWLOntology ontology, OWLClass cls)
	{
		//List<OWLDisjointClassesAxiom> dClass = null;
		//dClass = entitysearch.getDisjointClasses(concept, ontology);

		if(cls.toString().trim().length() == 0)
			return null;
		List<String> listOfDisjointClasses = new ArrayList<String>();
		
		for (OWLDisjointClassesAxiom axiom : ontology.getDisjointClassesAxioms(cls))
		{
			//System.out.println(concept.toString() + dClass.getRepresentativeElement());
			String expression = axiom.getClass().toString();
			//OWLClass dConcept = dClass.getRepresentativeElement().asOWLClass();
			//System.out.println(dConcept + getIndex(dConcept) + "is disjoin with" + concept.toString());
			//if(!expression.equals("owl:Nothing"))
			//{	//deleteInfoList(concept.toString(), dConcept);	
				
				listOfDisjointClasses.add(expression);
			//}
		}
		
		return listOfDisjointClasses;
	}
	
	//****************************Equivalent Classes **********************
	
	/*public void preComputeEquivalentClass(OWLOntologyManager manager, OWLOntology ontology, OWLReasoner reasoner) 
			throws OWLOntologyCreationException
	{
		List<String> listOfEquivalentClasses = null;
		
		for (OWLClass cls : classElements) 
		{
			//concept = cls.asOWLClass();
			
			if(elementsNode.get(getIndex(cls.toString())).getUnSatisfiability() == false
					||getSatisfiability(reasoner, cls.asOWLClass()))//check the satisfiability
			{	
				//Test Equivalent Classes
				listOfEquivalentClasses = getEquivalentClass(ontology, cls);
				for(int i=0; i<listOfEquivalentClasses.size(); i++)
				{
					removeEquivalentConceptPossibleList(cls.toString(), listOfEquivalentClasses.get(i));	
					System.out.println(listOfEquivalentClasses.get(i) +"is equivalent with" + cls.toString());
				}
			}
		}
	}

	public List<String> getEquivalentClass(OWLOntology ontology, OWLClass cls)
	{
		List<String> listOfEquivalentClasses = new ArrayList<String>();
		
		for(OWLEquivalentClassesAxiom axiom : ontology.getEquivalentClassesAxioms(cls))
		{
			String expression = axiom.getClass().toString();
			//if(!expression.equals("owl:Nothing"))
			//{
				//OWLClass asOwlClass = expression.asOWLClass();
				listOfEquivalentClasses.add(expression);
			//}
		}
		
		return listOfEquivalentClasses;
	}
	
	//***************************Super Classes ****************************
	/*public void preComputeSuperClass(OWLOntologyManager manager, OWLOntology ontology, OWLReasoner reasoner) 
			throws OWLOntologyCreationException
	{
		List<OWLClass> listOfSuperClasses = null;		
		boolean result;
		
		for (OWLClass cls : classElements) 
		{
			//concept = cls.asOWLClass();
			
			if(elementsNode.get(getIndex(cls.toString())).getUnSatisfiability() == false
					||getSatisfiability(reasoner, cls.asOWLClass()))//check the satisfiability
			{			
				//Test Super Classes
				listOfSuperClasses = getSuperClass(ontology, cls);
				for(int i=0; i<listOfSuperClasses.size(); i++)
				{
					OWLClass concept = listOfSuperClasses.get(i).asOWLClass();
			    	String nameSubsumer = concept.toString();
			    	int index = getIndex(nameSubsumer);
					//System.out.println("!!!!!" + index + nameSubsumer);
					
					//System.out.println(index);
					OWLClass subsumer = elementsNode.get(index).getOWLClassName();
					//System.out.println(subsumee);
					
					//addOneSubsumInfoList(concept.toString(), nameSubsumee);
					addInfoList(nameSubsumer, concept.toString());
					addOneSubsumInfoList( nameSubsumer, concept.toString());
					
					//To test whether two concepts are equivalent 
					result = testOWLReasonerSubClass( manager, reasoner, concept, subsumer);
					if(result == true)
					{	
						//addInfoList(concept.toString(), nameSubsumer );
						//addOneSubsumInfoList(concept.toString(), nameSubsumer);
						removeEquivalentConceptPossibleList(concept.toString(), nameSubsumer);
					}
				}
			}
		}
	}*/
		
	
	public List<OWLClass> getSuperClass(OWLOntology ontology, OWLClass cls)
	{
		List<OWLClass> listOfSuperClasses = new ArrayList<OWLClass>();
		
		for(OWLSubClassOfAxiom axiom : ontology.getSubClassAxiomsForSuperClass(cls))
		{
			OWLClassExpression expression = axiom.getSubClass();
			if(!expression.isAnonymous())
			{
				OWLClass asOWLClass = expression.asOWLClass();
				listOfSuperClasses.add(asOWLClass);
			}
		}
		
		return listOfSuperClasses;
	}
	
	//***************************Child Classes ***************************
	
	/*public void preComputeChildClass(OWLOntologyManager manager, OWLOntology ontology, OWLReasoner reasoner) 
			throws OWLOntologyCreationException
	{
		List<OWLClass> listOfChildClasses = null;
		
		boolean result;
		
		for (OWLClass cls : classElements) 
		{
			//concept = cls.asOWLClass();
			
			if(elementsNode.get(getIndex(cls.toString())).getUnSatisfiability() == false
					||getSatisfiability(reasoner, cls.asOWLClass()))//check the satisfiability
			{
				//Test Child Classes
			    listOfChildClasses = getChildClass(ontology, cls);
			    for(int i=0; i<listOfChildClasses.size(); i++)
			    {
			    	OWLClass concept = listOfChildClasses.get(i).asOWLClass();
			    	String nameSubsumee = concept.toString();
			    	int index = getIndex(nameSubsumee);
					//System.out.println("!!!!!" + index + nameSubsumee);
					
					//System.out.println(index);
					OWLClass subsumee = elementsNode.get(index).getOWLClassName();
					//System.out.println(subsumee);
					
					//addOneSubsumInfoList(concept.toString(), nameSubsumee);
					addInfoList(concept.toString(), nameSubsumee);
					addOneSubsumInfoList(concept.toString(), nameSubsumee);
					
					//To test whether two concepts are equivalent 
					result = testOWLReasonerSubClass( manager, reasoner, subsumee, concept);
					/*if(result == true)
					{	
						//addInfoList(nameSubsumee, concept.toString());
						//addOneSubsumInfoList(nameSubsumee, concept.toString());
						removeEquivalentConceptPossibleList(concept.toString(), nameSubsumee);
					}
					/*else if(result == false)
					{
						addOneSubsumInfoList(concept.toString(), nameSubsumee);
						//addInfoList(concept.toString(), nameSubsumee);
					}
			    }
			}
		}
	}*/
	
	/*public List<OWLClass> getChildClass(OWLOntology ontology, OWLClass cls)
	{
		List<OWLClass> listOfChildClasses = new ArrayList<OWLClass>();
		
		for(OWLSubClassOfAxiom axiom : ontology.getSubClassAxiomsForSuperClass(cls))
		{
			OWLClassExpression expression = axiom.getSubClass();
			if(!expression.isAnonymous())
			{
				OWLClass asOWLClass = expression.asOWLClass();
				listOfChildClasses.add(asOWLClass);
				System.out.println(expression.toString() +"is equivalent with" + cls.toString());
			}
		}
		
		return listOfChildClasses;
	}*/
	

	public boolean testOWLReasonerSubClass( OWLOntologyManager manager, OWLReasoner reasoner,
			OWLClassExpression class1, OWLClassExpression class2) 
	{

		OWLAxiom axiom = manager.getOWLDataFactory().getOWLSubClassOfAxiom(class2, class1);
		String subsumer = class1.toString();
		String subsumee = class2.toString();

		getCounterNumofTests();
		if (reasoner.isEntailed(axiom)) 
		{
			//System.out.println("OWLReasoner(Merged): " + subsumee + " is subclass of " + subsumer);
			//System.out.println(subsumee);
		    addInfoList(subsumer, subsumee);
			addOneSubsumInfoList(subsumer, subsumee);
			//list.getCounterNumofTests();
			//markExistingHierarchy(list, class1, class2);
			return true;
		} 
		else {
			//System.out.println("OWLReasoner(Merged): " + clname(class1) + " is not necessarily subclass of " + clname(class2));
			deleteInfoList(subsumer, subsumee);
			//list.getCounterNumofTests();
			return false;
		}
	}


	
	
	/*public void getEquivalentClass(OWLReasoner reasoner, OWLClass concept)
	{		
		//List<OWLEquivalentClassesAxiom> eClass = null;
		//eClass.add(reasoner.getEquivalentClasses(concept));
		
		if(concept.toString().trim().length() == 0)
			return;
		Node<OWLClass> equivalentClasses = reasoner.getEquivalentClasses(concept);
		//Set<OWLClass> result = null;
		if(concept.isAnonymous())
		{
			for (OWLClass eClass : equivalentClasses.getEntities())
			{
				//System.out.println(concept.toString() + dClass.getRepresentativeElement());
				
				String eConcept = eClass.asOWLClass().toString();
				//System.out.println(eConcept + getIndex(eConcept) + "is equivalent with" + concept.toString());
				if(!eConcept.equals("owl:Nothing"))
					removeEquivalentConceptPossibleList(concept.toString(), eConcept);	
			}
			//result = equivalentClasses.getEntities();
		}
		else
		{	//result = equivalentClasses.getEntitiesMinus(concept.asOWLClass());
			for (OWLClass eClass : equivalentClasses.getEntitiesMinus(concept.asOWLClass()))
			{
				//System.out.println(concept.toString() + dClass.getRepresentativeElement());
			
				String eConcept = eClass.asOWLClass().toString();
				//System.out.println(eConcept + getIndex(eConcept) + "is equivalent with" + concept.toString());
				if(!eConcept.equals("owl:Nothing"))
					removeEquivalentConceptPossibleList(concept.toString(), eConcept);	
		}
		}
	}*/
	
	
	/*public void getSubClass(OWLOntologyManager manager, OWLReasoner reasoner, OWLClass concept)
	{
		//List<OWLSubClassOfAxiom> sClass = null;

		if(concept.toString().trim().length() == 0)
			return;
	
		String nameSubsumee;
		int index;
		OWLClass subsumee;
		boolean result;
		NodeSet<OWLClass> subClasses = reasoner.getSubClasses(concept, false);
		for (Node<OWLClass> sClass : subClasses.getNodes())
		{
			//System.out.println(concept.toString() + sClass.getRepresentativeElement());
			nameSubsumee = sClass.getRepresentativeElement().toString();
			//System.out.println(nameSubsumee);
			if(!nameSubsumee.equals("owl:Nothing"))
			{
			//System.out.println(nameSubsumee + getIndex(nameSubsumee) + "is subclass of" + concept.toString());
			index = getIndex(nameSubsumee);
			//System.out.println("!!!!!" + index + nameSubsumee);
			
			//System.out.println(index);
			subsumee = elementsNode.get(index).getOWLClassName();
			//System.out.println(subsumee);
			
			addInfoList(concept.toString(), nameSubsumee);
			
			result = testOWLReasonerSubClass( manager, reasoner, subsumee, concept);
			if(result == true)
			{	
				removeEquivalentConceptPossibleList(concept.toString(), nameSubsumee);
			}
			else if(result == false)
			{
				addOneSubsumInfoList(concept.toString(), nameSubsumee);
				addInfoList(concept.toString(), nameSubsumee);
			}
			}
		}
	}*/
	
//////////////////////////////////////////////////////////
//--------------------Third Version Precomputing Using Own Parser -------------------
	public void ExtractSubClassAxioms(OWLOntologyManager manager, OWLReasoner reasoner, ArrayList<OWLClassAxiom> axioms)
	{
		//ArrayList<OWLClassAxiom> axioms = getSubClassAxiom();
		String curAxiom, subsumeeClass, subsumerClass;
	
		for(int i=0; i<axioms.size(); i++)
		{
			curAxiom = axioms.get(i).toString();
			//System.out.println(curAxiom);
		
			if(curAxiom.indexOf('>')+2 == curAxiom.lastIndexOf('<'))
			{
				subsumeeClass = curAxiom.substring(curAxiom.indexOf('<'), curAxiom.indexOf('>')+1);
				//System.out.println(subsumeeClass);
		
				subsumerClass = curAxiom.substring(curAxiom.lastIndexOf('<'), curAxiom.lastIndexOf('>')+1);
				//System.out.println(subsumerClass);
				
				addInfoList(subsumerClass, subsumeeClass);
				addOneSubsumInfoList(subsumerClass, subsumeeClass);
				
				//To test whether two concepts are equivalent 
				boolean result = testOWLReasonerSubClass( manager, reasoner, elementsNode.get(getIndex(subsumeeClass)).getOWLClassName(), elementsNode.get(getIndex(subsumerClass)).getOWLClassName()/*, elementsNode*/);
				if(result == true)
				{	
					//addInfoList(nameSubsumee, concept.toString());
					//addOneSubsumInfoList(nameSubsumee, concept.toString());
					removeEquivalentConceptPossibleList(subsumerClass, subsumeeClass);
				}
			}
		}
	}
	
	public void ExtractEquivalentClassAxioms(ArrayList<OWLClassAxiom> axioms)
	{
		//ArrayList<OWLClassAxiom> axioms = getSubClassAxiom();
		String curAxiom, subsumeeClass, subsumerClass;
	
		for(int i=0; i<axioms.size(); i++)
		{
			curAxiom = axioms.get(i).toString();
			//System.out.println(curAxiom);
		
			if(curAxiom.indexOf('>')+2 == curAxiom.lastIndexOf('<'))
			{
				subsumeeClass = curAxiom.substring(curAxiom.indexOf('<'), curAxiom.indexOf('>')+1);
				//System.out.println(subsumeeClass);
		
				subsumerClass = curAxiom.substring(curAxiom.lastIndexOf('<'), curAxiom.lastIndexOf('>')+1);
				//System.out.println(subsumerClass);
				
				removeEquivalentConceptPossibleList(subsumeeClass, subsumerClass);	
				//System.out.println(subsumeeClass +"is equivalent with" + subsumerClass);
			}
		}
	}
	
	
	public void ExtractSuperClassAxioms(OWLOntologyManager manager, OWLReasoner reasoner, ArrayList<OWLClassAxiom> axioms)
	{
		//ArrayList<OWLClassAxiom> axioms = getSubClassAxiom();
		String curAxiom, subsumeeClass, subsumerClass;
	
		for(int i=0; i<axioms.size(); i++)
		{
			curAxiom = axioms.get(i).toString();
			//System.out.println(curAxiom);
		
			if(curAxiom.indexOf('>')+2 == curAxiom.lastIndexOf('<'))
			{
				subsumerClass = curAxiom.substring(curAxiom.indexOf('<'), curAxiom.indexOf('>')+1);
				//System.out.println(subsumerClass);

				subsumeeClass = curAxiom.substring(curAxiom.lastIndexOf('<'), curAxiom.lastIndexOf('>')+1);
				//System.out.println(subsumeeClass);

				addInfoList(subsumerClass, subsumeeClass);
				addOneSubsumInfoList(subsumerClass, subsumeeClass);
				
				//To test whether two concepts are equivalent 
				boolean result = testOWLReasonerSubClass( manager, reasoner, elementsNode.get(getIndex(subsumeeClass)).getOWLClassName(), elementsNode.get(getIndex(subsumerClass)).getOWLClassName()/*, elementsNode*/);
				if(result == true)
				{	
					//addInfoList(nameSubsumee, concept.toString());
					//addOneSubsumInfoList(nameSubsumee, concept.toString());
					removeEquivalentConceptPossibleList(subsumerClass, subsumeeClass);
				}
			}
		}
	}
	
	
	public void ExtractDisjointClassAxioms(ArrayList<OWLClassAxiom> axioms)
	{
		//ArrayList<OWLClassAxiom> axioms = getSubClassAxiom();
		String curAxiom, subsumeeClass, subsumerClass;
	
		for(int i=0; i<axioms.size(); i++)
		{
			curAxiom = axioms.get(i).toString();
			//System.out.println(curAxiom);
		
			if(curAxiom.indexOf('>')+2 == curAxiom.lastIndexOf('<'))
			{
				subsumeeClass = curAxiom.substring(curAxiom.indexOf('<'), curAxiom.indexOf('>')+1);
				//System.out.println(subsumeeClass);
		
				subsumerClass = curAxiom.substring(curAxiom.lastIndexOf('<'), curAxiom.lastIndexOf('>')+1);
				//System.out.println(subsumerClass);
				
				//deleteInfoList(subsumeeClass, subsumerClass);
				deleteDisjointClassInfoList(subsumeeClass, subsumerClass);
				//System.out.println("88888888888888");
				//System.out.println(subsumeeClass +"is disjoin with" + subsumerClass); 	
			}
		}
	}
	
	
	public void ExtractTopClass(OWLClass top)
	{
		if(top.toString() == "owl:Thing")
		{
			//traverse all the concepts in the top possible list and delete
			int indexTop = getIndex(top.toString());
			int possibleSize = elementsNode.get(indexTop).getPossibleSize();
			for(int i=0; i<possibleSize; i++)
			{
				addInfoList(top.toString(), getValue(elementsNode.get(indexTop).getPossibleList().get(i).get()));
				addOneSubsumInfoList(top.toString(), getValue(elementsNode.get(indexTop).getPossibleList().get(i).get()));

			}
			
		}
	}
	
	public void ExtractBottomClass(OWLClass bottom)
	{
		if(bottom.toString() == "owl:Nothing")
		{
			//?unsat
			//subsumee of all the other concepts in the possible?
			
		}
	}
	
	public int min(int num1, int num2)
	{
		if(num1 < num2)
			return num1;
		else 
			return num2;
	}
	
	public ArrayList<ArrayList<OWLClassAxiom>> divideAxioms(int numThread, ArrayList<OWLClassAxiom> axioms) 
	{
		ArrayList<ArrayList<OWLClassAxiom>> groups = new ArrayList<ArrayList<OWLClassAxiom>>();
		int totalnum = axioms.size();
		System.out.println("The size of Axioms is: " + totalnum);

		int numOrder = 0;

		for (int i = 0; i < min(numThread, totalnum); i++) //200/5 OR 5/200?  min(numThread, totalnum)???
		{
			ArrayList<OWLClassAxiom> subGroup = new ArrayList<OWLClassAxiom>();

			for (int j = 0; j < (totalnum / numThread) + 1; j++) 
			{
				if (numOrder < axioms.size()) 
				{
					subGroup.add(axioms.get(numOrder++));
				} else
					break;
			}
			System.out.println("The size of subGroup is: " + subGroup.size());
			groups.add(subGroup);
		}
		System.out.println("The size of Group is: " + groups.size());

		
		return groups;
	}
}
	
	