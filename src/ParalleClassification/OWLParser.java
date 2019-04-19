package ParalleClassification;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.core.SubstringMatcher;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
//import org.semanticweb.HermiT.Reasoner;
//import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
//import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.UnknownOWLOntologyException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
//import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
//import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
//import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
//import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
//import org.semanticweb.owlapi.util.InferredAxiomGenerator;
//import org.semanticweb.owlapi.util.InferredDisjointClassesAxiomGenerator;
//import org.semanticweb.owlapi.util.InferredOntologyGenerator;
//import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
//import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;

import com.sun.org.apache.bcel.internal.generic.RETURN;




public class OWLParser {
	//private static final String PIZZA_IRI = "http://www.semanticweb.org/quanzixi/ontologies/2016/5/pizza.owl#";
	private File file = null;
			//new File("/Users/Zixi/Dropbox/Research/HermiTParallelVersion1/project/examples/ontologies/pizza.owl");
	private OWLOntologyManager manager = null;
	private OWLOntology ontology = null;
	private OWLReasoner reasoner = null;
	private List<OWLClass> classElements = new ArrayList<OWLClass>();
	private OWLDataFactory factory = null;
	private String inputFile;
	//private List<OWLDisjointClassesAxiom> result = new ArrayList<OWLDisjointClassesAxiom>();

	
	public OWLParser (String input)
	{
		this.inputFile = input;
		this.file = new File(inputFile);
	}
	
	public OWLParser(OWLOntology ontology /*,OWLDataFactory dataFactory, String input*/)
	{
		//this.manager = manager;
		this.ontology = ontology;
		//this.reasoner = reasoner;
		//OWLParser.file = file;
		//this.inputFile = input;
		//this.factory = dataFactory;
	}
	
	public OWLParser(OWLOntologyManager manager, OWLOntology ontology, OWLReasoner reasoner /*,OWLDataFactory dataFactory, String input*/)
	{
		this.manager = manager;
		this.ontology = ontology;
		this.reasoner = reasoner;
		//OWLParser.file = file;
		//this.inputFile = input;
		//this.factory = dataFactory;
	}
	
	/*public void loadOntology() throws OWLOntologyCreationException 
	{
		manager = OWLManager.createOWLOntologyManager();
		//IRI iri = IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");

    	File file = new File("/Users/Zixi/Dropbox/Research/HermiTParallelVersion1/project/examples/ontologies/ProjectNov30.owl");

		ontology = manager.loadOntologyFromOntologyDocument(file);
		System.out.println("Loaded Ontology: " + ontology);

		IRI documentIRI = manager.getOntologyDocumentIRI(ontology);
		System.out.println("From: " + documentIRI);

		reasoner = new Reasoner( ontology );
		//manager.removeOntology(pizzaOntology);
	}*/


	/*public File useReasoner() throws OWLOntologyCreationException, IOException, UnknownOWLOntologyException, OWLOntologyStorageException 
	{ 
		//IRI docIRI = IRI.create(file);

		//manager = OWLManager.createOWLOntologyManager();
		
		//ontology = manager.loadOntologyFromOntologyDocument(docIRI);
		//System.out.println("Loaded: " + ontology.getOntologyID());

		// Use Hermit Reasoner
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		//dataFactory = manager.getOWLDataFactory();
	
		//ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		//OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		reasoner = reasonerFactory.createReasoner(ontology);
		//Ask the reasoner to do all the necessary work now
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		reasoner.precomputeInferences(InferenceType.DISJOINT_CLASSES);
		reasoner.precomputeInferences(InferenceType.SAME_INDIVIDUAL);
		List<InferredAxiomGenerator<? extends OWLAxiom>> generators = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
        generators.add(new InferredSubClassAxiomGenerator());
        generators.add(new InferredEquivalentClassAxiomGenerator());
        //generators.add(new InferredDisjointClassesAxiomGenerator() {
            boolean precomputed=false;
            protected void addAxioms(OWLClass entity, OWLReasoner reasoner, OWLDataFactory dataFactory, Set<OWLDisjointClassesAxiom> result) 
            {
                if (!precomputed) {
                    reasoner.precomputeInferences(InferenceType.DISJOINT_CLASSES);
                    precomputed=true;
                }
                for (OWLClass cls : reasoner.getDisjointClasses(entity).getFlattened()) {
                    result.add(dataFactory.getOWLDisjointClassesAxiom(entity, cls));
                }
            }
        //});
        //create an instance of InferredOntologyGenerator
        InferredOntologyGenerator iog=new InferredOntologyGenerator(reasoner,generators);
        
        OWLOntology inferredAxiomsOntology=manager.createOntology();
        iog.fillOntology(manager, inferredAxiomsOntology);
        
        //String prefix = ontology.getOntologyID().getOntologyIRI().toString();
        file = new File("examples/ontologies/inferred.owl");
        if (!file.exists())
            file.createNewFile();
        else
        	file.delete();
        file = file.getAbsoluteFile();
        // Now we create a stream since the ontology manager can then write to that stream. 
        OutputStream outputStream=new FileOutputStream(file);
        // We use the same format as for the input ontology.
        manager.saveOntology(inferredAxiomsOntology, manager.getOntologyFormat(ontology), outputStream);
	
        return file;
		// Determine if the ontology is actually consistent (in this case, it should be).
		//boolean consistent = reasoner.isConsistent();
		//System.out.println("Consistent: " + consistent);
		//System.out.println("\n");
        
		//"http://www.test.pl/ontA.owl#");
		//DefaultPrefixManager pmA = new DefaultPrefixManager(ontology.getOntologyID().getOntologyIRI() + "#");
		
		//Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses();
		//Set<OWLClass> unsatisfiable = bottomNode.getEntitiesMinusBottom();
		//if (!unsatisfiable.isEmpty()) 
		//{
			//System.out.println("The following classes are unsatisfiable: ");
			//for (OWLClass cls : unsatisfiable) {
				//System.out.println("  " + cls);
			//}
		//} 
		//else
			//System.out.println("There are no unsatisfiable classes");
		//System.out.println("\n");

		//Set<OWLClass> subClses = reasoner.getSubClasses(thing, true);
		//List<String> elements = new ArrayList<String>();

		//OWLDataFactory fac = manager.getOWLDataFactory();
		//OWLClass thing = fac.getOWLClass(IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl#thing"));
		//NodeSet<OWLClass> subClses = reasoner.getSubClasses(thing, true);
		//Set<OWLClass> clses = subClses.getFlattened();
		//System.out.println("Subclasses of thing: ");
		//for (OWLClass cls : clses) {
			//System.out.println("   " + cls);
		//}
		//System.out.println("\n");
		
		//Node<OWLClass> topNode = reasoner.getTopClassNode();
		//print(topNode, reasoner, 0, pmA);
	}*/
	 
	
	/*private void print(Node<OWLClass> parent, OWLReasoner reasoner, int depth, DefaultPrefixManager pm) 
	{
		if (parent.isBottomNode()) 
		{
			return;
		}
		// Print an indent to denote parent-child relationships
		printIndent(depth);
		// print the node (containing the child classes)
		printNode(parent, pm);
		for (Node<OWLClass> child : reasoner.getSubClasses(parent.getRepresentativeElement(), true)) 
		{
			print(child, reasoner, depth + 1, pm);
		}
	}*/

	
	/*private void printIndent(int depth) 
	{
		for (int i = 0; i < depth; i++) {
			System.out.println("******" + i);
		}
	}*/
	
	
	/*private void printNode(Node<OWLClass> parent, DefaultPrefixManager pm) 
	{
		//DefaultPrefixManager pm = new DefaultPrefixManager("http://www.co-ode.org/ontologies/pizza/pizza.owl#");
		// print out a node as a list of class names in curly bracket
		System.out.print("{");
		for (Iterator<OWLClass> iterator = parent.getEntities().iterator(); iterator.hasNext();) 
		{
			OWLClass cls = iterator.next();
			// Use prefixManager to provide a slightly nicer shorter name
			System.out.print(pm.getShortForm(cls));
			if (iterator.hasNext()) {
				System.out.print("  ");
			}
		}
		System.out.println("}");
	}*/
	
	public List<OWLClass> getClassElements()
	{
		return classElements;
	}
	
	private static String clname(OWLClassExpression cl) 
	{
		return cl.asOWLClass().toString()/*.getIRI().getFragment()*/;
	}

	public List<String> createAndGetOWLClass() throws OWLOntologyCreationException 
	{
		List<String> elements =new ArrayList<String>();
		
		for (OWLClass cls : ontology.getClassesInSignature()) 
		{
			classElements.add(cls.asOWLClass());
			elements.add(clname(cls));
		}
		
		
		return elements;
	}
	
	
	public ArrayList<OWLAnnotationAssertionAxiom> getAllAnnotationAxiom ()
	{
		ArrayList<OWLAnnotationAssertionAxiom> axioms = new ArrayList<OWLAnnotationAssertionAxiom>();
		for(int i=0; i<classElements.size(); i++)
		{
			OWLClass cls = classElements.get(i);
			for(OWLAnnotation annotation : cls.getAnnotations(ontology))
			{
				axioms.add(factory.getOWLAnnotationAssertionAxiom(cls.getIRI(), annotation));
				System.out.println(annotation);
			}
		}
		return axioms;
	}
	
	/*public ArrayList<OWLClass> getAllSubClassAxiom ()
	{
		ArrayList<OWLClass> axioms = new ArrayList<OWLClass>();
		for(int i=0; i<classElements.size(); i++)
		{
			OWLClass cls = classElements.get(i);
			//axioms.addAll(ontology.getSubClassAxiomsForSubClass(cls));
			//System.out.println(cls.getAnnotations(ontology));
			for(OWLSubClassOfAxiom axiom : ontology.getSubClassAxiomsForSubClass(cls))
			{
				OWLClassExpression expression = axiom.getSuperClass();
				
				if(expression.isAnonymous())
				{
					axioms.addAll(expression.getClassesInSignature());
					System.out.println(expression.getClassesInSignature());
				}
			}
		}
		return axioms;
	}*/
	
	public OWLClass getTopClass()
	{
		OWLClass top = factory.getOWLThing();
		System.out.println(top);
		
		return top;
	}
	
	public OWLClass getBottomClass()
	{
		OWLClass bottom = factory.getOWLNothing();
		System.out.println(bottom);
		
		return bottom;
	}
	
	public ArrayList<OWLClassAxiom> getAllAxiom ()
	{
		ArrayList<OWLClassAxiom> axioms = new ArrayList<OWLClassAxiom>();
		for(int i=0; i<classElements.size(); i++)
		{
			OWLClass cls = classElements.get(i);
			//axioms.addAll(ontology.getSubClassAxiomsForSubClass(cls));
			//System.out.println(cls.getAnnotations(ontology));
				//OWLClassExpression expression = axiom.getSuperClass();
				axioms.addAll(ontology.getAxioms(cls));		
		}
		printAxioms(axioms);
		return axioms;
	}
	
	public ArrayList<OWLClassAxiom> getEquivalentClassAxiom ()
	{
		ArrayList<OWLClassAxiom> axioms = new ArrayList<OWLClassAxiom>();
		for(int i=0; i<classElements.size(); i++)
		{
			OWLClass cls = classElements.get(i);
			axioms.addAll(ontology.getEquivalentClassesAxioms(cls));		
		}
		//printAxioms(axioms);
		return axioms;
	}
	
	public ArrayList<OWLClassAxiom> getSubClassAxiom ()
	{
		ArrayList<OWLClassAxiom> axioms = new ArrayList<OWLClassAxiom>();
		//ArrayList<String> axiom = new ArrayList<String>();
		for(int i=0; i<classElements.size(); i++)
		{
			OWLClass cls = classElements.get(i);
			axioms.addAll(ontology.getSubClassAxiomsForSubClass(cls));		
		}
		//printAxioms(axioms);
		return axioms;
	}

	
	public ArrayList<OWLClassAxiom> getSuperClassAxiom ()
	{
		ArrayList<OWLClassAxiom> axioms = new ArrayList<OWLClassAxiom>();
		for(int i=0; i<classElements.size(); i++)
		{
			OWLClass cls = classElements.get(i);
			axioms.addAll(ontology.getSubClassAxiomsForSuperClass(cls));		
		}
		//printAxioms(axioms);
		return axioms;
	}
	
	public ArrayList<OWLClassAxiom> getDisjointClassAxiom ()
	{
		ArrayList<OWLClassAxiom> axioms = new ArrayList<OWLClassAxiom>();
		for(int i=0; i<classElements.size(); i++)
		{
			OWLClass cls = classElements.get(i);
			axioms.addAll(ontology.getDisjointClassesAxioms(cls));		
		}
		//printAxioms(axioms);
		return axioms;
	}
	
	public void printAxioms(ArrayList<OWLClassAxiom> axioms) {
		for (int i = 0; i < axioms.size(); i++) {
			System.out.println("*******This is Axiom" + i + "********");
				System.out.println(axioms.get(i).toString());
			
		}
	}
	
	
	public void reasonerDispose()
	{
		reasoner.dispose();
	}
	
	public void printGroups(ArrayList<ArrayList<Integer>> groups) {
		for (int i = 0; i < groups.size(); i++) {
			System.out.println("*******This is Group" + i + "********");
			for (int j = 0; j < groups.get(i).size(); j++) {
				System.out.println(groups.get(i).get(j));
			}
		}
	}
	
	
	public void printGroupElements(List<String> groupElements) {
		System.out.println("*****The size of groupElements is:" + groupElements.size() + "*****");
		for (int i = 0; i < groupElements.size(); i++) {
			System.out.println(groupElements.get(i));
		}
	}
	
	
	/*private void markExistingHierarchy(NodeList list, OWLClass d1, OWLClass d2)
	{
		OWLAnnotationProperty label = df.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
		
		String parent = d1.getAnnotations(ontology, annotationProperty);
		String children = null;
		list.addInfoList(parent, children);
	}*/
	    
 
	/*private static void printOntologyAndImports(OWLOntologyManager manager, OWLOntology ontology) 
	{
		System.out.println("Loaded ontology:");
		// Print ontology IRI and where it was loaded from (they will be the same)
		printOntology(manager, ontology);
		// List the imported ontologies
		for (OWLOntology importedOntology : ontology.getImports()) {
			System.out.println("Imports:");
			printOntology(manager, importedOntology);
		}
	}

	private static void printOntology(OWLOntologyManager manager, OWLOntology ontology) {
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI();
		IRI documentIRI = manager.getOntologyDocumentIRI(ontology);
		System.out.println(ontologyIRI == null ? "anonymous" : ontologyIRI.toQuotedString());
		System.out.println("    from " + documentIRI.toQuotedString());
	}*/
}
