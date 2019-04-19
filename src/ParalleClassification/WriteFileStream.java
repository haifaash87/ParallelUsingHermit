package ParalleClassification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class WriteFileStream {
	 private File file;
	 private FileOutputStream fstream = null;
 	 //private FileDescriptor descriptor = null;

	 //private String strFilePath;
	 //private String inputPath;
	 private BufferedWriter bw = null;
	 //int num; // initial number of possible nodes
	 //private int index;
	 //private List<List<Integer>> list;
	 
	 public  WriteFileStream (String outputPath) throws IOException
	 {
		 //this.strFilePath = outputPath;
		 //this.inputPath = inputPath;
		 //this.num = num;
		 //this.index = index;
		 //file = new File(path);
		 this.file = new File(outputPath);
		 this.fstream = new FileOutputStream(file);
		 this.bw = new BufferedWriter(new OutputStreamWriter(fstream));
		 bw.write("\n");
		 System.out.println("The file has been created!!!");
	 }
	 
	 public FileOutputStream getfstream()
	 {
		 return fstream;
	 }
	 
	 public void writeFileInfo(int d) throws IOException
	 {
		//bw.write("****************************\n");
		bw.write(d + " ");	
		//fstream.write(d+' ');
	 }
	 
	 public void writeFileInfo(double num, boolean ms) throws IOException
	 {
		//bw.write("****************************\n");
		 if(ms)
		 {
			 bw.write(num + "ms ");	
			 //fstream.write(num);
		 }
		 else {
			 bw.write( (int) num );
		}
	 }
	 
	 public void writeFileInfo(String info) throws IOException
	 {
		//bw.write("****************************\n");
		bw.write(info + " ");
		//bw.write("\n");
	 }
	 
	 public void writeFileInfo(int numThread,int numSubTests) throws IOException
	 {
		bw.write("The number of threads is " + numThread + "\n");
		bw.write("The total number of subsumption tests is " + numSubTests);
		bw.write("\n");
		bw.write("****************************\n");
		//bw.close();
	 }
	 
	 public void writeFileInfo(String info, int numThread, int numSubTests) throws IOException
	 {
		bw.write("The number of threads is " + numThread + "\n");
		bw.write(info);
		bw.write("The total number of subsumption tests are " + numSubTests);
		bw.write("\n");
		bw.write("****************************\n");
	 }
	 

	 public void writeFileInfo(String outputPath, String inputPath, int num, int nodeSize) throws IOException
	 {
		bw.write("****************************\n");
		bw.write("The input OWL file path is: " + inputPath + "\n");
        bw.write("The output file path is: " + outputPath + "\n");
        bw.write("The total number of nodes: " + nodeSize + "\n");
        bw.write("The total number of initial possible nodes is " + num + "\n");
		bw.write("\n");
		bw.close();
	 }
	 public void closeFile() throws IOException
	 {
		 bw.close();
	 }
}
