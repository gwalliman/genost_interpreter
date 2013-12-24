package robotinterpreter;

import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import robotinterpreter.terminals.Terminals;

public class RobotInterpreter 
{
	
	public static ArrayList<RobotListener> robotlisteners = new ArrayList<RobotListener>();

	/**
	 * Main function.
	 * Initializes the terminals, sets up the GUI.
	 * 
	 * @param args
	 */
	public RobotInterpreter()
	{
		Terminals.init();
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	components.FileChooserDemo.createAndShowGUI();;
		    }
		});   
	}
	
	public static ArrayList<RobotListener> getRobotListeners()
	{
		return robotlisteners;
	}
	
	public void addRobotListener(RobotListener toAdd)
	{
		robotlisteners.add(toAdd);
	}
	
	public static void main(String args[])
	{
		@SuppressWarnings("unused")
		RobotInterpreter r = new RobotInterpreter();
	}
}
