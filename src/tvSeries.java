import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class tvSeries implements Callable< Map<Integer,List<String>> > {

	private String tvSeriesName;

	public tvSeries(String tvSeriesName) {
		this.tvSeriesName = tvSeriesName;
	}

	/* @param The text which is between the label (table) 
	 * @return The extracted value for each tv show stored in Map */ 

	private Map<Integer,List<String>> extractTvShows(String text) {
		int start = 0,end = 0,count = 0;
		Map<Integer,List<String>> everyShowDetail = new HashMap<>(); 
		while(end < text.length() && start < text.length()){
			List<String> details = new ArrayList<>();
			start = UtilityClass.giveStartingIndex(text,"result_text",end);
			end = UtilityClass.giveStartingIndex(text,"</td",start);
			if(start!=Integer.MAX_VALUE && end!=Integer.MAX_VALUE) {
				int linkStart = UtilityClass.giveLastIndex(text,"href=\"",start);
				int linkEnd = UtilityClass.giveStartingIndex(text,"\"",linkStart);
				details.add(text.substring(linkStart,linkEnd));
				int nameEnd = UtilityClass.giveStartingIndex(text,")",linkEnd);
				details.add(text.substring(linkEnd+3,nameEnd+1).replace("</a>","").split("<")[0]);
				everyShowDetail.put(++count,details);
			}
		}
		return everyShowDetail;
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
			searchURL = new URL("http://www.imdb.com/find?ref_=nv_sr_fn&q=" + URLEncoder.encode(searchItem,"UTF-8") + "&s=all");
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

	public Map<Integer,List<String>> call() {
		Map<Integer,List<String>> classOutput = null;
		try {
			classOutput = extractTvShows(UtilityClass.stringBetweenClosedLabel(getImdbPage(tvSeriesName),"table"));
		}
		catch(Exception e){
			
		}
		return classOutput;
	}

}