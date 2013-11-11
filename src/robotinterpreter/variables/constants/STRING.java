package robotinterpreter.variables.constants;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.Variable;

public class STRING extends Variable
{
	private String value;
	
	public STRING(Code c, String s)
	{
		code = s;
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(s);
		
		if(tokens.length > 2)
		{
			if(tokens[0] != Terminals.QUOTE || tokens[tokens.length - 1] != Terminals.QUOTE)
			{
				RobotInterpreter.halt("STRING", lineNum, code, "String must be wrapped in quotes");
			}
			
			value = code.substring(1, code.length() - 1);
		}
		else RobotInterpreter.halt("STRING", lineNum, code, "Syntax error in string");
	}

	public void print() 
	{
		System.out.print("string \"" + value + "\"");
	}
}
