package ParalleClassification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.semanticweb.owlapi.model.AddAxiom;


public class Time {

	private long startTime;
    private long endTime;
    private String value;
    private List<Long> arrTime = new ArrayList<Long>();

    public long getDuration() {
        return endTime - startTime;
    }
   
    public void start() {
        startTime = System.currentTimeMillis();
    }
    public void stop() {
         endTime = System.currentTimeMillis();
     }

    public void add(Long time)
    {
    	arrTime.add(time);
    }
    
    public List<Long> getTimeArray()
    {
    	return arrTime;
    }
    
    public void setTimeArray(List<Long> atime)
    {
    	arrTime = atime;
    }
    
    public double calculateDeviation()
    {
    	double sum = 0.0;
    	double deviation = 0.0;

    	for(double num : arrTime)
    	{
    		sum += num;
    	}
    	
    	double mean = sum/arrTime.size();
    	
    	for(double num: arrTime)
    	{
    		deviation += Math.pow(num-mean, 2);
    	}
    	System.out.println("The Deviation time is: " + Math.sqrt(deviation/arrTime.size()));
    	return Math.sqrt(deviation/arrTime.size());
    }
    
    public double calculateMax()
    {
    	double max = arrTime.get(0);
    	
    	for(int i=0; i<arrTime.size(); i++)
    	{
    		if(arrTime.get(i)>max)
    			max = arrTime.get(i);
    	}
    	System.out.println("The Max time is: " + max);
    	return max;
    }
    
    public double calculateMin()
    {
    	double min = arrTime.get(0);
    	
    	for(int i=0; i<arrTime.size(); i++)
    	{
    		if(arrTime.get(i) < min)
    			min = arrTime.get(i);
    	}
    	System.out.println("The Min time is: " + min);
    	return min;
    }
    
    
    public double calculateAverage()
    {
    	double sum = 0.0;
    	double average = 0.0;
    	
    	for(double num : arrTime)
    	{
    		sum =+ num;
    	}
    	average = sum/arrTime.size();
    	System.out.println("The Average time is: " + average);
    	return average;
    }
    
    public double calculateMedian()
    {
    	Arrays.sort(arrTime.toArray());
    	int middle = arrTime.size()/2;
    	double medianValue = 0.0;
    	if(arrTime.size()%2 == 1)
    		medianValue = arrTime.get(middle);
    	else
    		medianValue = (arrTime.get(middle-1) + arrTime.get(middle))/2;
    	
    	System.out.println("The Median time is: " + medianValue);
    	return medianValue;
    }
    
    public String printTime(long totaltime)
    {
    	value = "Total parallel execution time: " + totaltime + "ms";
   	    System.out.println(value);
        return value;
    }
    
    public String printTime1()
    {
    	 final long duration = getDuration();
    	 value = "Total sequential execution time: " + duration + "ms";
    	 System.out.println(value);
         return value;
    }
  
    public String printTime2()
    {
    	 final long duration = getDuration();
         value = "Total parallel execution time: " + duration + "ms";
         System.out.println(value);
         return value;
    }
    
}
