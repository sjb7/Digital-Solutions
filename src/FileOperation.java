import java.io.*;
import java.util.*;

public class FileOperation {
	String fileName;
	FileWriter fw = null;

	public FileOperation(String fileName) {
		this.fileName = fileName;
	}

	public void AddTheTvSeries(List<String> values) {
		try {
			//if(fw==null) {
			fw  = new FileWriter(fileName,true);
			fw.write(values.get(0) + " " + values.get(1)+"\n");
		}
		catch(IOException ioe) { ioe.printStackTrace(); }
		finally { try { if(fw!=null) {fw.close();} } catch(IOException ie){} }
	}

}