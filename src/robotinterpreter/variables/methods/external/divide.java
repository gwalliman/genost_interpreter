package robotinterpreter.variables.methods.external;

import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;

public class divide extends ExtMethod 
{
	public divide()
	{
		id = "add";
		type = Terminals.INT;
		paramTypes = new String[2];
		paramTypes[0] = Terminals.INT;
		paramTypes[1] = Terminals.INT;
	}
	
	public Object execute(Object[] args) 
	{
		int denom = ((int)args[1]);
		if(denom == 0)
		{
			RobotInterpreter.error("DIVIDE", -1, "Externally defined", "Divide by 0 error");
		}
		return ((int)args[0]) / ((int)args[1]);
	}
}
