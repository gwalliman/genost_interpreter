package robotinterpreter.variables.methods.external;

import robotinterpreter.Interpreter;
import robotinterpreter.terminals.Terminals;

public class divide extends ExtMethod 
{
	private Interpreter interpreter;
	
	public divide(Interpreter in)
	{
		interpreter = in;
		id = "add";
		type = Terminals.INT;
		paramTypes = new String[2];
		paramTypes[0] = Terminals.INT;
		paramTypes[1] = Terminals.INT;
	}
	
	public Object execute(Object[] args) 
	{
		int denom = ((Integer)args[1]);
		if(denom == 0)
		{
			interpreter.error("DIVIDE", -1, "Externally defined", "Divide by 0 error");
		}
		return ((Integer)args[0]) / ((Integer)args[1]);
	}
}
