package robotinterpreter.variables.methods.external;

import robotinterpreter.RobotListener;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;

public class turn extends ExtMethod 
{
	
	public turn()
	{
		id = "turn";
		type = Terminals.VOID;
		paramTypes = new String[1];
		paramTypes[0] = Terminals.STRING;
	}
	
	public Object execute(Object[] args) 
	{
		for(RobotListener l : RobotInterpreter.getRobotListeners())
		{
			if(args[0].equals("l"))
			{
				l.turnLeft();
			}
			else if(args[0].equals("r"))
			{
				l.turnRight();
			}
		}
		return null;
	}
}