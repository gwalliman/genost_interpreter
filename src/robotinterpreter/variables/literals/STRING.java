package robotinterpreter.variables.literals;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

/**
 * Represents a string literal.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class STRING extends Variable
{
	private String value;
	
	/**
	 * Takes the provided value and ensures that it is an String.
	 * If so, adopts it as a value.
	 * 
	 * @param b	the parent body
	 * @param c	the Code file
	 * @param s	a String containing just the literal value (which here should be wrapped in quotes). Format: ""String test"", ""ASDFf1234!@%""
	 */
	public STRING(BODY b, Code c, String s)
	{
		body = b;
		code = s;
		lineNum = c.currentLineNum();
		
		//Ensure that the value is wrapped in quotes. If it is, strip the quotes and adopt the result as the value.
		if(code.substring(0, 1).equals(Terminals.QUOTE) && code.substring(code.length() - 1, code.length()).equals(Terminals.QUOTE))
		{
			value = code.substring(1, code.length() - 1);
		}
		else RobotInterpreter.halt("STRING", lineNum, code, "String must be wrapped in quotes");
	}
	
	/**
	 * Simple print function - prints the value.
	 */
	public void print() 
	{
		RobotInterpreter.write("parse", "string \"" + value + "\"");
	}

	/**
	 * We have nothing to validate here, but we must implement the function.
	 */
	public void validate() 
	{ 
		RobotInterpreter.writeln("validate", "Validating STRING");
	}
	
	/**
	 * Return the value.
	 * 
	 * @param args	always null
	 * @return	the String value
	 */
	public Object execute(Object args[]) 
	{
		return value;
	}
}
