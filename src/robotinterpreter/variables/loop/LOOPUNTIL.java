package robotinterpreter.variables.loop;

import robotinterpreter.Code;
import robotinterpreter.Interpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;
import robotinterpreter.variables.conditional.CONDITIONLIST;

/**
 * class LOOPUNTIL
 * 
 * A LOOPUNTIL repeatedly executes a code body until a certain condition is TRUE. (If the condition is initially true, the body is never executed)
 * Contains a CONDITIONLIST and a BODY to execute.
 * 
 * Note that LOOPUNTILs create a new scope for their code bodies, so any variables declared within the code body will be accessible only from within that body (and any child bodies).
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class LOOPUNTIL extends Variable
{
	private CONDITIONLIST cl;
	private BODY codeBody;
	
	/**
	 * public LOOPUNTIL(BODY b, Code c)
	 * 
	 * Get the CONDITIONLIST. This is much like how an IF parses its CONDITIONLIST.
	 * Afterwards, parse the body.
	 * 
	 * @param b	the parent body
	 * @param c	the Code file
	 */
	public LOOPUNTIL(BODY b, Code c)
	{
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		//PARSING CONDITIONLIST
		//Ensure that the CONDITIONLIST is surrounded by parentheses
		if(tokens[1] != Terminals.OPENPAREN)
		{
			Interpreter.error("LOOPUNTIL", lineNum, code, "LOOPUNTIL must open with (");
		}
		
		if(tokens[tokens.length - 1] != Terminals.CLOSEPAREN)
		{
			Interpreter.error("LOOPUNTIL", lineNum, code, "LOOPUNTIL must close with )");
		}
		
		if(tokens.length > 3)
		{
			cl = new CONDITIONLIST(body, c, code.substring(11, code.length() - 1));
		}
		else Interpreter.error("LOOPUNTIL", lineNum, code, "LOOPUNTIL must contain a condition list!");

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
	 * public void print()
	 * 
	 * Simple print function.
	 */
	public void print() 
	{
		Interpreter.write("parse", "loopuntil (");
		cl.print();
		Interpreter.writeln("parse", ")");
		codeBody.print();
	}

	/**
	 * public void validate()
	 * 
	 * First, validate the CONDITIONLIST
	 * Second, validate the BODY.
	 */
	public void validate() 
	{
		Interpreter.writeln("validate", "Validating LOOPUNTIL");
		//VALIDATING CONDITIONLIST
		cl.validate();
		//VALIDATING BODY
		codeBody.validate();
	}

	/**
	 * public Object execute(Object[] args)
	 * 
	 * We create a WHILE loop that executes until a boolean value is true.
	 * Each iteration of the loop executes the CONDITIONLIST and assigns the result to the WHILE variable.
	 * Inside the loop, we first execute the BODY, and then update the boolean value with another check of the CONDITIONLIST.
	 * 
	 * Once the CONDITIONLIST returns true, we stop looping and return null.
	 * 
	 * @param args	should always be null
	 * @return	always returns null
	 * 
	 */
	public Object execute(Object[] args) 
	{
		//EXECUTING CONDITIONLIST (first run)
		boolean go = (Boolean) cl.execute(null);
		
		while(!go)
		{
			//EXECUTING BODY
			codeBody.execute(null);

			//EXECUTING CONDITIONLIST
			go = (Boolean) cl.execute(null);
		}
		
		return null;
	}
}
