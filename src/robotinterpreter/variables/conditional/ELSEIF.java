package robotinterpreter.variables.conditional;

import robotinterpreter.Code;
import robotinterpreter.Interpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

/**
 * An ELSEIF is much like an IF; it has a CONDITIONLIST and a BODY.
 * There may be multiple ELSEIFs in one IF tree. To account for this, we treat ELSEIF like a linked list node and contain a link to another ELSEIF, if one exists.
 * 
 * Note that ELSEIFs create a new scope for their code bodies, so any variables declared within the code body will be accessible only from within that body (and any child bodies).
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class ELSEIF extends Variable
{
	private CONDITIONLIST cl;
	private BODY codeBody;
	//The next ELSEIF, if there is one
	private ELSEIF elseif;
	
	/**
	 * Goes through the same functions the IF does to get the CONDITIONLIST / codeBody.
	 * Afterwards, checks if the next line is another ELSEIF.
	 * If it is, recursively parse that ELSEIF.
	 * 
	 * @param b	the parent body
	 * @param c	the Code object
	 */
	public ELSEIF(BODY b, Code c)
	{
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		//An ELSEIF CONDITIONLIST must be surrounded by parentheses.
		if(tokens[1] != Terminals.OPENPAREN)
		{
			Interpreter.error("ELSEIF", lineNum, code, "ELSEIF must open with (");
		}
		
		if(tokens[tokens.length - 1] != Terminals.CLOSEPAREN)
		{
			Interpreter.error("ELSEIF", lineNum, code, "ELSEIF must close with )");
		}

		//If we have more than 3 tokens, then we have at least something in the CONDITIONLIST.
		if(tokens.length > 3)
		{
			cl = new CONDITIONLIST(body, c, code.substring(8, code.length() - 1));
		}
		else Interpreter.error("ELSEIF", lineNum, code, "ELSEIF must contain a condition list!");

		//Move on to the next line and parse the BODY.
		c.nextLine();
		codeBody = new BODY(body, c);
		
		//Get the next line. If we find another ELSEIF, parse it.
		c.nextLine();
		String[] newTokens = Code.tokenize(c.currentLine());
		if(newTokens[0].equals(Terminals.ELSEIF))
		{
			elseif = new ELSEIF(body, c);
		}
		
	}
	
	/**
	 * @return	the next ELSEIF.
	 */
	public ELSEIF getNextElseIf()
	{
		return elseif;
	}
	
	/**
	 * @return	the code body for this statement
	 */
	public BODY getCodeBody()
	{
		return codeBody;
	}
	
	/**
	 * Simple print function; prints the conditionlist and the codeBody.
	 * Prints the next ELSEIF, if there is one.
	 */
	public void print() 
	{
		Interpreter.write("parse", "elseif (");
		cl.print();
		Interpreter.writeln("parse", ")");
		codeBody.print();
		
		if(elseif != null)
		{
			Interpreter.write("parse", Code.newline);
			elseif.print();
		}
	}

	/**
	 * Validates three things:
	 * 1. The CONDITIONLIST
	 * 2. The BODY
	 * 3. The next ELSEIF, if applicable.
	 */
	public void validate() 
	{
		Interpreter.writeln("validate", "Validating ELSEIF");

		cl.validate();
		codeBody.validate();
		
		if(elseif != null)
		{
			elseif.validate();
		}
	}

	/**
	 * Executes the CONDITIONLIST and gets the result.
	 * If the result is true, executes the code body.
	 * If the result is false, go on to the next ELSEIF and execute that.
	 * 
	 * Note that once we get a TRUE result, we return TRUE, to tell the IF that we executed one of the ELSEIFs.
	 * If we never get a TRUE, then we return FALSE (this happens when we get a FALSE result on an ELSEIF that has no next ELSEIF)
	 * 
	 * @param args	should always be null
	 * @return	true if one of the ELSEIFs executed, false otherwise.
	 */
	public Object execute(Object[] args) 
	{
		boolean go = (Boolean) cl.execute(null); 
		
		if(go)
		{
			codeBody.execute(null);
			return go;
		}
		else if(elseif != null)
		{
			return elseif.execute(null);
		}
		else
		{
			return false;
		}
	}
}
