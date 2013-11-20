package robotinterpreter.variables;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;

/**
 * class STMTLIST
 * 
 * A STMTLIST is simply a linked-list interpretation for statements.
 * Each STMTLIST node is one node in the linked list and has a reference to its consitituant STMT, along with various helper functions.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class STMTLIST extends Variable
{
	private STMT stmt;
	//This is null at the end of the list
	private STMTLIST nextStmt;
	
	/**
	 * public STMTLIST(BODY b, Code c)
	 * 
	 * Gets the next STMT, parses it, and moves on to the next one, if it exists.
	 * 
	 * @param b	the parent body
	 * @param c	the code object
	 */
	public STMTLIST(BODY b, Code c) 
	{
		body = b;
		//Skip over any empty lines (i.e. whitespace only)
		while(c.currentLine().trim().length() == 0) 
		{ 
			c.nextLine();
		}
		
		//If we are not at the end of the parent body, make a new STMT
		if(c.currentLineNum() != body.getFinishLine())
			stmt = new STMT(body, c);
		
		c.nextLine();
		
		//If we are not at the end of the parent body or the end of the code, itself, add a new STMTLIST to the linked list
		if(c.currentLine() != null && c.currentLineNum() != body.getFinishLine())
		{
			nextStmt = new STMTLIST(body, c);
		}
		else nextStmt = null;
	}
	
	/**
	 * public STMT getStmt()
	 *
	 * @return	the STMT for this STMTLIST
	 */
	public STMT getStmt() 
	{
		return stmt;
	}
	
	/**
	 * public STMTLIST getNextStmt()
	 * 
	 * @return	returns the next statement in the list. Returns NULL if this STMTLIST does not exist.
	 */
	public STMTLIST getNextStmt()
	{
		return nextStmt;
	}
	
	/**
	 * public void print()
	 * 
	 * Print function. Prints the current stmt and, if the next STMTLIST exists, prints that.
	 */
	public void print() 
	{
		if(stmt != null)
		{
			stmt.print();
			RobotInterpreter.write("parse", Code.newline);
			
			if(nextStmt != null)
				nextStmt.print();
		}
		else RobotInterpreter.writeln("parse", "EMPTY STMTLIST");
	}

	/**
	 * public void validate()
	 * 
	 * Validation function. If the STMT exists, call its validation function. If the next STMTLIST exists, call its validation function
	 * 
	 */
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating STMTLIST");
		stmt.validate();
		if(nextStmt != null)
		{
			nextStmt.validate();
		}
	}

	/**
	 * public Object execute(Object args[])
	 * 
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
