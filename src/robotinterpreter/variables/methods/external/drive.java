package robotinterpreter.variables.methods.external;

import java.util.ArrayList;

import robotinterpreter.terminals.Terminals;

public class drive extends ExtMethod 
{
	ArrayList<DriveListener> listeners;
	public drive()
	{
		id = "drive";
		type = Terminals.VOID;
		paramTypes = new String[1];
		paramTypes[0] = Terminals.STRING;
		
		listeners = new ArrayList<DriveListener>();
	}
	
	public void addListener(DriveListener toAdd)
	{
		listeners.add(toAdd);
	}
	
	public Object execute(Object[] args) 
	{
		for(DriveListener l : listeners)
		{
			if(args[0] == "f")
			{
				l.driveForward();
			}
			else if(args[0] == "b")
			{
				l.driveBackwards();
			}
		}
		return null;
	}
}