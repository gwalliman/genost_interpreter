package robotinterpreter.variables.constants;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.variables.Variable;

public class INTEGER extends Variable
{
	private int value;
	
	public INTEGER(Code c, String s)
	{
		code = s;
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(s);
		
		if(tokens.length == 1)
		{

			if(tokens[0].matches("-?[0-9]+"))
			{
				value = Integer.parseInt(tokens[0]);
			}
			else RobotInterpreter.halt("INTEGER", lineNum, code, "Provided integer is of invalid format");
		}
		else RobotInterpreter.halt("INTEGER", lineNum, code, "Syntax error in integer.");
	}

	public void print() 
	{
		System.out.print("int " + value);
	}

	//Nothing to validate, value was validated during parsing.
	public void validate() 	{ }

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
