package robotinterpreter.variables.constants;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.Variable;

public class BOOLEAN extends Variable
{
	private boolean value;
	
	public BOOLEAN(Code c, String s)
	{
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
		System.out.print("bool " + value);
	}

	//Nothing to validate, value was validated during parsing.
	public void validate() { }

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
