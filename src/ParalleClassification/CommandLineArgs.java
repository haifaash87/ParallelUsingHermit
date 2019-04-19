package ParalleClassification;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class CommandLineArgs {

	 private final CmdLineParser cliParser = new CmdLineParser(this);

	 //private final OWLDataFactory dataFactory = OWLManager.createOWLOntologyManager().getOWLDataFactory();

	 //private final PrefixManager prefixManager = new DefaultPrefixManager();

	 @Option(name = "-i", usage = "input the path file of an otology")
	 private String inputFile;

	 @Option(name = "-w", usage = "number of threads for classification")
	 private int numthread;

	 @Option(name = "-o", usage = "output of the path file.")
	 private String outputFile;

	 //@Getter
	 @Option(name = "-maxTime", usage = "maximum execution time in miliseconds")
	 private long maxTime;


	 public CommandLineArgs(/*@NonNull*/ String[] args) throws CmdLineException {
	   cliParser.parseArgument(args);
	   //if()
	   if (inputFile == null) throw new IllegalArgumentException("You must specify an ontology file path");
	 }

	 public int NumThread() {
	   if (numthread < 0) {
	     return 1;
	   } 
	   else return numthread;
	 }


	 public String getOntologyFilePath() {
		 return inputFile;
	 }
	 
	 public String getOutputFilePath(){
		 return outputFile;
	 }
	 
}
