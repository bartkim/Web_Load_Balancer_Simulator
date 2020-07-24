import java.io.*;
import java.util.*;

public class Dispatcher
{
	//---------------
	//  constructor
	//---------------
	public Dispatcher (String fileName, LinkedList list)
	{
		this.fileName = fileName;			// file name that contains loads
		moon = list;						// the list of nodes
	}
	
	//------------
	//  methods
	//------------
	public void dispatch()
	{
		try {
			FileReader in = new FileReader(fileName);			// to open file
			BufferedReader inBuffer = new BufferedReader(in);	// to open read stream
		
			load = readLoad(inBuffer);							// read load from load file
		
			while ( !(load == null) || !isEmptyNodeLoad() ) {		//There is no remained load in file &&(and)&& no processing load in node.
				if ( load != null && load.getArrival() == time ) {		// at arrival time of load
					if ( assign(load) == false )							// assign load to node by RR
						//miss();													// print miss message
						if ( moon.size() == 0 ) {
							failure();											// failure
							break;
						}
					load = readLoad(inBuffer);							// read new load
				}
				unitTimePass();											// unit time pass
			}
		
			complete();											// print message and close nodes' output stream
				
			in.close();											// close FileReader
			inBuffer.close();									// close BufferedReader
		}
		catch (IOException e)
		{
			System.out.println("Error: " + e);
			System.exit(1);
		}
	}
	
	public boolean assign (Load load)				// load is assigned to nodes by RR.
	{
		int i;
		Node temp;
		
		for ( i = 0 ; i < moon.size() ; i++ ) {
			temp = (Node) moon.get(turn);
			
			if ( temp.putLoad(load) ) {					// Is available ?
				if ( temp.isExceed() ) {					// Is exceed ?
					temp.failure();								// node is failure.
					moon.remove(turn);							// node removed.
					if ( moon.size() != 0 ) {
						turn = turn % moon.size();
						i--;
					}
				} else {									// available and not exceed ( no problem! )
					done++;										// done increase...
					turn = ++turn % moon.size();				// load is assigned in turn
					return true;
				}
			}
			else
				turn = ++turn % moon.size();
		}

		return false;
	}
	
	public Load readLoad(BufferedReader inBuffer) throws IOException
	{
		String name;							// to store temporarily
		int arrival, cpu, mem, bw, duration;
		
		String line = inBuffer.readLine();		// read a line.
				
		if ( line == null )						// if there is no line, returns null
			return null;
		else {
			StringTokenizer token = new StringTokenizer(line, " ()");	// to get token
			name = token.nextToken();									// read name
			arrival = Integer.parseInt(token.nextToken());				// read arrival time
			cpu = Integer.parseInt(token.nextToken());					// read cpu requirement
			mem = Integer.parseInt(token.nextToken());					// read mem requirement
			bw = Integer.parseInt(token.nextToken());					// read bandwidth requirement
			duration = Integer.parseInt(token.nextToken());				// read duraton time
			
			return new Load(name, arrival, cpu, mem, bw, duration);		// create object, and return load
		}
	}
	
	public void unitTimePass()
	{
		int i;
		
		time++;							// unit time passes
		
		for ( i = 0 ; i < moon.size() ; i++ )
			((Node) moon.get(i)).unitTimePass();		// nodes' time pass
	}
	
	public boolean isEmptyNodeLoad()				// to check whether nodes' load list is all empaty
	{
		int i;
		boolean flag = true;
		
		for ( i = 0 ; i < moon.size() ; i++ )
			flag = flag && ((Node) moon.get(i)).isEmpty();		// Is all nodes' load list empty?
		
		return flag;
	}
	
	public void terminateNodes()				// to close nodes' output stream  
	{
		int i;
		
		for ( i = 0 ; i < moon.size() ; i++ )
			((Node)moon.get(i)).terminate();				// terminate...
	}
	
	public void complete ()
	{											// to print complete message and close nodes' output stream
		int i;
		
		for ( i = 0 ; i < moon.size() ; i++ )
			((Node)moon.get(i)).complete();					// complete...
	}
	
	public void miss()
	{
		System.out.println ("~~~~~~~~~~~~   missed   ~~~~~~~~~~~~~");
		System.out.println (load);
		System.out.println ();
	}
	
	public int numberOfDone ()
	{
		return done;
	}
	
	public void failure()						// system is failure
	{
		//System.out.println ("!!!!!!!!!!!!   System fail...    !!!!!!!!!!!!!!");
		//System.out.println ("Time : " + time);
		//System.out.println (load);
		
		terminateNodes();
		//System.exit(1);
	}
	
	//-------------------
	//  member variable
	//-------------------
	private String fileName;		// input file name that contains loads
	private int time = 1;			// time
	private int turn;				// turn of nodes
	private Load load;				// load
	private int done = 0;			// the number of load
	
	private LinkedList moon;		// linked list of node
}