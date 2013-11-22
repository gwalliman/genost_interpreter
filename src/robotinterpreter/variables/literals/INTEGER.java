package robotinterpreter.variables.literals;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

/**
 * Represents a literal integer, which can be positive or negative.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class INTEGER extends Variable
{
	private int value;
	
	/**
	 * Takes the provided value and ensures that it is an integer.
	 * If so, adopts it as a value.
	 * 
	 * @param b	the parent body
	 * @param c	the Code file
	 * @param s	a string containing only the integer value
	 */
	public INTEGER(BODY b, Code c, String s)
	{
		body = b;
		code = s;
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(s);
		
		//We should only have one token here, the literal itself
		if(tokens.length == 1)
		{
			//Every valid integer should be of this format
			if(tokens[0].matches("-?[0-9]+"))
			{
				value = Integer.parseInt(tokens[0]);
			}
			else RobotInterpreter.halt("INTEGER", lineNum, code, "Provided integer is of invalid format");
		}
		else RobotInterpreter.halt("INTEGER", lineNum, code, "Syntax error in integer.");
	}
	
	/**
	 * Simple print function - prints the value.
	 */
	public void print() 
	{
		RobotInterpreter.write("parse", "int " + value);
	}

	/**
	 * We have nothing to validate here, but we must implement the function.
	 */
	public void validate() 	
	{ 
		RobotInterpreter.writeln("validate", "Validating INTEGER");
	}

	/**
	 * Return the value.
	 * 
	 * @param args	always null
	 * @return	the integer value
	 */
	public Object execute(Object args[]) 
	{
		return value;
	}
}
