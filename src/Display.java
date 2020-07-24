import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Display extends JFrame implements ActionListener
{
	//---------------
	//  constructor
	//---------------
	public Display ()
	{
		setTitle ("Simulation");				// set title of window
		setSize (400, 400);						// set size of window
		setResizable(false);					// resizable false
		addWindowListener(new WindowAdapter()	// terminatable
			{
				public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				}
			} );

		// Buttons
		makefile = new JButton("New Load");
		simulate = new JButton("Simulate");
		
		// Labels
		JLabel intervalLabel = new JLabel("Maximum Interval");
		JLabel cpuLabel = new JLabel("Maximum CPU (1~100)");
		JLabel memLabel = new JLabel("Maximum Memory (1~256)");
		JLabel bwLabel = new JLabel("Maximum Bandwidth (1~100)");
		JLabel loadNumberLabel = new JLabel("The number of loads");
		JLabel nodeNumberLabel = new JLabel("The number of nodes");
		
		// Text Field
		interval = new JTextField("3", 4);
		cpu = new JTextField("40", 4);
		mem = new JTextField("70", 4);
		bw = new JTextField("30", 4);
		dur = new JTextField("40", 4);
		numOfLoad = new JTextField("500", 4);
		numOfNode = new JTextField("5", 4);
		
		// setting panel
		setting = new JPanel();
		setting.setLayout(new GridBagLayout());
		GridBagConstraints constraint = new GridBagConstraints();
		Border settingEtched = BorderFactory.createEtchedBorder();
		Border settingTitled = BorderFactory.createTitledBorder(settingEtched, "Configuration");
		setting.setBorder(settingTitled);
		setting.reshape(5, 210, 250, 160);
		
		constraint.weightx = 0;
		constraint.weighty = 0;
		constraint.anchor = GridBagConstraints.WEST;
		addComponent(setting, intervalLabel,   constraint, 0, 0, 1, 1);
		addComponent(setting, cpuLabel,        constraint, 0, 1, 1, 1);
		addComponent(setting, memLabel,        constraint, 0, 2, 1, 1);
		addComponent(setting, bwLabel,         constraint, 0, 3, 1, 1);
		addComponent(setting, loadNumberLabel, constraint, 0, 4, 1, 1);
		addComponent(setting, nodeNumberLabel, constraint, 0, 5, 1, 1);
		
		constraint.anchor = GridBagConstraints.CENTER;
		addComponent(setting, interval,      constraint, 1, 0, 1, 1);
		addComponent(setting, cpu,           constraint, 1, 1, 1, 1);
		addComponent(setting, mem,           constraint, 1, 2, 1, 1);
		addComponent(setting, bw,            constraint, 1, 3, 1, 1);
		addComponent(setting, numOfLoad,     constraint, 1, 4, 1, 1);
		addComponent(setting, numOfNode,     constraint, 1, 5, 1, 1);
		
		
		// sim panel
		sim = new JPanel();
		sim.setLayout(null);
		Border simEtched = BorderFactory.createEtchedBorder();
		Border simTitled = BorderFactory.createTitledBorder(simEtched, "Simulation");
		sim.setBorder(simTitled);
		sim.reshape(260, 210, 130, 160);
		makefile.reshape(15, 30, 100, 50);
		simulate.reshape(15, 90, 100, 50);
		sim.add(makefile);
		sim.add(simulate);
		simulate.setEnabled(false);

		// graph panel
		graph = new JPanel();
		Border graphBevel = BorderFactory.createLoweredBevelBorder();
		Border graphTitled = BorderFactory.createTitledBorder(graphBevel, "Throughput vs Threshold Graph");
		graph.setBorder(graphTitled);
		graph.reshape(5, 5, 385, 200);
		
		// button action
		makefile.addActionListener(this);
		simulate.addActionListener(this);
		
		// add component to container
		Container contentPane = getContentPane();
		contentPane.setLayout(null);
		contentPane.add(setting);
		contentPane.add(sim);
		contentPane.add(graph);
	}
	
	//-------------
	//  methods
	//-------------
	public void addComponent (JPanel d, Component c, GridBagConstraints gbc, int x, int y, int w, int h)
	{
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		d.add(c, gbc);
	}
	
	public void start(int numNode, float threshold)
	{
		int i;
		Node temp;
		
		list = new LinkedList();
		for ( i = 0 ; i < numNode ; i++ ) {				// create nodes
			temp = new Node ("./logfile/" + "Node"+(i+1)+".txt", threshold);
			list.add(temp);
		}
		
		Dispatcher dispatch = new Dispatcher(fileName, list);	// create dispatcher
		dispatch.dispatch();									// dispatch
		System.out.println( dispatch.numberOfDone() );
		list = null;
	}
	
	//-----------------------
	//  Button Action Event
	//-----------------------
	public void actionPerformed(ActionEvent evt)
	{
		Object source = evt.getSource();
		
		if (source == makefile) {
			simulate.setEnabled(false);

			int maxInterval = Integer.parseInt( interval.getText().trim() );		// maximum interval of arrival time
			int maxCpu = Integer.parseInt( cpu.getText().trim() );					// maximum CPU resource requirement
			int maxMem = Integer.parseInt( mem.getText().trim() );					// maximum memor resource requirement
			int maxBw = Integer.parseInt( bw.getText().trim() );					// maximum network bandwidth
			int maxDuration = Integer.parseInt( dur.getText().trim() );				// maximum duration time
			int numLoad = Integer.parseInt( numOfLoad.getText().trim() );			// number of load
			
			makeLoadFile(fileName, numLoad, maxInterval, maxCpu, maxMem, maxBw, maxDuration);		// make load file
						
			simulate.setEnabled(true);
		}
		else if (source == simulate) {
			makefile.setEnabled(false);

			int numNode = Integer.parseInt( numOfNode.getText().trim() );
						
			start(numNode, 0.8f);				// simulate start
			makefile.setEnabled(true);
		}
	}
	
	
	//-----------------
	//  static method
	//-----------------
	public static void makeLoadFile(String fileName, int numLoad, int maxInterval, int maxCpu, int maxMem, int maxBw, int maxDuration)
	{
		int i;
		int time = 0;
		
		try {
			FileOutputStream out = new FileOutputStream(fileName);	// open stream
			PrintWriter load = new PrintWriter(out, true);
		
			for ( i = 1 ; i <= numLoad ; i++ ) {					// make load...
				load.print("load"+i);									// example
				load.print(" (");										//   load3 (5 10 54 34 25 4)
				time += randInt(1, maxInterval);
				load.print(time);
				load.print(' ');
				load.print(randInt(1, maxCpu));
				load.print(' ');
				load.print(randInt(1, maxMem));
				load.print(' ');
				load.print(randInt(1, maxBw));
				load.print(' ');
				load.print(randInt(1, maxDuration));
				load.println(')');
			}
			out.close();										// stream is closed
			load.close();
		}
		catch (IOException e) {
			System.out.println("Error: " + e);
			System.exit(1);
		}
	}
	
	public static int randInt(int low, int high)			// this method generates random interger with range.
	{
		return (int)(Math.random()*(high-low+1)) + low;
	}
	
	// main method
	public static void main (String[] args)
	{
		Display display = new Display();
		display.show();
	}

	//-------------------
	// member variables
	//-------------------
	private String fileName = "./logfile/load.txt";		// the file that is recorded loads;
	
	private JButton makefile;
	private JButton simulate;
	
	private JPanel setting;
	private JPanel sim;
	private JPanel graph;
	
	private JTextField interval;
	private JTextField cpu;
	private JTextField mem;
	private JTextField bw;
	private JTextField dur;
	private JTextField numOfLoad;
	private JTextField numOfNode;
	
	private LinkedList list;		// the list of nodes
}