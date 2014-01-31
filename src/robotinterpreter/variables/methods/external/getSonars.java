package robotinterpreter.variables.methods.external;

import robotinterpreter.RobotListener;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;

public class getSonars extends ExtMethod 
{
	
	public getSonars()
	{
		id = "getSonars";
		type = Terminals.INT;
		paramTypes = new String[1];
		paramTypes[0] = Terminals.INT;
	}
	
	public Object execute(Object[] args) 
	{
		int data = 0;
		for(RobotListener l : RobotInterpreter.getRobotListeners())
		{
			data = l.getSonarData((Integer)args[0]);
		}
		return data;
	}
}