package robotinterpreter.variables.loop;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

/**
 * Executes a body of code for a certain number of iterations.
 * We specify the number of iterations with an integer literal. The integer may be in the range (-1, infinity).
 * A positive or zero integer will execute for exactly that many times. -1 will execute infinitely.
 * 
 * Note that LOOPFORs create a new scope for their code bodies, so any variables declared within the code body will be accessible only from within that body (and any child bodies).
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class LOOPFOR extends Variable
{
	private int iterations;
	private BODY codeBody;
	
	/**
	 * Gets the iterations number and ensures that it is of the proper format.
	 * Then parses the body.
	 * 
	 * @param b
	 * @param c
	 */
	public LOOPFOR(BODY b, Code c)
	{
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		//PARSING INTEGER
		//Ensure that the second token is indeed an integer.
		if(tokens[1].matches("-?[0-9]+"))
		{
			iterations = Integer.parseInt(tokens[1]);
		}
		else RobotInterpreter.halt("LOOPFOR", lineNum, code, "LOOPFOR iterations integer is missing or of invalid format.");
		
		//PARSING BODY
		c.nextLine();
		codeBody = new BODY(body, c);
	}
	
	/**
	 * @return	the code body for this statement
	 */
	public BODY getCodeBody()
	{
		return codeBody;
	}
	
	/**
	 * Simple print function.
	 */
	public void print() 
	{
		RobotInterpreter.writeln("parse", "loopfor " + iterations + " times");
		codeBody.print();
	}

	/**
	 * First, ensure that the iterations integer is greater than or equal to -1.
	 * Second, validate the body itself.
	 */
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating LOOPFOR");

		if(iterations < -1)
		{
			RobotInterpreter.halt("LOOPFOR", lineNum, code, "LOOPFOR iterations integer cannot be less than -1");
		}
		codeBody.validate();
	}

	/**
	 * If iterations is equal to -1, we create an infinite loop and run it.
	 * TODO: create a way of breaking out of this loop.
	 * 
	 * If iterations is 0 or greater, run a for loop to execute the code body that exact number of times.
	 * 
	 * @param args	should always be null
	 * @return	always returns null
	 * 
	 */
	public Object execute(Object[] args) 
	{
		if(iterations == -1)
		{
			while(true)
			{
				codeBody.execute(null);
			}
		}
		else
		{
			for(int x = 0; x < iterations; x++)
			{
				codeBody.execute(null);
			}
		}
		return null;
	}
}
