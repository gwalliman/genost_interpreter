package robotinterpreter.variables.methods.external;

import robotinterpreter.RobotListener;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;

public class getBearing extends ExtMethod 
{
	
	public getBearing()
	{
		id = "getSonars";
		type = Terminals.INT;
		paramTypes = null;
	}
	
	public Object execute(Object[] args) 
	{
		int data = 0;
		for(RobotListener l : RobotInterpreter.getRobotListeners())
		{
			data = l.getBearing();
		}
		return data;
	}
}