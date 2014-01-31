package robotinterpreter.variables.wait;

import robotinterpreter.Code;
import robotinterpreter.Interpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;
import robotinterpreter.variables.conditional.CONDITIONLIST;

/**
 * WAITUNTIL sleeps the execution thread until a certain condition evaluates to true.
 * For obvious reasons, this condition MUST change externally; it must be something changing in another thread, like a sensor value.
 * However, we do not enforce this, and any condition may be entered.
 * 
 * For obvious reasons, it is entirely possible to get caught in an infinite loop here.
 * TODO: Find some way of breaking out of infinite loop without crashing program.
 * 
 * Current polling frequency is 1/4 of a second (250 ms)
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class WAITUNTIL extends Variable
{
	private CONDITIONLIST cl;
	
	/**
	 * Parses the CONDITIONLIST. That's all we need to do!
	 * 
	 * @param b	the parent body
	 * @param c	the Code file
	 */
	public WAITUNTIL(BODY b, Code c)
	{
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		//Ensure that we have a matching OPEN and CLOSE parentheses around the CONDITIONLIST
		if(tokens[1] != Terminals.OPENPAREN)
		{
			Interpreter.error("WAITUNTIL", lineNum, code, "WAITUNTIL must open with (");
		}
		
		if(tokens[tokens.length - 2] != Terminals.CLOSEPAREN)
		{
			Interpreter.error("WAITUNTIL", lineNum, code, "WAITUNTIL must close with )");
		}
		
		//We must have a CONDITIONLIST; we error out if there is not one present.
		if(tokens.length > 3)
		{
			cl = new CONDITIONLIST(body, c, code.substring(11, code.length() - 2));
		}
		else Interpreter.error("WAITUNTIL", lineNum, code, "WAITUNTIL must contain a condition list!");

		if(tokens[tokens.length - 1] != Terminals.SEMICOLON)
		{
			Interpreter.error("WAITUNTIL", lineNum, code, "Missing semicolon");
		}		
	}
	
	/**
	 * Simple print function
	 */
	public void print() 
	{
		Interpreter.write("parse", "waituntil (");
		cl.print();
		Interpreter.writeln("parse", ")");
	}

	/**
	 * Validate condition list
	 */
	public void validate() 
	{
		Interpreter.writeln("validate", "Validating WAITUNTIL");

		cl.validate();
	}

	/**
	 * We get the initial value of the condition and, if that condition is false, we start waiting using a while loop.
	 * We sleep for the some amount of time related to the above polling value, then evaluate the condition again. We go back through the loop and try again.
	 * 
	 * @param args	should always be null
	 * @return	always returns null
	 */
	public Object execute(Object[] args) 
	{
		boolean go = (Boolean) cl.execute(null);
		
		while(!go)
		{
			try 
			{
				//We currently set polling interval to 1/4 of a second
				Thread.sleep(250);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
			go = (Boolean) cl.execute(null);
		}
		
		return null;
	}
}
