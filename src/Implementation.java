import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Implementation {
	public static void main(String[] args) {
		System.out.println();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter the name of the tv Series : ");
		String tvSeriesName = null;
		while(true) {
			try {
				tvSeriesName = br.readLine();
			}
			catch(IOException e) {}
			if(tvSeriesName!=null && tvSeriesName.length()>0)
				break;
			else System.out.println("\nEnter valid name");
		}
		System.out.println();
		final ExecutorService service;
		final Future<Map<Integer,List<String>>> task;
		service = Executors.newFixedThreadPool(1);
		task = service.submit(new tvSeries(tvSeriesName));
		Map<Integer,List<String>> taskOutput = null;
		try {
			 taskOutput = task.get();
		}
		catch(Exception ie){ }
		UtilityClass.PrintMap(taskOutput);
		service.shutdownNow();
		System.out.println("\nEnter the Index number of the tvSeries you want to add");
		int key = 0;
		try {
			key = Integer.parseInt(br.readLine());
		} catch(Exception e) {}
		FileOperation fop = new FileOperation("storedSeries.txt");
		fop.AddTheTvSeries(taskOutput.get(key));
	}
}