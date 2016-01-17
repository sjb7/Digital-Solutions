import java.util.*;
import java.io.*;
import java.net.*;

public class tvSeries implements Runnable {

	private String tvSeriesName;

	public tvSeries(String tvSeriesName) {
		this.tvSeriesName = tvSeriesName;
	}

	/*Helper function for giveStartingIndex and giveLastIndex*/

	private int giveIndex(String text,String searchString,int start,int startOrLast) {
		for(int i = start; i<text.length()-searchString.length(); i++) {
			int k=0;
			for(int j=i,b=0;j<i+searchString.length();j++,b++){
				if(text.charAt(j) != searchString.charAt(b)) {
					k=1;
					break;
				}
			}
			if(k==0){
				if(startOrLast==0)
					return i;
				else { return i+searchString.length(); }
			}
		}
		return Integer.MAX_VALUE;
	}

	/* @param The text, string to search in the text, the index from which to start searching
	   @return It gives the starting index of first occurence of given String found in text */

	private int giveStartingIndex(String text,String searchString,int start){
		return giveIndex(text,searchString,start,0);
	}

	/* @param The text, string to search in the text, the index from which to start searching
	   @return It gives the last+1 index of first occurence of given String found in text */

	private int giveLastIndex(String text,String searchString,int start){
		return giveIndex(text,searchString,start,1);
	}


	/* @param Takes the text and the label 
	   @return The text between <label> and </label> */

	private String stringBetweenClosedLabel(String text,String label) throws labelNotFoundException {
		int StartOfLabel = giveStartingIndex(text,"<"+label,0);
		if(StartOfLabel==Integer.MAX_VALUE)
			throw new labelNotFoundException();
		int EndOfLabel = giveStartingIndex(text,"</" + label,StartOfLabel);
		if(EndOfLabel==Integer.MAX_VALUE)
			throw new labelNotFoundException();
		return text.substring(StartOfLabel,EndOfLabel);
	}

	/* @param The text which is between the label (table) 
	 * @return The extracted value for each tv show stored in Map */ 

	private Map<Integer,List<String>> extractTvShows(String text) {
		int start = 0,end = 0,count = 0;
		Map<Integer,List<String>> everyShowDetail = new HashMap<>(); 
		while(end < text.length() && start < text.length()){
			List<String> details = new ArrayList<>();
			start = giveStartingIndex(text,"result_text",end);
			end = giveStartingIndex(text,"</td",start);
			if(start!=Integer.MAX_VALUE && end!=Integer.MAX_VALUE) {
				int linkStart = giveLastIndex(text,"href=\"",start);
				int linkEnd = giveStartingIndex(text,"\"",linkStart);
				details.add(text.substring(linkStart,linkEnd));
				int nameEnd = giveStartingIndex(text,")",linkEnd);
				details.add(text.substring(linkEnd+3,nameEnd+1).replace("</a>",""));
				everyShowDetail.put(++count,details);
			}
		}
		return everyShowDetail;
	}

	private void PrintMap(Map<Integer,List<String>> givenMap) {
		for( Map.Entry<Integer,List<String>> entry : givenMap.entrySet() ) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			System.out.println(key + " " + value);
		}
	}

	/* @param The name of the tv series given as a input by the user
	 * @return The search page of imdb for the input */

	private String getImdbPage(String searchItem) {
		URL searchURL;
		InputStream is = null;
		BufferedReader bf;
		String line;
		StringBuilder sb = new StringBuilder();
		try {
			searchURL = new URL("http://www.imdb.com/find?ref_=nv_sr_fn&q=" + searchItem + "&s=all");
			is = searchURL.openStream();
			bf = new BufferedReader(new InputStreamReader(is));
			while((line=bf.readLine())!=null){
				sb = sb.append(line);
			}		
		}
		catch(MalformedURLException mue) {
			mue.printStackTrace();
		}
		catch(IOException ie) {
			ie.printStackTrace();
		}
		finally {
			try {  
				if(is!=null) { is.close(); }
			}
			catch(IOException ie) { /*Do Nothing Here*/ }
		}
		return sb.toString();	
	}

	public void run() {
		try {
			PrintMap(extractTvShows(stringBetweenClosedLabel(getImdbPage(tvSeriesName),"table")));
		}
		catch(Exception e){
			
		}
	}

	public static void main(String[] args) {
		Thread first = new Thread(new tvSeries("Fargo"));
		first.start();
	}
}

class labelNotFoundException extends Exception {
	public labelNotFoundException() { super("The given label was not found"); } 
}