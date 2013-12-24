package robotinterpreter.variables.wait;

import robotinterpreter.Code;
import robotinterpreter.Interpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

/**
 * WAITFOR causes the execution thread to sleep for a certain specified number of seconds.
 * The number of seconds MUST be positive!
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class WAITFOR extends Variable
{
	private int iterations;
	
	/**
	 * Gets the number of iterations. This is actually all we need to do.
	 * 
	 * @param b	the parent body
	 * @param c	the Code object
	 */
	public WAITFOR(BODY b, Code c)
	{
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		//Parsing number of iterations
		if(tokens[1].matches("-?[0-9]+"))
		{
			iterations = Integer.parseInt(tokens[1]);
		}
		else Interpreter.error("WAITFOR", lineNum, code, "WAITFOR integer is missing or of invalid format.");
		
		if(tokens[tokens.length - 1] != Terminals.SEMICOLON)
		{
			Interpreter.error("WAITFOR", lineNum, code, "Missing semicolon");
		}		
	}
	
	/**
	 * Simple print function
	 */
	public void print() 
	{
		Interpreter.writeln("parse", "waitfor " + iterations + " seconds");
	}


	/**
	 * Ensure iterations is not negative
	 * Validate body
	 */
	public void validate() 
	{
		Interpreter.writeln("validate", "Validating WAITFOR");

		if(iterations < 0)
		{
			Interpreter.error("LOOPFOR", lineNum, code, "LOOPFOR iterations integer cannot be less than 0");
		}
	}

	/**
	 * When we execute, we simply sleep the thread for one second for each iteration.
	 * 
	 * @param args	should always be null
	 * @return	always returns null
	 */
	public Object execute(Object[] args) 
	{
		for(int x = 0; x < iterations; x++)
		{
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{
				Thread.currentThread().interrupt();
				return null;			
			}
		}
		
		return null;
	}
}
