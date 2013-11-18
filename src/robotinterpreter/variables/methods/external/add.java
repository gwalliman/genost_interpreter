package robotinterpreter.variables.methods.external;

import robotinterpreter.terminals.Terminals;

public class add extends ExtMethod 
{
	public add()
	{
		id = "add";
		type = Terminals.INT;
		paramTypes = new String[2];
		paramTypes[0] = Terminals.INT;
		paramTypes[1] = Terminals.INT;
	}
	
	public Object execute(Object[] args) 
	{
		return ((int)args[0]) + ((int)args[1]);
	}
}
