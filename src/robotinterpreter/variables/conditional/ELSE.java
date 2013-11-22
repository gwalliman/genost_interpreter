package robotinterpreter.variables.conditional;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

/**
 * ELSE is much simpler than IF or ELSEIF. It has a body and, if the ELSE is executed, the body will always execute.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class ELSE extends Variable
{
	private BODY codeBody;
	
	/**
	 * Parse the body and store it. That's it!
	 * 
	 * @param b	the parent body
	 * @param c	the Code object
	 */
	public ELSE(BODY b, Code c)
	{
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();

		c.nextLine();
		codeBody = new BODY(body, c);
	}
	
	/**
	 * Simply prints the code body.
	 * 
	 */
	public void print() 
	{
		RobotInterpreter.writeln("parse", "else");
		codeBody.print();
	}

	/**
	 * Simply validates the code body.
	 */
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating ELSE");

		codeBody.validate();
	}

	/**
	 * Simply executes the code body. Always executes it; there are no conditionlists to worry about.
	 * 
	 * @param args	will always be null
	 * @return always returns null
	 */
	public Object execute(Object[] args) 
	{
		codeBody.execute(null);
		return null;
	}
}
