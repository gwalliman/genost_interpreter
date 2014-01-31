package robotinterpreter.variables.methods.external;

import robotinterpreter.RobotListener;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;

public class turnAngle extends ExtMethod 
{
	
	public turnAngle()
	{
		id = "turnAngle";
		type = Terminals.VOID;
		paramTypes = new String[1];
		paramTypes[0] = Terminals.INT;
	}
	
	public Object execute(Object[] args) 
	{
		for(RobotListener l : RobotInterpreter.getRobotListeners())
		{
			l.turnAngle((Integer) args[0]);
		}
		return null;
	}
}