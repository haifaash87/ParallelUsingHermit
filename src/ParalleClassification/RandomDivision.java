package ParalleClassification;
//import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.semanticweb.owlapi.model.OWLClass;


public class RandomDivision extends AtomicNodeList
{

	//private static final SecureRandom RANDOM = new SecureRandom();
	private int[] order;
	//private List<String> elements;
	//private List<Node> elementsNode;
	
	//Constructor
	public RandomDivision(List<String> elements, List<OWLClass> classElements) 
	{
		super(elements, classElements);
		//this.elements = elements;
		//this.order = generateOrganizedOrder(elements.size(),numThread);
		this.order = generateRandomOrder(elements.size());
		//this.elementsNode = nodes;
	}
	
	
	private int[] generateIndex(int size) {
		int[] index = new int[size];
		for (int i = 0; i < size; i++) {
			index[i] = i;
		}
		return index;
	}
	
	// private static int step = 1;
	 
	//Disorder the original list
	private int[] generateRandomOrder(int size) {
		int[] index = generateIndex(size);

		int swap;
		for (int i = 0; i < size; i++) {
			Random rand = new Random();
			int randomIndex = rand.nextInt(size);
			// int randomIndex = step
			swap = index[i];
			index[i] = index[randomIndex];
			index[randomIndex] = swap;
		}
		return index;
	}

    //arithmetic series
	/*private int[] generateOrganizedOrder(int size, int numThread) {
		int times = 0;
		int[] index = generateIndex(size);
		int[] randomArray = new int[size];

		for (int j = 0, i = 0; j < size; j++, i = i + numThread) {
			if (i < size)
				randomArray[j] = index[i];
			else {
				times++;
				i = times;
				randomArray[j] = index[i];
			}
		}
	    for(int i=0;i<size;i++) { System.out.println(randomArray[i]); }
	    
		return randomArray;
	}*/
    
	
    /*private int getRandomInt(int lowerBound, int upperBound) 
    {
        return RANDOM.nextInt(upperBound - lowerBound) + lowerBound;
    }*/
    
	public int min(int num1, int num2)
	{
		if(num1 < num2)
			return num1;
		else 
			return num2;
	}
	
    //The division of the groups depends on the number of threads
	public ArrayList<ArrayList<Integer>> divideGroup(int numThread) 
	{
		ArrayList<ArrayList<Integer>> groups = new ArrayList<ArrayList<Integer>>();

		// order = generateOrganizedOrder(elements.size(),numThread);

		int totalnum = elements.size();
		int numOrder = 0;

		
		for (int i = 0; i < min(numThread, totalnum); i++) 
		{
			ArrayList<Integer> subGroup = new ArrayList<Integer>();

			for (int j = 0; j < (totalnum / numThread) + 1; j++) 
			{
				if (numOrder < order.length) 
				{
					subGroup.add(order[numOrder++]);
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
