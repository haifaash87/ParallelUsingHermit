
package ParalleClassification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
//import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;


public class PreComputing {
    //loading all the axioms
	
	public ArrayList<OWLAnnotationAssertionAxiom> getAllAnnotationAxiom (OWLOntology ontology, OWLClass cls, OWLDataFactory factory)
	{
		ArrayList<OWLAnnotationAssertionAxiom> axioms = new ArrayList<OWLAnnotationAssertionAxiom>();
		for(OWLAnnotation annotation : cls.getAnnotations(ontology))
		{
			axioms.add(factory.getOWLAnnotationAssertionAxiom(cls.getIRI(), annotation));
		}
		return axioms;
	}
	
	public ArrayList<OWLClass> getClassesFromExpression (OWLClass owlClass, OWLOntology ontology)
	{
		ArrayList <OWLClass> listOfOWLClasses = new ArrayList<OWLClass>();
		
		for(OWLSubClassOfAxiom axiom : ontology.getSubClassAxiomsForSubClass(owlClass))
		{
			OWLClassExpression expression = axiom.getSuperClass();
			
			if(expression.isAnonymous())
			{
				listOfOWLClasses.addAll(expression.getClassesInSignature());
				System.out.println(expression.getClassesInSignature());
			}
		}
		return listOfOWLClasses;
	}
	
	/*public ArrayList<ArrayList<OWLAxiom>> divideAxiomsGroups(ArrayList<OWLAnnotationAssertionAxiom> axioms, int thread)
	{
		//ArrayList<OWLAxiom> axiomGroup = new ArrayList<OWLAxiom>();
		ArrayList<ArrayList<OWLAxiom>> axiomGroups = new ArrayList<ArrayList<OWLAxiom>>();

		int size = axioms.size();
		int groupSize = size/thread +1;
		
		for(int i=0; i<groupSize & i<size; i++)
		{
			ArrayList<OWLAxiom> group = new ArrayList<OWLAxiom>();
			for(int j=0; j< groupSize; j++)
			{
				
			}
			
		}
		
		
		return axiomGroups;
		
	}*/
	
	//reading all the axioms to find existing 
	void readAxioms()
	{
		//if(isNonSyntacticOWL11Axiom() == getAxiomType()){}
		//if(isLogicAxiom == getAxiomType())
	}
}
