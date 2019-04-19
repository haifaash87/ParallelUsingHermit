package ParalleClassification;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {

	 private File file;
	 private String strFilePath;
	 private BufferedReader br = null;
	 private String strXmlLine = "";
	 private String strXmlInfo = "";
	 //private int index;
	 //private List<List<Integer>> list;
	 
	 public ReadFile(String path, int index)
	 {
		 this.strFilePath = path;
		 //this.index = index;
		 //file = new File(path);
	 }

	 public void getFileInfo()
	 {	 
		 try{
			 file = new File(strFilePath);
			 br = new BufferedReader(new FileReader(file));
			 while ((strXmlLine = br.readLine()) != null)
			 {
				 strXmlInfo += strXmlLine;
				 strXmlInfo += '\n';
			 }
			 //br.close();
		 } catch (FileNotFoundException e)
		 {
			 e.printStackTrace();
		 }catch (IOException e)
		 {
			 e.printStackTrace();
		 }
		 
		 System.out.print(strXmlInfo);
	 }
	 

	 
	 public void closeFile() 
	 {
		 try{
		     br.close();
		 }
		 catch (IOException e)
		 {
			 e.printStackTrace();
		 }
	 }
}
