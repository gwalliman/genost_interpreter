package robotinterpreter.variables.methods.external;

import robotinterpreter.RobotInterpreter;

public class print extends ExtMethod 
{
	public print()
	{
		id = "print";
		type = "void";
		paramTypes = new String[1];
		paramTypes[0] = "string";
	}
	
	public Object execute(Object[] args) 
	{
		RobotInterpreter.writeln("message", (String)args[0]);
		return null;
	}
}
