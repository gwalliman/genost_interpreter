package robotinterpreter.variables.methods.external;

import robotinterpreter.RobotInterpreter;

public class divide extends ExtMethod 
{
	public divide()
	{
		id = "add";
		type = "int";
		paramTypes = new String[2];
		paramTypes[0] = "int";
		paramTypes[1] = "int";
	}
	
	public Object execute(Object[] args) 
	{
		int denom = ((int)args[1]);
		if(denom == 0)
		{
			RobotInterpreter.halt("DIVIDE", -1, "Externally defined", "Divide by 0 error");
		}
		return ((int)args[0]) / ((int)args[1]);
	}
}
