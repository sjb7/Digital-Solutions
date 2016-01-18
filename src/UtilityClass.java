import java.util.*;
import java.io.*;

public class UtilityClass {

	/*Helper function for giveStartingIndex and giveLastIndex*/

	private static int giveIndex(String text,String searchString,int start,int startOrLast) {
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

	public static int giveStartingIndex(String text,String searchString,int start){
		return giveIndex(text,searchString,start,0);
	}

	/* @param The text, string to search in the text, the index from which to start searching
	   @return It gives the last+1 index of first occurence of given String found in text */

	public static int giveLastIndex(String text,String searchString,int start){
		return giveIndex(text,searchString,start,1);
	}


	/* @param Takes the text and the label 
	   @return The text between <label> and </label> */

	public static String stringBetweenClosedLabel(String text,String label) throws labelNotFoundException {
		int StartOfLabel = giveStartingIndex(text,"<"+label,0);
		if(StartOfLabel==Integer.MAX_VALUE)
			throw new labelNotFoundException();
		int EndOfLabel = giveStartingIndex(text,"</" + label,StartOfLabel);
		if(EndOfLabel==Integer.MAX_VALUE)
			throw new labelNotFoundException();
		return text.substring(StartOfLabel,EndOfLabel);
	}

	/* @param Takes HashMap 
	   Prints HashMap in console format */

	public static void PrintMap(Map<Integer,List<String>> givenMap) {
		for( Map.Entry<Integer,List<String>> entry : givenMap.entrySet() ) {
			String key = entry.getKey().toString();
			List<String> value = entry.getValue();
			System.out.print(key+".");
			int space = 3 - key.length();
			while(space-->0)
				System.out.print(" ");
			System.out.println(value.get(1).trim());
		}
	}
}