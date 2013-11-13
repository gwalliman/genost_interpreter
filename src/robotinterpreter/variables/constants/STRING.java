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
		
		if(code.substring(0, 1).equals(Terminals.QUOTE) && code.substring(code.length() - 1, code.length()).equals(Terminals.QUOTE))
		{
			value = code.substring(1, code.length() - 1);
		}
		else RobotInterpreter.halt("STRING", lineNum, code, "String must be wrapped in quotes");
	}

	public void print() 
	{
		System.out.print("string \"" + value + "\"");
	}

	//Nothing to validate, value was validated during parsing.
	public void validate() 
	{ 
		System.out.println("Validating STRING");
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
