package robotinterpreter.variables.methods.external;

import robotinterpreter.RobotListener;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;

public class turnToBearing extends ExtMethod 
{
	
	public turnToBearing()
	{
		id = "turnToBearing";
		type = Terminals.VOID;
		paramTypes = new String[1];
		paramTypes[0] = Terminals.INT;
	}
	
	public Object execute(Object[] args) 
	{
		for(RobotListener l : RobotInterpreter.getRobotListeners())
		{
			l.turnToBearing((int) args[0]);
		}
		return null;
	}
}