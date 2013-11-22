package robotinterpreter.variables.literals;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

/**
 * Represents a literal boolean object.
 * Can be either true or false.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class BOOLEAN extends Variable
{
	private boolean value;
	
	/**
	 * Ensures that the boolean value is valid and adopts it if so.
	 * 
	 * @param b	the parent body
	 * @param c	the Code file
	 * @param s	a string containing just the literal, i.e. "true" or "false"
	 */
	public BOOLEAN(BODY b, Code c, String s)
	{
		body = b;
		code = s;
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(s);

		//We should only have one token here, the literal itself
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

	/**
	 * Simple print function - prints the value.
	 */
	public void print() 
	{
		RobotInterpreter.write("parse", "bool " + value);
	}

	/**
	 * We have nothing to validate here, but we must implement the function.
	 */
	public void validate() 
	{ 
		RobotInterpreter.writeln("validate", "Validating BOOLEAN");
	}

	/**
	 * Return the value.
	 * 
	 * @param args	always null
	 * @return	the boolean value
	 */
	public Object execute(Object args[]) 
	{
		return value;
	}
}
