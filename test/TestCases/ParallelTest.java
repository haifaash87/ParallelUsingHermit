package TestCases;

import java.io.*;
//import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
//import java.util.Scanner;
import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.kohsuke.args4j.CmdLineException;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
//import org.kohsuke.args4j.CmdLineException;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.UnknownOWLOntologyException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;


import ParalleClassification.*;

public class ParallelTest {
	    
	/*public static void setUp() throws OWLOntologyCreationException, OWLOntologyStorageException 
	{
		File file = new File("/Users/Zixi/Dropbox/Research/HermiTParallelVersion1/project/examples/ontologies/TestExample.owl");
        
        OWLOntologyManage                                                                                                                                                            r manager = OWLManager.createOWLOntologyManager();
        //Load the ontology
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file); 
        System.out.println("Loaded ontology: " + ontology);
        
        //OWLReasonerConfiguration config = new SimpleConfiguration(50000);
      
        
        IRI documentIRI = IRI.create("http://www.owl-ontologies.com/generations#");
        System.out.println("From: " + documentIRI);
        
        OWLOntologyFormat format = manager.getOntologyFormat(ontology);
        System.out.println("Format: " + format);
        
        //Save the ontology in owl/xml format
        OWLXMLOntologyFormat owlxmlFormat = new OWLXMLOntologyFormat();
        if(format.isPrefixOWLOntologyFormat())
        {
        	owlxmlFormat.copyPrefixesFrom(format.asPrefixOWLOntologyFormat());
        }
        manager.saveOntology(ontology, owlxmlFormat, IRI.create(file.toURI()));       
        
        Set<OWLClass> classes = ontology.getClassesInSignature();
        for(OWLClass cls : classes)
        {
        	for(OWLAnnotation annotation: ontology.getAnnotations())
            {
        	     System.out.println(cls + " Annotations:" + annotation.getValue());
            }
        }
	 }*/
	static int numSuperClass = 0;
	static int numSubClass = 0;
	static int numEquivalent = 0;
	static int numDisjoint = 0;
	
	public static void precomputeParallel(OWLReasoner reasoner, OWLOntologyManager manager, OWLParser parser,
			int numThread, AtomicNodeList list1) throws InterruptedException, ExecutionException
	{
		ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numThread);

    	//do{
    		ArrayList<OWLClassAxiom> axioms2, axioms3, axioms4;
    		ArrayList<ArrayList<OWLClassAxiom>> groups = null;
    		Future <ArrayList<OWLClassAxiom>> futurecall = null;
    	
    		/*if(axioms.size() != 0)
    		{
    			groups = list1.divideAxioms(list1.getThreadNumforAxioms(numThread, axioms), axioms);
    			int indexThread1 = 0;
    			while(indexThread1<list1.getThreadNumforAxioms(numThread, axioms))
    			{
    				executor.submit(new RunTask(reasoner, manager, list1, groups.get(indexThread1), 1));

    				indexThread1++;
    			}
    		}*/
    		//executor.shutdown(); 
    		
    		//axioms = parser.getSubClassAxiom();
    		futurecall =executor.submit(new CallTask1(parser, 2));
    		axioms2 = futurecall.get();
    		numSubClass = axioms2.size();
    		if(numSubClass != 0)
    		{
    			groups = list1.divideAxioms(list1.getThreadNumforAxioms(numThread, axioms2), axioms2);
    			//ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numThread);
    			int indexThread2 = 0;
    			while(indexThread2<list1.getThreadNumforAxioms(numThread, axioms2))
    			{
    				executor.submit(new RunTask(reasoner, manager, list1, groups.get(indexThread2), 2));

    				indexThread2++;
    			}
    		}
    		//executor.shutdown(); 
    		
    		//axioms = parser.getEquivalentClassAxiom();
    		futurecall =executor.submit(new CallTask1(parser, 3));
    		axioms3 = futurecall.get();
    		numEquivalent = axioms3.size();
    		if(numEquivalent != 0)
    		{
    			groups = list1.divideAxioms(list1.getThreadNumforAxioms(numThread, axioms3), axioms3);
    			//ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numThread);
    			int indexThread3 = 0;
    			while(indexThread3<list1.getThreadNumforAxioms(numThread, axioms3))
    			{
    				executor.submit(new RunTask(reasoner, manager, list1, groups.get(indexThread3), 3));

    				indexThread3++;
    			}
    		}
    		//executor.shutdown(); 
    		
    		//axioms = parser.getDisjointClassAxiom();
    		futurecall =executor.submit(new CallTask1(parser, 4));
    		axioms4 = futurecall.get();    		
    		numDisjoint = axioms4.size();
    		if(numDisjoint != 0)
    		{
    			groups = list1.divideAxioms(list1.getThreadNumforAxioms(numThread, axioms4), axioms4);
    			//ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numThread);
    			int indexThread4 = 0;
    			while(indexThread4<list1.getThreadNumforAxioms(numThread, axioms4))
    			{
    				executor.submit(new RunTask(reasoner, manager, list1, groups.get(indexThread4), 4));

    				indexThread4++;
    			}
    		}
///?????????????????  figure out how to shut them down before going on!!
    		executor.shutdown(); 
    		while(true)
			{
				if(executor.isTerminated())
					return;
			}
	}
	
	public static void precomputeSequential(OWLParser parser, OWLOntologyManager manager, OWLReasoner reasoner, 
			AtomicNodeList list1, WriteFile Wfile) throws IOException
	{
		//SuperClass
    	ArrayList<OWLClassAxiom> superClassAxiom = parser.getSuperClassAxiom();
    	list1.ExtractSuperClassAxioms(manager, reasoner, superClassAxiom); //Done!
    	System.out.println("After computing superClass, The total number of possible node is " + list1.getTotalNumofPossible());
    	//Wfile.writeFileInfo(list1.getTotalNumofPossible());
    	//SubClass
    	ArrayList<OWLClassAxiom> subClassAxiom = parser.getSubClassAxiom();
    	list1.ExtractSubClassAxioms(manager, reasoner, subClassAxiom); //Done!
    	System.out.println("After computing subClasss, The total number of possible node is " + list1.getTotalNumofPossible());
    	//Wfile.writeFileInfo(list1.getTotalNumofPossible());
        //EquivalentClass
    	ArrayList<OWLClassAxiom> equivalentClassAxiom = parser.getEquivalentClassAxiom();
    	list1.ExtractEquivalentClassAxioms(equivalentClassAxiom);
    	System.out.println("After computing equivalentClass, The total number of possible node is " + list1.getTotalNumofPossible());
    	//Wfile.writeFileInfo(list1.getTotalNumofPossible());
    	//DisjointClass
    	ArrayList<OWLClassAxiom> disjointClassAxiom = parser.getDisjointClassAxiom();
    	list1.ExtractDisjointClassAxioms(disjointClassAxiom);
    	System.out.println("After computing disjointClass, The total number of possible node is " + list1.getTotalNumofPossible());
    	//Wfile.writeFileInfo(list1.getTotalNumofPossible());
    	//Test Satisfiability --- Not really necessary...
    	//list1.getSatisfiability(reasoner, concept)
    	
    	//TopEntity owl:Thing
    	//list1.ExtractTopClass(parser.getTopClass());
    	//System.out.println("After computing disjointClass, The total number of possible node is " + list1.getTotalNumofPossible());
    	
    	//Bottom owl:Nothing
    	//list1.ExtractBottomClass(parser.getBottomClass());
	}
	
    public static void main (String[] args) throws OWLOntologyCreationException, IOException, InterruptedException, ExecutionException, UnknownOWLOntologyException, OWLOntologyStorageException 
    {		
		System.out.println(System.getProperty("java.library.path"));
		for(String arg : args)
		{
			System.out.println(arg);
		}
		
		CommandLineArgs arguments = null;
			try {
				arguments = new CommandLineArgs(args);
			} catch (CmdLineException e) {
				e.printStackTrace();
			}
			
		String inputFile = arguments.getOntologyFilePath();
		int numThread = arguments.NumThread();
		String outputFile = arguments.getOutputFilePath();
		
		//String inputFile = "/Users/quanzixi/Desktop/Ontology/18d69173-cf8b-4141-b8d2-7c73a834c95d_ncitations.owl_functional.owl";
		//String inputFile = "/Users/quanzixi/Desktop/Ontology/pizza.owl";
		//String inputFile = "/Users/quanzixi/Desktop/00284.owl_functional.owl";
		//int numThread = 5; 
		//String outputFile = "/Users/quanzixi/Desktop/output1.txt"; 
		
		Time time = new Time();
    	
		File file = new File(inputFile);
		IRI docIRI = IRI.create(file);
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(docIRI);
		//OWLDataFactory factory = manager.getOWLDataFactory();
		System.out.println("Loaded: " + ontology.getOntologyID());
	    OWLReasoner reasoner = new Reasoner(ontology);
	    //OWLParser parser = new OWLParser(manager, ontology, reasoner /*,factory, inputFile*/);
	    OWLParser parser = new OWLParser(ontology /*,factory, inputFile*/);
    
		
    	//Get the complete NodeList with all the initialized variables
    	List<String> elements = parser.createAndGetOWLClass();
    	List<OWLClass> classElements = parser.getClassElements();
    	AtomicNodeList list1 = new AtomicNodeList(elements, classElements);
    	System.out.println("The complete nodelist has beed generated."); 
    	
    	WriteFile Wfile = new WriteFile(outputFile);
    	//WriteFileStream Wfile = new WriteFileStream(outputFile);
    	//FileDescriptor descriptor = Wfile.getfstream().getFD();
    	
    	Wfile.writeFileInfo(inputFile); // inputfile name
        Wfile.writeFileInfo(numThread);                     //number of thread
    	Wfile.writeFileInfo(list1.getElementsNumber());     //number of concepts
    	Wfile.writeFileInfo(list1.getTotalNumofPossible());//number of possible list
    	System.out.println("The total number of possible node is " + list1.getTotalNumofPossible());
    	//time.stop();
    	
    	
    	//------PreComputing using Own Parser
    	//time.start();
    	//sequential precomputing
    	//precomputeSequential(parser, manager, reasoner, list1, Wfile);
    	//time.stop();
    	//Wfile.writeFileInfo(time.getDuration(), true);//record precomputing duration time for sequential
    	
    	time.start();
    	//parallel precomputing
    	//precomputeParallel(parser, manager, reasoner, numThread, list1);
    	ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numThread);

    	//do{
    		ArrayList<OWLClassAxiom> axioms1, axioms2, axioms3, axioms4;
    		ArrayList<ArrayList<OWLClassAxiom>> groups = null;
    		Future <ArrayList<OWLClassAxiom>> futurecall = null;
    	
    		futurecall =executor.submit(new CallTask1(parser, 1));
    		axioms1 = futurecall.get();
    		numSuperClass = axioms1.size();
    		if(numSuperClass != 0)
    		{
    			groups = list1.divideAxioms(numThread, axioms1);
    			int indexThread1 = 0;
    			while(indexThread1<list1.getThreadNumforAxioms(numThread, axioms1))
    			{
    				executor.submit(new RunTask(reasoner, manager, list1, groups.get(indexThread1), 1));

    				indexThread1++;
    			}
    		}
    		//executor.shutdown(); 
    		
    		//axioms = parser.getSubClassAxiom();
    		futurecall =executor.submit(new CallTask1(parser, 2));
    		axioms2 = futurecall.get();
    		numSubClass = axioms2.size();
    		if(numSubClass != 0)
    		{
    			groups = list1.divideAxioms(numThread, axioms2);
    			//ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numThread);
    			int indexThread2 = 0;
    			while(indexThread2<list1.getThreadNumforAxioms(numThread, axioms2))
    			{
    				executor.submit(new RunTask(reasoner, manager, list1, groups.get(indexThread2), 2));

    				indexThread2++;
    			}
    		}
    		//executor.shutdown(); 

    		System.out.println("##############SubClass done.");

    		
    		//axioms = parser.getEquivalentClassAxiom();
    		futurecall =executor.submit(new CallTask1(parser, 3));
    		axioms3 = futurecall.get();
    		numEquivalent = axioms3.size();
    		if(numEquivalent != 0)
    		{
    			groups = list1.divideAxioms(numThread, axioms3);
    			//ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numThread);
    			int indexThread3 = 0;
    			while(indexThread3<list1.getThreadNumforAxioms(numThread, axioms3))
    			{
    				executor.submit(new RunTask(reasoner, manager, list1, groups.get(indexThread3), 3));

    				indexThread3++;
    			}
    		}
    		System.out.println("##############Equivalent done.");

    		//executor.shutdown(); 
    		
    		//axioms = parser.getDisjointClassAxiom();
    		futurecall =executor.submit(new CallTask1(parser, 4));
    		axioms4 = futurecall.get();    		
    		numDisjoint = axioms4.size();
    		if(numDisjoint != 0)
    		{
    			groups = list1.divideAxioms(numThread, axioms4);
    			//ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numThread);
    			int indexThread4 = 0;
    			while(indexThread4<list1.getThreadNumforAxioms(numThread, axioms4))
    			{
    				executor.submit(new RunTask(reasoner, manager, list1, groups.get(indexThread4), 4));

    				indexThread4++;
    			}
    		}
    		System.out.println("##############Disjoint done.");

///?????????????????  figure out how to shut them down before going on!!
    		executor.shutdown(); 
    		while(true)
			{
				if(executor.isTerminated())
					break;
			}
    	
    	/*time.stop();
    	Wfile.writeFileInfo(time.getDuration(), true);//record precomputing duration time for parallelization
    	
    	Wfile.writeFileInfo(list1.getTotalNumofPossible());//number of possible list after precomputing
    	Wfile.writeFileInfo(numSubClass);
    	Wfile.writeFileInfo(numEquivalent);
    	Wfile.writeFileInfo(numDisjoint);*/
    	time.stop();
    	Wfile.writeFileInfo(time.getDuration(), true);
    	//Precomputing Done
		System.out.println("##############Precomputing tests have been done.");
    	reasoner.dispose();
        
    	Wfile.writeFileInfo(list1.getTotalNumofPossible());//number of possible list
    	System.out.println("The total number of possible node is " + list1.getTotalNumofPossible());

    	
    	//if(numThread[i]<=0)
    	//if(numThread <=0)
		//{
			//System.out.println("Please input a positive integer for the number of thread.");
    		//return;//Add special conditions here.
		//}
    	    
    	//NNF
    	
	    /*
    	//Parallel Part
		
		// printGroups(groups);
    	int step = 0;//list1.getThreadNum(numThread)/10;//Indicate how many times of tests
		//numThread = list1.getThreadNum(numThread, elements.size());
        //Wfile.writeFileInfo(numThread);                     //number of thread

	
    	//Random Test
		//Time time = new Time();
		List<Long> arrTime = new ArrayList<Long>();
		
		while(step>0 && list1.getTotalNumofPossible() != 0)
		{
	    	RandomDivision randomTest = new RandomDivision(elements, classElements);

	    	ArrayList<ArrayList<Integer>> groups1 = randomTest.divideGroup(numThread);
	    	int indexThread = 0;
	    	//ExecutorService executor = Executors.newFixedThreadPool(numThread);
	    	ThreadPoolExecutor executor1 = (ThreadPoolExecutor)Executors.newFixedThreadPool(numThread);
	    	//executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numThread);
	    	
	    	do {
	    		//time.start();
				//OWLClass reasoner = executorReasoner.submit(new RunReasoner(indexThread, inputFile));
	    		//executor.execute(new RunTask(indexThread, groups.get(indexThread), list1, inputFile, outputFile));
				//timer = executor.submit(new CallTask(indexThread, groups.get(indexThread), list1, inputFile));
				//futures.add(
				executor1.submit(new CallTask(indexThread, groups1.get(indexThread), list1, inputFile, arrTime));

			    indexThread++;
			    
			} while (indexThread < numThread);		
			
			executor1.shutdown(); 
			
			while(true)
			{
				if(executor1.isTerminated())
					break;
			}
			//time.stop();
			
			//for(int i=0; i<futures.size(); i++)
			//{
				//if(timer < futures.get(i).get())
					//timer = futures.get(i).get();
			//}
			
			//totaltime += timer;
			step--;
			//Wfile.writeFileInfo(time.getDuration(), true);
			//Wfile.writeFileInfo(list1.getTotalNumofPossible()); // current number of possible list
			System.out.println("The current number of possible node is " + list1.getTotalNumofPossible());
		}

		//System.out.println("##############Random tests have been done.");
			
		
	    //Wfile.writeFileInfo(numThread);                     //number of thread
	    
		while( list1.getTotalNumofPossible() != 0 )
		{
	    	SubDivision subTest = new SubDivision(elements, classElements);
	    	numThread = subTest.getThreadNum(numThread,list1.getTotalNumofPossible());
	    	//Wfile.writeFileInfo(numThread);                     //number of thread

	    	ArrayList<ArrayList<Integer>> groups2 = subTest.subDivideGroup(numThread);
	    	//ArrayList<ArrayList<Integer>> groups = subTest.subRedivideGroup(numThread);
	    	int indexThread = 0;
	    	//ExecutorService executor = Executors.newFixedThreadPool(numThread);
	    	ThreadPoolExecutor executor2 = (ThreadPoolExecutor)Executors.newFixedThreadPool(numThread);
	    	//List<Future<Long>> futures = new ArrayList<Future<Long>>();
	    	//timer = 0;
			do {
				//time.start();
				//RunTask task = new RunTask(indexThread, groups.get(indexThread), list1, inputFile, outputFile);
				//executor.execute(new RunTask(indexThread, groups.get(indexThread), list1, inputFile, outputFile));
				//timer = executor.submit(new CallTask(indexThread, groups.get(indexThread), list1, inputFile, outputFile));
				//futures.add(
				executor2.submit(new CallTask(indexThread, groups2.get(indexThread), list1, inputFile, arrTime));
				//if(timer.get() > time2)
				//{ time2 = timer.get(); }
				
				indexThread++;
				//tag.add(task.getStop());
			} while (indexThread < numThread);
			
			//executor.shutdown(); 
			
			//while(true)
			//{
				//if(executor.isTerminated())
					//break;
			//}
			//time.stop();
			//for(int i=0; i<futures.size(); i++)
			//{
				//if(timer < futures.get(i).get())
					//timer = futures.get(i).get();
			//}
			//totaltime += timer;
			//Wfile.writeFileInfo(time.getDuration(), true);
			//Wfile.writeFileInfo(list1.getTotalNumofPossible()); // current number of possible list
			System.out.println("The current number of possible node is " + list1.getTotalNumofPossible());
			
			executor2.shutdown(); 
			
			while(true)
			{
				if(executor2.isTerminated())
					break;
			}
		}
		Wfile.writeFileInfo(list1.getTotalNumofPossible());
		System.out.println("The current number of possible node is " + list1.getTotalNumofPossible());
		
		//executor.shutdown(); 
		
		//while(true)
		//{
		//	if(executor.isTerminated())
		//		break;
		//}
		
		
		
		//System.out.println("list1 remains possible nodes");		
		//list1.printPossibleNodes();
		//list1.printKnownNodes();
		//list1.printImpossibleNodes();
		//System.out.println("The final counter is " + list1.getCounter());
		
		//num = list1.getTotalNumofPossible();


	    System.out.println("########All the tests has been done.");
	    //System.out.println("----------The total number of possible nodes is " + num);
	      
        //time.stop();
        
        //String info2 = time.printTime(totaltime);  
        //Wfile = new WriteFile(outputFile, indexOntology++);
        //Wfile.writeFileInfo(info2, numThread, list1.getCounterNumofTests());
	 
	    
	    Wfile.writeFileInfo(list1.getCounterNumofTests()); // number of subsumption tests
	    
	    //Wfile.writeFileInfo("\n");
	    time.setTimeArray(arrTime);
		Wfile.writeFileInfo(time.calculateDeviation(), true);
		Wfile.writeFileInfo(time.calculateMax(), true);
		Wfile.writeFileInfo(time.calculateMin(), true);
		Wfile.writeFileInfo(time.calculateMedian(), true);
		Wfile.writeFileInfo(time.calculateAverage(), true);
		//oFile.writeFileInfo("*********");
		//Wfile.writeFileInfo("\n");
	    
        //Wfile.closeFile();
		System.out.println(list1.getCounterNumofTests());
  
    	//}*/
		
        Wfile.closeFile();
        System.out.println("Done!!!");
    }
}

