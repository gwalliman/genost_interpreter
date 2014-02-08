package robotinterpreter.variables.literals;

import robotinterpreter.Code;
import robotinterpreter.Interpreter;
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
	private Interpreter interpreter;
	
	private boolean value;
	
	/**
	 * Ensures that the boolean value is valid and adopts it if so.
	 * 
	 * @param b	the parent body
	 * @param c	the Code file
	 * @param s	a string containing just the literal. Format: "true", "false"
	 */
	public BOOLEAN(Interpreter in, BODY b, Code c, String s)
	{
		interpreter = in;
		body = b;
		code = s;
		lineNum = c.currentLineNum();
		
		String[] tokens = c.tokenize(s);

		//We should only have one token here, the literal itself
		if(tokens.length == 1)
		{
			if(Terminals.booleanVals.contains(tokens[0]))
			{
				value = Boolean.parseBoolean(tokens[0]);
			}
			else interpreter.error("BOOLEAN", lineNum, code, "Boolean value must be either true or false");
		}
		else interpreter.error("BOOLEAN", lineNum, code, "Syntax error in boolean.");
	}

	/**
	 * Simple print function - prints the value.
	 */
	public void print() 
	{
		interpreter.write("parse", "bool " + value);
	}

	/**
	 * We have nothing to validate here, but we must implement the function.
	 */
	public void validate() 
	{ 
		interpreter.writeln("validate", "Validating BOOLEAN");
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
