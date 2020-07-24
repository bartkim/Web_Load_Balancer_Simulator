import java.io.*;
import java.util.*;

public class Node
{
	// constructor
	public Node (String name)
	{
		this(name, 0.9f);
	}
	
	public Node (String name, float threshold)
	{
		try {
			loadList = new LinkedList();				// create the list of load using linked list.
			out = new FileOutputStream(name);			// output file stream
			log = new PrintWriter(out, true);			// writer
			
			cpuUsed = memUsed = bwUsed = 0;				// resources initialization
			this.threshold = threshold;
		}
		catch (IOException e)
		{
			System.out.println("Error: " + e);
			System.exit(1);
		}
	}
	                                        
	//-----------
	//  methods
	//-----------
	public void setThreshold (float threshold)
	{
		threshold = threshold;				// set threshold.
	}
	
	public boolean putLoad (Load load)
	{
		if ( isAvailable() ) {
			cpuUsed += load.getCpu();			// resource allocate
			memUsed += load.getMem();
			bwUsed += load.getBw();
		
			loadList.add(load);
			//fileRecord();
			return true;
		}
		else
			return false;
	}
	
	public void removeLoad (int i)			// get rid of load
	{
		Load temp;
		
		temp = (Load) loadList.remove(i);
		cpuUsed -= temp.getCpu();			// disallocation of resources
		memUsed -= temp.getMem();
		bwUsed -= temp.getBw();
		//fileRecord();
	}
	
	public boolean isAvailable()
	{
		return ( performanceRate() <= threshold);
	}
	
	public float performanceRate()
	{
		 return (float)cpuUsed / maxCpu * 0.5f + (float)memUsed / maxMem * 0.3f + (float)bwUsed / maxBw * 0.2f;
	}		
	
	public boolean isExceed ()
	{
		return ( cpuUsed > maxCpu || memUsed > maxMem || bwUsed > maxBw );		// There must be sufficient spaces...
	}
	
	public void failure ()					// print failure message on file and close file output stream
	{
		log.println (" System is Down!!!");
		log.println ("    Time : " + time);
		log.println ("    Threshold : " + threshold);
		log.println ("    Performance Rate : " + performanceRate() );
		
		terminate();
	}
	
	public void complete ()					// print complete message on file and close file output stream
	{
		log.println ("Complete...");
		
		terminate();
	}
	
	public void unitTimePass()
	{
		int i;
		Load temp;
		
		time++;									// time increase
		
		for ( i = 0 ; i < loadList.size() ; i++ ) {		// load's remainder time is reduced.
			temp = (Load) loadList.get(i);
			if ( temp.unitTimePass() == 0 ) {
				removeLoad(i);
				i--;							//
			}
		}
	}
	
	public void fileRecord ()						// time : 4
	{												// Resource Monitoring
		int i;										//		CPU		Memeory		Bandwidth
													//		20%		45Mb		23Mhz
													// Load Monitoring
		log.println ("Time : " + time);				//   name	arrival   CPU   Mem   Bw   Dur   Rem
		log.println ("Resource Monitoring");		//   load1    3       40    33    23    7     3
		log.println ("\tCPU\tMemory\tBandwidth");	//			.........................
		log.println ("\t" + cpuUsed + "%\t" + memUsed + "Mb\t" + bwUsed + "MHz\t");
		log.println ("Performance Rate : " + performanceRate());
		log.println ("Load Monitoring");
		log.println ("  name\tarrival\tCPU\tMem\tBw\tDur\tRem");
		
		for ( i = 0 ; i < loadList.size() ; i++ )
			log.println ( " " + (Load)loadList.get(i) );
		
		log.println ("");
	}
	
	public boolean isEmpty()				// check whether load list is empty
	{
		return loadList.size() == 0;
	}
	
	public void terminate ()				// to close the output stream
	{
		try {
			out.close();
			log.close();
		}
		catch (IOException e)
		{
			System.out.println("Error: " + e);
			System.exit(1);
		}
	}
			
	
	//--------------------
	//  member variable
	//--------------------
	private Load load;				// load
	private LinkedList loadList;	// to store loads
	
	private FileOutputStream out;	// file output
	private PrintWriter log;
	
	private int time = 1;			// time initialize
	private float threshold;		// threshold
	private float performanceRate;	// performance rate
	
	private int cpuUsed;			// used CPU resource
	private int maxCpu = 100;		// maximum CPU resource
	
	private int memUsed;
	private int maxMem = 256;
	
	private int bwUsed;
	private int maxBw = 100;
}