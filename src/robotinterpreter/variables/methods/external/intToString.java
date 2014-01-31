package robotinterpreter.variables.methods.external;

import robotinterpreter.terminals.Terminals;

public class intToString extends ExtMethod 
{
	public intToString()
	{
		id = "intToString";
		type = Terminals.STRING;
		paramTypes = new String[1];
		paramTypes[0] = Terminals.INT;
	}
	
	public Object execute(Object[] args) 
	{
		return Integer.toString(((Integer)args[0]));
	}
}
