package ParalleClassification;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.semanticweb.owlapi.model.OWLClass;

public class SubDivision extends AtomicNodeList{
	
	//private List<String> elements;
	//private List<Node> elementsNode;
	
	//Constructor
	public SubDivision(List<String> elements, List<OWLClass> classElements)
	{
		super(elements, classElements);
		//this.elements = elements;
		//this.elementsNode = nodes;
	}
	
	
	//Return the node's possible list which is not empty
	public List<Integer> findExistingPossibleList(/*List<Node> elementsNode*/)
	{
		List<Integer> notEmptyPossibleList = new ArrayList<Integer>();
		
		for(int i=0; i<elementsNode.size();i++)
		{
			if(!elementsNode.get(i).isEmptyPossible())
			{
				notEmptyPossibleList.add(i);
			}
		}
		
		return notEmptyPossibleList;//If the list is empty, it will return null which means the program can stop.
	}
	
	
	//Divide groups depend on the existing nodes in the possible list
	/*public int getNumofPossibleNode()
	{
		int numPossibleNode=0;
		List<Integer> compareList = findExistingPossibleList();
		
		for(int i=0; i<compareList.size(); i++)
		{
			int index = compareList.get(i);
			//int num = elementsNode.get(index).getPossibleList().size();
			int num = elementsNode.get(index).traversePossibleList().size();
			numPossibleNode = numPossibleNode + num ;	
		}
		
		return numPossibleNode;
	}*/
	
	
	public List<Integer> getTotalPossibleList()
	{
		List<Integer> nodeList = findExistingPossibleList();
		
		if(nodeList == null)
		{
			System.out.println("All the possible nodes have been tested.");
			return null;
		}
		//List<Integer> allPossibleNodes = new ArrayList<Integer>();
		List<Integer> allPossibleNodes = new ArrayList<Integer>();
		List<AtomicInteger> value;// = new ArrayList<AtomicInteger>();
		
		for(int i=0; i<nodeList.size(); i++)
		{
			allPossibleNodes.add(nodeList.get(i));
			//value = elementsNode.get(nodeList.get(i)).getPossibleList();
			//allPossibleNodes.addAll(elementsNode.get(nodeList.get(i)).traversePossibleList());
			value = elementsNode.get(nodeList.get(i)).traversePossibleList();
			for(int j=0; j<value.size(); j++)
			{
			   allPossibleNodes.add(value.get(j).intValue());
			}
		}
		
		return allPossibleNodes;
	}
	//***************************** Second version improvements
	//Here try to redivision the group which the divions of each group almost evenly separated.
	//First,Get all the concepts which the remaining posssible list is not empty. 
	//Second, divided these concept into different threads with same numbers (evenly divided)
	//Third, redivision each remaining possible list make all the divions for possible are evenly separated.
	//Notes: the current number of groups can be more than the number of current threads?!
	public ArrayList<ArrayList<Integer>> subRedivideGroup(int numThread)
	{
		//List<Integer> allPossibleNodes = getTotalPossibleList();
		List<Integer> allRemainingPossibleNodes = findExistingPossibleList(); //total number of remaining main nodes(not include possible)
		ArrayList<ArrayList<Integer>> groups = new ArrayList<ArrayList<Integer>>();

		
		int currentIndex = 0, compareIndex = 0;
		List<AtomicInteger> currentPossible = new ArrayList<AtomicInteger>();
		List<AtomicInteger> comparePossible = new ArrayList<AtomicInteger>();
				
		//balance the possible list of all the remaining concepts
		for (int i=0; i< allRemainingPossibleNodes.size() ; i++)
		{
		  currentIndex = allRemainingPossibleNodes.get(i);
		  currentPossible = elementsNode.get(currentIndex).getPossibleList();
		  for(int j=0; j<currentPossible.size();j++)
		  { 
			  compareIndex = currentPossible.get(j).intValue();
			  comparePossible = elementsNode.get(compareIndex).getPossibleList();
			  if(elementsNode.get(currentIndex).getPossibleSize() > elementsNode.get(compareIndex).getPossibleSize() && elementsNode.get(compareIndex).isEmptyPossible() != true)
			  {
				  elementsNode.get(currentIndex).removePossible(compareIndex);
				  elementsNode.get(compareIndex).addPossible(currentIndex);
			  }
		  }
		}
		//allRemainingPossibleNodes = findExistingPossibleList();
		
		int numNodeForEachThread = (allRemainingPossibleNodes.size()/numThread)+1;//number of main nodes averagely separated into each thread
		int groupIndex = 0, index =0;
		Group group = new Group(groupIndex);
		int possibleIndex = 0;
		//divide all the concept's possible into different groups
		//int i=0;
		int num=0;
		for(int i=0; index < allRemainingPossibleNodes.size() && groupIndex < numThread ; i++)
		{
			currentIndex = allRemainingPossibleNodes.get(index);
			//System.out.println(currentIndex + "---------------------------------");
			if(i < numNodeForEachThread)
			{
				if(elementsNode.get(currentIndex).getFlag() == -1 || elementsNode.get(currentIndex).getFlag() != groupIndex)
				{
					group.addNode(currentIndex);
					elementsNode.get(currentIndex).setFlag(groupIndex);
				}
				
				for(int j=0; j<elementsNode.get(currentIndex).traversePossibleList().size(); j++)//current group
				{
					possibleIndex = elementsNode.get(currentIndex).traversePossibleList().get(j).intValue();
					//System.out.println(possibleIndex + "---------------------------------+"+elementsNode.get(currentIndex).getPossibleList().get(j+1));
					if(elementsNode.get(possibleIndex).getFlag() == -1 ||
					elementsNode.get(possibleIndex).getFlag() != groupIndex)
					{
						group.addNode(elementsNode.get(currentIndex).traversePossibleList().get(j).intValue());
						elementsNode.get(elementsNode.get(currentIndex).traversePossibleList().get(j).intValue()).setFlag(groupIndex);
					}
				}
				num = num + elementsNode.get(currentIndex).traversePossibleList().size();
				index++;
			}
			else
			{
				i=0;
				groups.add(group.getNodes());
				System.out.println(group.getGroupSize() + "---------------------------------"+ num);
				group = new Group(groupIndex++);
				if(elementsNode.get(currentIndex).getFlag() == -1 || elementsNode.get(currentIndex).getFlag() != groupIndex)
				{
					group.addNode(currentIndex);
					elementsNode.get(currentIndex).setFlag(groupIndex);
				}
				for(int j=0; j<elementsNode.get(currentIndex).traversePossibleList().size(); j++)//current group
				{
					possibleIndex = elementsNode.get(currentIndex).traversePossibleList().get(j).intValue();
					if(elementsNode.get(possibleIndex).getFlag() == -1 ||
					elementsNode.get(possibleIndex).getFlag() != groupIndex)
					{
						group.addNode(elementsNode.get(currentIndex).traversePossibleList().get(j).intValue());
						elementsNode.get(elementsNode.get(currentIndex).traversePossibleList().get(j).intValue()).setFlag(groupIndex);
					}
				}
				index++;
			 }
		}
		
		groups.add(group.getNodes());

		return groups;

	}
	
	//*****************************
	
	//In the function, first get the comparelist which includes all the nodes without an empty possible list.
	//Second, traverse all the possible list of the nodes in the compareList.
	//Third, divide all the nodes into different groups(Threads) which depends on the size of the group.
	
	public int getThreadNum(int num, int totalPossible)
	{
		int numThread = num;
		int totalnum = totalPossible;
		   
		while(totalnum/numThread < 5)
		{     
			numThread --;  
		}
		System.out.println("There are " + numThread + " threads used.");
		   
		return numThread;
	}   
	
	public ArrayList<ArrayList<Integer>> subDivideGroup(int numThread)
	{
		List<Integer> allPossibleNodes = getTotalPossibleList();
		int num = allPossibleNodes.size();// total number of nodes
		int average = (num/numThread)+1;// number of nodes for each group
		
		int groupIndex = 0;//record number of groups
		int index =0;//record total number of nodes, in the end equals to num
		int j=0;//record number of nodes in one group
		
		ArrayList<ArrayList<Integer>> groups = new ArrayList<ArrayList<Integer>>();
		Group group = new Group(groupIndex);
		
		for(j=0;index<num && groupIndex < numThread; j++)
		{
			if(j<average)
			{				
				if(elementsNode.get(allPossibleNodes.get(index)).getFlag() == -1 || 
						elementsNode.get(allPossibleNodes.get(index)).getFlag() != groupIndex)
				{
					group.addNode(allPossibleNodes.get(index));
					elementsNode.get(allPossibleNodes.get(index)).setFlag(groupIndex);
				}
				index++;
			}
			else
			{
				j=0;
				groups.add(group.getNodes());
				group = new Group(++groupIndex);
				if(elementsNode.get(allPossibleNodes.get(index)).getFlag() == -1 || 
						elementsNode.get(allPossibleNodes.get(index)).getFlag() != groupIndex)
				{
					group.addNode(allPossibleNodes.get(index));
					elementsNode.get(allPossibleNodes.get(index)).setFlag(groupIndex);
				}
				
				index++;
			}
			//System.out.println("The size of subGroup is: " + group.getGroupSize());

		}
		groups.add(group.getNodes());
		System.out.println("The size of Group is: ---------" + groups.size());

		return groups;
	}
	
	
	////////////////???????????????????????????
	////whywhywhywhywhy
	/*public ArrayList<ArrayList<Integer>> divideGroup(List<String> elements, Integer numThread)
	{	
		ArrayList<ArrayList<Integer>> groups = new ArrayList<ArrayList<Integer>>();
		//ArrayList<Integer> group = new ArrayList<>();
		//int flagNode = -2;
		//int flagPossible = -2;
		
		List<Integer> compareList = findExistingPossibleList(elementsNode);//The node without an empty possible list
		List<Integer> possibleList = new ArrayList<Integer>();
		
		if(compareList == null)
		{
			System.out.println("All the possible nodes have been tested.");
			return null;
		}

		int num = getNumofPossibleNode();//Total number of possible nodes in the list
		System.out.println(num);
		
		int average = ((num + elements.size())/numThread)+1;
		System.out.println(average);
		int groupIndex = 0;
		int nodeIndex = -1;

		Group group = new Group(groupIndex);

		for(int i=0; i<compareList.size(); i++)
		{
			nodeIndex = compareList.get(i);
			possibleList = elementsNode.get(nodeIndex).traversePossibleList();
			
			if(group.getGroupSize() > average && groupIndex < elements.size())
			{
				groups.add(group.getNodes());
				groupIndex++;
				group = new Group(groupIndex);
				System.out.println("---------------------------------");
			}
			group.addNode(elementsNode.get(nodeIndex).getIndex());
			System.out.println(i + "---------------------------------");
			//elementsNode.get(nodeIndex).setFlag(groupIndex);

			for (int j = 0; j < elementsNode.get(nodeIndex).getPossibleList().size(); j++) 
			{
				if (group.getGroupSize() > average && groupIndex < numThread) 
				{
					groups.add(group.getNodes());
					groupIndex++;
					group = new Group(groupIndex);
				}
				group.addNode(possibleList.get(j));
				System.out.println( j + "*******************************");
				//elementsNode.get(possibleList.get(j)).setFlag(groupIndex);
			}
		}
	
		/*for (int i = 0; i < compareList.size(); i++) 
		{
			nodeIndex = compareList.get(i);// get index for current node
			possibleList = elementsNode.get(nodeIndex).traversePossibleList();// get possible list for current node
			flagNode = elementsNode.get(nodeIndex).getFlag();// get flag for
																// current node
			if (group.getGroupSize() > average && groupIndex < numThread) {
				groups.add(group.getNodes());
				groupIndex++;
				group = new Group(groupIndex);
			}
			if (flagNode == -1) {
				group.addNode(elementsNode.get(nodeIndex).getIndex());
				elementsNode.get(nodeIndex).setFlag(groupIndex);
			}
			for (int j = 0; j < elementsNode.get(nodeIndex).getPossibleListSize(); j++) 
			{
				flagPossible = elementsNode.get(possibleList.get(j)).getFlag();// get flag for possible node
				if (group.getGroupSize() > average && groupIndex < numThread) {
					groups.add(group.getNodes());
					groupIndex++;
					group = new Group(groupIndex);
				}
				if (flagPossible == -1)// possible节点未添加
				{
					//System.out.println(possibleList.get(j));
					group.addNode(possibleList.get(j));
					elementsNode.get(possibleList.get(j)).setFlag(groupIndex);
				}
			}	
			System.out.println("The Size of current possible list is: " + elementsNode.get(nodeIndex).getPossibleListSize());
			System.out.println("********"+groupIndex);
		}
		
		
		/*for (int groupNum = 0; groupNum < numThread; groupNum++) {
			// System.out.println("The total number of possible nodes is " +
			// num);
			for (int i = 0; i < compareList.size(); i++) {
				int index = compareList.get(i);// get index for current node
				List<Integer> leftPossibleList = elementsNode.get(index).traversePossibleList();// get possible lisfor current node

				flagNode = elementsNode.get(index).getFlag();// get flag for current node

				group.addNode(elementsNode.get(index).getIndex());
				elementsNode.get(index).setFlag(groupIndex);
				System.out.println(
						"The Size of current possible list is: " + elementsNode.get(index).getPossibleListSize());
				if (group.getGroupSize() < (num / numThread) + 1) {
					for (int j = 0; j < elementsNode.get(index).getPossibleListSize(); j++) {
						flagPossible = elementsNode.get(leftPossibleList.get(j)).getFlag();// get flag for possible node
						if (flagPossible == -1)// possible节点未添加
						{
							System.out.println(leftPossibleList.get(j));
							group.addNode(leftPossibleList.get(j));
							elementsNode.get(leftPossibleList.get(j)).setFlag(groupIndex);
						}
						/*
						 * else // possible节点已添加 { if (flagNode != groupIndex &&
						 * flagNode == -1) // Node节点尚未添加 {
						 * group.addNode(elementsNode.get(index).getIndex());
						 * elementsNode.get(index).setFlag(groupIndex); } }
						
					}
				} else {
					ArrayList<Integer> nodes = group.getNodes();
					for (int k = 0; k < nodes.size(); k++) {
						System.out.println(nodes.get(k));
					}
					groups.add(nodes);
					groupIndex++;
					group = new Group(groupIndex);
				}

			}
		}
		return groups;	
	}*/
}
