package robotinterpreter.variables;

import robotinterpreter.Code;
import robotinterpreter.Interpreter;

/**
 * A STMTLIST is simply a linked-list interpretation for statements.
 * Each STMTLIST node is one node in the linked list and has a reference to its consitituant STMT, along with various helper functions.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class STMTLIST extends Variable
{
	private Interpreter interpreter;
	
	private STMT stmt;
	//This is null at the end of the list
	private STMTLIST nextStmt;
	
	/**
	 * Gets the next STMT, parses it, and moves on to the next one, if it exists.
	 * 
	 * @param b	the parent body
	 * @param c	the code object
	 */
	public STMTLIST(Interpreter in, BODY b, Code c) 
	{
		interpreter = in;
		body = b;
		//Skip over any empty lines (i.e. whitespace only)
		while(c.currentLine().trim().length() == 0) 
		{ 
			c.nextLine();
		}
		
		//If we are not at the end of the parent body, make a new STMT
		if(c.currentLineNum() != body.getFinishLine())
			stmt = new STMT(interpreter, body, c);
		
		c.nextLine();
		
		//If we are not at the end of the parent body or the end of the code, itself, add a new STMTLIST to the linked list
		if(c.currentLine() != null && c.currentLineNum() != body.getFinishLine())
		{
			nextStmt = new STMTLIST(interpreter, body, c);
		}
		else nextStmt = null;
	}
	
	/**
	 * @return	the STMT for this STMTLIST
	 */
	public STMT getStmt() 
	{
		return stmt;
	}
	
	/**
	 * @return	returns the next statement in the list. Returns NULL if this STMTLIST does not exist.
	 */
	public STMTLIST getNextStmt()
	{
		return nextStmt;
	}
	
	/**
	 * Print function. Prints the current stmt and, if the next STMTLIST exists, prints that.
	 */
	public void print() 
	{
		if(stmt != null)
		{
			stmt.print();
			interpreter.write("parse", Code.newline);
			
			if(nextStmt != null)
				nextStmt.print();
		}
		else interpreter.writeln("parse", "EMPTY STMTLIST");
	}

	/**
	 * Validation function. If the STMT exists, call its validation function. If the next STMTLIST exists, call its validation function
	 * 
	 */
	public void validate() 
	{
		interpreter.writeln("validate", "Validating STMTLIST");
		stmt.validate();
		if(nextStmt != null)
		{
			nextStmt.validate();
		}
	}

	/**
	 * Executes the current stmt. Tries to get a return value.
	 * We will only ever get a return value if we are in a codeBody. If we do get one, we stop immediately and return the value.
	 * If we do not get one, then we continue execution until we either get a return value or run out of stmts to execute.
	 * 
	 * @param args	any args passed to the body. This should always be null here.
	 * @return	the return value for the STMTLIST, which will only not be null if the STMT is a return STMT and we are in a codeBody.
	 * 
	 */
	public Object execute(Object args[]) 
	{
		Object retVal = null;
		retVal = stmt.execute(null);
		
		if(retVal != null)
		{
			return retVal;
		}
		else if(nextStmt != null)
		{
			return nextStmt.execute(null);
		}
		else return null;
	}
}
