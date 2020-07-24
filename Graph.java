import java.awt.*;
import javax.swing.*;

public class Graph extends JPanel
{
	public Graph(int[] value)
	{
		setSize(385, 200);				// set size
		this.value = value;				// assign data
		origin = new Point(30, 170);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g.drawLine(origin.x, origin.y, origin.x, 30);		// draw y axis
		g.drawLine(origin.x, origin.y, 350, origin.y);		// draw x axis
		
		drawGraph(g);
	}	
	
	public void drawGraph(Graphics g)
	{
		int i;
		int x1 = origin.x;
		int x2;
		int y1 = origin.y;
		int y2;
		int maxValue[] = {0, 0};
		
		// draw graph line....
		g.setColor(Color.blue);
		for ( i = 1 ; i < value.length ; i++ ) {
			if ( maxValue[1] < value[i] ) {				// file maximum value in array.
				maxValue[0] = i;							// store index
				maxValue[1] = value[i];						// store value
			}
			x2 = origin.x + 15 * i;
			y2 = origin.y - 140 * value[i] / numLoad;	// 140 = origin.y - 30
			g.drawLine(x1, y1, x2, y2);
			x1 = x2;
			y1 = y2;
		}
		//System.out.println ( (maxValue[0] * 0.05) + "  " + maxValue[1]);
		
		// point max value....
		g.setColor(Color.red);
		x1 = origin.x + maxValue[0]*15;								// point threshold
		g.drawLine(x1, 165, x1, 175);								// 165 = origin.y - 5
		g.drawString("Threshold " + maxValue[0]*0.05, maxValue[0]*15, 190);
		
		y1 = origin.y - 140 * maxValue[1] / numLoad;				// point throughput		140 = origin.y - 30
		g.drawLine( 25, y1, 35, y1);								// 						25 = origin.x - 5
		g.drawString("Throughput " + maxValue[1], 40, 175 - 140 * maxValue[1] / numLoad);
	}
	
	public void setData(int[] value, int numLoad)		// to take data
	{
		this.value = value;
		this.numLoad = numLoad;
	}
	
	private Point origin;
	private int[] value;			// to store array of values
	private int numLoad = 1;		// the number of load
}