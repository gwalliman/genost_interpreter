package robotinterpreter.variables;

import robotinterpreter.Code;

public class STMTLIST extends Variable
{
	private BODY body;
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
			stmt = new STMT(c);
		
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
			System.out.print(Code.newline);
			
			if(nextStmt != null)
				nextStmt.print();
		}
		else System.out.println("EMPTY STMTLIST");
	}

	//Validate the stmt and the next, if it exists
	public void validate() 
	{
		System.out.println("Validating STMTLIST");
		stmt.validate();
		if(nextStmt != null)
		{
			nextStmt.validate();
		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
