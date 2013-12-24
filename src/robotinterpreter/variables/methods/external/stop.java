package robotinterpreter.variables.methods.external;

import robotinterpreter.RobotListener;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;

public class stop extends ExtMethod 
{
	
	public stop()
	{
		id = "stop";
		type = Terminals.VOID;
		paramTypes = null;
	}
	
	public Object execute(Object[] args) 
	{
		for(RobotListener l : RobotInterpreter.getRobotListeners())
		{
			l.stop();
		}
		return null;
	}
}