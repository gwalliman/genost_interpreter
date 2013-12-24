package robotinterpreter.variables.methods.external;

import robotinterpreter.Interpreter;
import robotinterpreter.terminals.Terminals;

public class print extends ExtMethod 
{
	public print()
	{
		id = "print";
		type = Terminals.VOID;
		paramTypes = new String[1];
		paramTypes[0] = Terminals.STRING;
	}
	
	public Object execute(Object[] args) 
	{
		Interpreter.writeln("message", (String)args[0]);
		return null;
	}
}
