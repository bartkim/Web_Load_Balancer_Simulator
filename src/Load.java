public class Load
{
	//----------------
	//  constructor
	//----------------
	public Load (String name, int arrival, int cpu, int mem, int bw, int duration)
	{
		this.name = name;				// initialize
		this.arrival = arrival;
		this.cpu = cpu;
		this.mem = mem;
		this.bw = bw;
		this.duration = duration;
		remain = duration;
	}
	
	
	//------------
	//  methods
	//------------
	public int unitTimePass()			// unit time pass
	{
		return --remain;
	}
	
	public String toString()			// convert data to string value
	{
		return name + "\t" + arrival + "\t" + cpu + "\t" + mem + "\t" + bw + "\t" + duration + "\t" + remain;
	}
	
	public int getArrival()				// to get arrival time
	{
		return arrival;
	}
	
	public int getCpu()					// to get required cpu resource
	{
		return cpu;
	}
	
	public int getMem()					// to get required memory resource
	{
		return mem;
	}
	
	public int getBw()					// to get required bandwidth resource
	{
		return bw;
	}
	
	
	//-------------------
	//  member variable
	//-------------------
	private String name;	// the name of load
	private int arrival;	// the arrival time
	private int duration;	// duration
	private int remain;		// remained time
	private int cpu;		// the requirement of cpu resource
	private int mem;		// the requirement of memory resource
	private int bw;			// the requirement of bandwidth resource
}