package robotinterpreter.variables;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;

public class ID 
{
	public static String validate(String s, Code c)
	{
		if(!Code.checkAlphaNumeric(s))
		{
			RobotInterpreter.halt("ID", c.currentLineNum(), c.currentLine(), "ID must be alphanumeric.");
		}
		
		if(Terminals.reservedWords.contains(s))
		{
			RobotInterpreter.halt("ID", c.currentLineNum(), c.currentLine(), "ID cannot be resereved word " + s);
		}
		
		return s;
	}
}
