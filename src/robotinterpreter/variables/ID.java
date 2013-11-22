package robotinterpreter.variables;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;

/**
 * This class is used simply to validate an ID. It confirms that the ID can be used.
 * An ID must conform to two rules:
 * 1. Must be alphanumeric
 * 2. Must not be a reserved word as defined in the Terminals class.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
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
