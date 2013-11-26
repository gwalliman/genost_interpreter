package robotinterpreter.variables.conditional;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

/**
 * An IF statement conditionally executes a body of code depending on whether a linked CONDITIONLIST evaluates to true or not.
 * Note that IFs create a new scope for their code bodies, so any variables declared within the code body will be accessible only from within that body (and any child bodies).

 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class IF extends Variable
{
	private CONDITIONLIST cl;
	private BODY codeBody;
	//ELSEIF and ELSE may be null.
	private ELSEIF elseif;
	private ELSE els;
	
	/**
	 * Gets the CONDITIONLIST code and parses it.
	 * Then parses the BODY.
	 * 
	 * Finally, it checks to see if there are any ELSEIFs or ELSEs, and if so, parses them.
	 * 
	 * @param b	the parent body
	 * @param c	the Code object
	 */
	public IF(BODY b, Code c)
	{
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		//token[0] is "if", so token[1] should be the open paren to the CONDITIONLIST
		if(tokens[1] != Terminals.OPENPAREN)
		{
			RobotInterpreter.error("IF", lineNum, code, "IF must open with (");
		}
		
		//The last token should always be a closeparen.
		if(tokens[tokens.length - 1] != Terminals.CLOSEPAREN)
		{
			RobotInterpreter.error("IF", lineNum, code, "IF must close with )");
		}
		
		//PARSE CONDITIONLIST
		//If we have more than 3 tokens, then we have at least something in the CONDITIONLIST.
		if(tokens.length > 3)
		{
			cl = new CONDITIONLIST(body, c, code.substring(4, code.length() - 1));
		}
		else RobotInterpreter.error("IF", lineNum, code, "IF must contain a condition list!");

		//PARSE BODY
		//Move on to the next line and parse the BODY.
		c.nextLine();
		codeBody = new BODY(body, c);
		
		c.nextLine();
		
		//PARSE ELSEIF
		//Get the next line. If we find an ELSEIF, parse it.
		String[] newTokens = Code.tokenize(c.currentLine());
		if(newTokens[0].equals(Terminals.ELSEIF))
		{
			elseif = new ELSEIF(body, c);
		}
		
		//We have either parsed the ELSEIF, in which case we are on a new line, or we have not parsed an ELSEIF, and so are on the same line.
		//To handle both cases, we get the tokens of the current line again.
		newTokens = Code.tokenize(c.currentLine());
		
		//PARSE ELSE
		//If we find an ELSE on this line, we parse it.
		//If we don't find an ELSE, we go back to the previous line.
		//This is because an ELSE follows either an IF or an ELSEIF, which, in both cases, went forward one line more than usual to check for the ELSE.
		//So, if an ELSE is not found, we must go back one line to return to normal execution order.
		if(newTokens[0].equals(Terminals.ELSE))
		{
			if(newTokens.length == 1)
			{
				els = new ELSE(body, c);
			}
			else RobotInterpreter.error("IF", lineNum, code, "Syntax error related to ELSE");
		}
		else c.prevLine();
	}
	
	/**
	 * @return	the first ELSEIF. If there are subsequent ones, must call the ELSEIF's own get function.
	 */
	public ELSEIF getElseIf()
	{
		return elseif;
	}
	
	/**
	 * @return	the ELSE, if there is one.
	 */
	public ELSE getElse()
	{
		return els;
	}
	
	/**
	 * @return	the code body for this statement
	 */
	public BODY getCodeBody()
	{
		return codeBody;
	}
	
	/**
	 * Simple print function: prints the if, and the elseif / else if we have them.
	 */
	public void print() 
	{
		RobotInterpreter.write("parse", "if (");
		cl.print();
		RobotInterpreter.writeln("parse", ")");
		codeBody.print();
		
		if(elseif != null)
		{
			RobotInterpreter.write("parse", Code.newline);
			elseif.print();
		}
		
		if(els != null)
		{
			RobotInterpreter.write("parse", Code.newline);
			els.print();
		}
	}

	/**
	 * First, we validate the condition list.
	 * Second, we validate the if body.
	 * Third, we validate the elseif, if it exists.
	 * Fourth, we validate the else, if it exists.
	 */
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating IF");

		cl.validate();
		codeBody.validate();
		
		if(elseif != null)
		{
			elseif.validate();
		}
		
		if(els != null)
		{
			els.validate();
		}
	}

	
	/**
	 * We get the result of executing the CONDITIONLIST.
	 * If it is true, we execute the codeBody.
	 * If it is not true, we execute the ELSEIF. This returns a boolean indicating whether it executed or not.
	 * If the ELSEIF did not execute, then we execute the ELSE.
	 * 
	 * @param args	this should always be null
	 * @return	this always returns null
	 */
	public Object execute(Object[] args) 
	{
		boolean go = (boolean) cl.execute(null); 
		if(go)
		{
			codeBody.execute(null);
		}
		else
		{
			//The ELSEIF may have multiple ELSEIFs embedded.
			//Therefore, we have to check a returned value to see if any of those ELSEIFs did, in fact, execute.
			boolean elsEx = false;
			if(elseif != null)
			{
				elsEx = (boolean) elseif.execute(null);
			}
		
			//If the ELSEIFs did not execute (or if there is no ELSEIFs) and we have an ELSE, execute the ELSE.
			if(!elsEx && els != null)
			{
				els.execute(null);
			}
		}
		
		return null;
	}
}
