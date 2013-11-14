package robotinterpreter.variables;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;

public class STMTLIST extends Variable
{
	private STMT stmt;
	private STMTLIST nextStmt;
	
	public STMTLIST(BODY b, Code c) 
	{
		body = b;
		while(c.currentLine().trim().length() == 0) 
		{ 
			c.nextLine();
		}
		
		if(c.currentLineNum() != body.getFinishLine())
			stmt = new STMT(body, c);
		
		c.nextLine();
		if(c.currentLine() != null && c.currentLineNum() != body.getFinishLine())
		{
			nextStmt = new STMTLIST(body, c);
		}
		else nextStmt = null;
	}
	
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

	//Validate the stmt and the next, if it exists
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating STMTLIST");
		stmt.validate();
		if(nextStmt != null)
		{
			nextStmt.validate();
		}
	}

	@Override
	public Object execute(Object args[]) 
	{
		stmt.execute(null);
		if(nextStmt != null)
		{
			nextStmt.execute(null);
		}
		return null;
	}
}
