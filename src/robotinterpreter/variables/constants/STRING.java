package robotinterpreter.variables.constants;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

public class STRING extends Variable
{
	private String value;
	
	public STRING(BODY b, Code c, String s)
	{
		body = b;
		code = s;
		lineNum = c.currentLineNum();
		
		if(code.substring(0, 1).equals(Terminals.QUOTE) && code.substring(code.length() - 1, code.length()).equals(Terminals.QUOTE))
		{
			value = code.substring(1, code.length() - 1);
		}
		else RobotInterpreter.halt("STRING", lineNum, code, "String must be wrapped in quotes");
	}

	public void print() 
	{
		RobotInterpreter.write("parse", "string \"" + value + "\"");
	}

	//Nothing to validate, value was validated during parsing.
	public void validate() 
	{ 
		RobotInterpreter.writeln("validate", "Validating STRING");
	}

	public Object execute(Object args[]) 
	{
		return value;
	}
}
