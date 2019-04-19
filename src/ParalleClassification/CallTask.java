package ParalleClassification;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class CallTask implements Callable <Long>{

	private int id;
	//private AtomicNodeList list;
	private AtomicNodeList list;
	private ArrayList<Integer> group = null;
	//private OWLClass reasoner;
	private File ifile = null;
	//private WriteFile oFile = null; //WriteFile Wfile = new WriteFile(outputFile);
	//private WriteFileStream oFile = null;
	private List<AtomicNode> elementsNode = null;
	//private String inputfile;
	//private String outputFile;

	//private List<RunReasoner> ReasonerPool = null;
	private OWLOntologyManager manager = null;
	private OWLOntology ontology = null;
	private OWLReasoner reasoner = null;
	private List<Long> TimeArray = new ArrayList<Long>();
	
	public CallTask(int id, /*List<RunReasoner> ReasonerPool, OWLOntologyManager manager, OWLOntology ontology, 
			OWLClass reasoner,*/ ArrayList<Integer> group, AtomicNodeList list, String inputfile,/* WriteFile outfile,*/ List<Long> arrTime) throws OWLOntologyCreationException, IOException
	{
		this.id = id; 
		this.ifile = new File(inputfile);
		//this.oFile = outfile;
		this.TimeArray = arrTime;
		//this.ReasonerPool = ReasonerPool;
		this.manager = OWLManager.createOWLOntologyManager();
		this.ontology = manager.loadOntologyFromOntologyDocument(ifile);
		this.reasoner = new Reasoner( ontology );
		//this.reasoner = new OWLClass(inputfile);
		this.group = group;
		this.list = list;
		this.elementsNode = list.getElementsNode();
		//this.outputFile = outputfile;
	}
	

	public Long call() throws IOException 
	{	
		System.out.println("Call() function is called！！"+ id + Thread.currentThread().getName());
		//List<ParalleClassification.Node> elementsNode = list.getElementsNode();
		Time time = new Time(); 
		
    	time.start();
		//System.out.println("The parameter group size is: " + group.size());
		
		List<String> groupElements = new ArrayList<String>();
		for (int k = 0; k < group.size(); k++) 
		{
			String value = list.getValue(group.get(k));
			//int index = list.getIndex(value);
			//if (elementsNode.get(index).getUnSatisfiability() == true)
				//continue;
			//if (elementsNode.get(index).traversePossibleList().size() == 0)
				//continue;
			groupElements.add(value);// Problem solved!
		}

		//printGroupElements(groupElements);
		//getInstancesofClass(list, groupElements, elementsNode);
		getInstancesofClass(groupElements);
		
		time.stop();
		//String info2 = time.printTime2();
		
		try
		{
			Thread.sleep(100);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		System.out.println(id + Thread.currentThread().getName() +"is finished!");
		reasoner.dispose();
		
		if(time.getDuration() != 0)
			TimeArray.add(time.getDuration());
		
		//oFile.writeFileInfo(time.calculateDeviation());
		//oFile.writeFileInfo(time.calculateMax());
		//oFile.writeFileInfo(time.calculateMin());
		//oFile.writeFileInfo(time.calculateMedian());
		//oFile.writeFileInfo(time.calculateAverage());
		//oFile.writeFileInfo("*********");
		//oFile.writeFileInfo("\n");
		
		return time.getDuration();
		
		//return "11111";
	}
	

	public void printGroupElements(List<String> groupElements) {
		//System.out.println("*****The size of groupElements is:" + groupElements.size() + "*****");
		for (int i = 0; i < groupElements.size(); i++) {
			System.out.println(groupElements.get(i));
		}
	}
	
	//@SuppressWarnings("null")
	public void getInstancesofClass(/*NodeList list,*/ List<String> groupElements/*, List<ParalleClassification.Node> elementsNode*/) throws IOException
    {
    	//System.out.println( " ***** Testing consistency (and coherency) with HermiT ***** " );
    	//Time ctime = new Time();
    	
		for (int i = 0; i < groupElements.size(); i++) 
		{
			String value1 = groupElements.get(i);
			
			int index1 = list.getIndex(value1);
			if (elementsNode.get(index1).traversePossibleList() == null)
				continue;//avoid empty possible list
			if (elementsNode.get(index1).getUnSatisfiability() == true) 
			{
				//list.emptyPossibleList(value1);
				continue;//avoid repeated tests for unsatisfiable class
			}
			
			
			for (int j = i+1; j < groupElements.size(); j++) 
			{
				//ctime.start();
				String value2 = groupElements.get(j);
				//int index2 = list.getIndex(value2);
				//if(value1.equals(value2))
				//{
					//list.removeEquivalentConceptPossibleList(value1, value2);
					//if(index1 > index2)
					    //break;	
					//else if(index2 > index1)
					    //continue;
					//removeEquivalentConceptPossibleList(value1, value2);
				//}
				int index2 = list.getIndex(value2);
				if (elementsNode.get(index2).traversePossibleList() == null)
				{
					continue;//avoid empty possible list
				}
	    		if(elementsNode.get(index2).getUnSatisfiability() == true)
	    		{
	    			//list.deleteInfoList(value1, value2);
	    			//list.emptyPossibleList(value2);
	    			continue;//avoid repeated tests for unsatisfiable class
	    		}
	    		if(!list.checkExistingPossible(value1, value2))
	    			continue;//avoid repeated subsumption tests with the same concepts
	    		
	    		//ReasonerSubTest(list, value1, value2, elementsNode);
	    		//ReasonerSubTest(list, manager, reasoner, value1, value2, elementsNode);
	    		ReasonerSubTest( manager, reasoner, value1, value2);
	    		//ctime.stop();
	    		//if(ctime.getDuration() != 0)
	    			//ctime.add(ctime.getDuration());
			}
		}

    }
	
	
	public void ReasonerSubTest(/*NodeList list,*/ OWLOntologyManager manager, OWLReasoner reasoner,
			String value1, String value2/*, List<ParalleClassification.Node> elementsNode*/){
		
		//IRI iri1 = IRI.create(value1);
		int index1 = list.getIndex(value1);
		OWLClass class1 = elementsNode.get(index1).getOWLClassName();//manager.getOWLDataFactory().getOWLClass(iri1);
		int index2 = list.getIndex(value2);
		OWLClass class2 = elementsNode.get(index2).getOWLClassName();
		
		
		boolean result1 = testOWLReasonerSubClass(/*list,*/ manager, reasoner, class1, class2/*, elementsNode*/);
		
		boolean result2 = testOWLReasonerSubClass(/*list,*/ manager, reasoner, class2, class1/*, elementsNode*/);
		//find two concepts are equal or not
		if(result1 == true && result2 == true)
		{
			//System.out.println(class1.toString() + " is equal to " + class2.toString());
			list.removeEquivalentConceptPossibleList(value1, value2);
		}
		else if(result1 == true && result2 == false)
		{//class2 is subsumed by class1, but class 1 is not subsumed by class2
			list.addOneSubsumInfoList(value1, value2);
		}
		else if(result1 == false && result2 == true)
		{
			list.addOneSubsumInfoList(value2, value1);
		}
		/*else if(result1 == false && result2 == false)
		{
			list.addNoSubsumInfoList(value1, value2);
		}*/
	}
	
	private boolean testOWLReasonerSubClass(/*NodeList list,*/ OWLOntologyManager manager, OWLReasoner reasoner,
			OWLClassExpression class1, OWLClassExpression class2/*, List<ParalleClassification.Node> elementsNode*/) 
	{

		OWLAxiom axiom = manager.getOWLDataFactory().getOWLSubClassOfAxiom(class2, class1);
		String subsumer = class1.toString();
		String subsumee = class2.toString();
		//if(!reasoner.isSatisfiable(class1))
		//{
		int index = list.getIndex(subsumer);
		if (elementsNode.get(index).getUnSatisfiability() == true) 
		{
				//System.out.println("!!!!!!!!!!" + subsumer + " is unsatisfiable.");
				//elementsNode.get(index).setSatisfiability();
				// Delete all the concepts from subsumers' possible list
				//list.emptyPossibleList(subsumer);
				list.deleteInfoList(subsumer, subsumee);
				//list.deleteEquivalentfromPossibleList(subsumee);
				//list.deleteEquivalentfromPossibleList(subsumer);
				return false;
		}
		else if (reasoner.isEntailed(axiom)) 
		{
			//System.out.println("OWLReasoner(Merged): " + subsumee + " is subclass of " + subsumer);
			//System.out.println(subsumee);
			list.addInfoList(subsumer, subsumee);
			list.getCounterNumofTests();
			//markExistingHierarchy(list, class1, class2);
			return true;
		} 
		else {
			//System.out.println("OWLReasoner(Merged): " + clname(class1) + " is not necessarily subclass of " + clname(class2));
			list.deleteInfoList(subsumer, subsumee);
			list.getCounterNumofTests();
			return false;
		}
	}
	
}
