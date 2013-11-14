package robotinterpreter.variables.constants;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

public class BOOLEAN extends Variable
{
	private boolean value;
	
	public BOOLEAN(BODY b, Code c, String s)
	{
		body = b;
		code = s;
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(s);

		if(tokens.length == 1)
		{
			if(Terminals.booleanVals.contains(tokens[0]))
			{
				value = Boolean.parseBoolean(tokens[0]);
			}
			else RobotInterpreter.halt("BOOLEAN", lineNum, code, "Boolean value must be either true or false");
		}
		else RobotInterpreter.halt("BOOLEAN", lineNum, code, "Syntax error in boolean.");
	}

	public void print() 
	{
		RobotInterpreter.write("parse", "bool " + value);
	}

	//Nothing to validate, value was validated during parsing.
	public void validate() 
	{ 
		RobotInterpreter.writeln("validate", "Validating BOOLEAN");
	}

	public Object execute(Object args[]) 
	{
		return value;
	}
}
